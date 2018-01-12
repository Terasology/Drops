/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.lootPools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.lootPools.components.LootableComponent;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RegisterSystem
@Share(LootPool.class)
public class LootPool extends BaseComponentSystem {
    @In
    private PrefabManager prefabManager;

    @In
    EntityManager entityManager;

    private Map<String, List<LootableItem>> lootables = new HashMap<>();
    // sum of all frequencies of given group
    private Map<String, Long> randomTreshold = new HashMap<>();
    private Random random = new FastRandom();
    @Override
    public void preBegin() {
        final Collection<Prefab> prefabs = prefabManager.listPrefabs(LootableComponent.class);
        for (Prefab prefab : prefabs) {
            final LootableComponent component = prefab.getComponent(LootableComponent.class);
            for (LootableComponent.LootEntry entry : component.lootEntries) {
                if (!lootables.containsKey(entry.group)){
                    lootables.put(entry.group, new ArrayList<>());
                    randomTreshold.put(entry.group, 0L);
                }
                lootables.get(entry.group).add(new LootableItem(entry.frequency, prefab, entry.minAmount, entry.maxAmount));
                randomTreshold.put(entry.group, randomTreshold.get(entry.group) + entry.frequency);
            }
        }
    }

    /**
     * Sets seed for RNG to use.
     *
     * If you are going for really deterministic results, please use this method before every group of calls
     * to getRandomLoot(), with param being seed modified by some variable, like block position.
     * Otherwise, if there would be unexpected call to getRandomLoot() in meantime, RNG would be in
     * somehow modified state and you would receive different items. Returns itself
     *
     * @param seed The value to seed RNG with
     * @return This, for method chaining ({@code lootPool.setSeed(seed).getRandomLoot();})
     */
    public LootPool setSeed(long seed){
        this.random = new FastRandom(seed);
        return this;
    }

    /**
     *
     * @param group
     * @return
     */
    LootableItem getRandomLoot(@Nonnull String group){
        // When requesting group for which no items have been defined, return something from the default group
        if (!lootables.containsKey(group)) {
            group = "general";
        }
        long randomNumber = Math.abs(random.nextLong()) % randomTreshold.get(group);
        for (LootableItem item : lootables.get(group)){
            if (randomNumber < item.frequency) {
                return item;
            } else {
                randomNumber -= item.frequency;
            }
        }
        // return the last item - wouldn't be handled in the for loop
        return lootables.get(group).get(lootables.get(group).size() - 1);
    }

    LootableItem getRandomLoot(){
        return getRandomLoot("general");
    }

    List<EntityRef> toEntityList(LootableItem item){
        List<EntityRef> list = new ArrayList<>(item.maxAmount);
        int amount = random.nextInt(item.minAmount, item.maxAmount);
        for (int i = 0; i < amount; i++){
            list.add(entityManager.create(item.prefab));
        }
        return list;
    }
}

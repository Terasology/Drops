/*
 * Copyright 2020 MovingBlocks
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
package org.terasology.drops.loot;

import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RegisterSystem
@Share(LootSystem.class)
public class LootSystem extends BaseComponentSystem {
    @In
    private PrefabManager prefabManager;

    @In
    private EntityManager entityManager;

    private Map<Name, List<LootableItem>> lootables = new HashMap<>();
    // sum of all frequencies of given group
    private Map<Name, Long> randomThreshold = new HashMap<>();

    private Random random = new FastRandom();

    @Override
    public void preBegin() {
        final Collection<Prefab> prefabs = prefabManager.listPrefabs(LootableComponent.class);
        for (Prefab prefab : prefabs) {
            final LootableComponent component = prefab.getComponent(LootableComponent.class);
            for (LootableItem entry : component.lootEntries) {
                if (!lootables.containsKey(entry.group)) {
                    lootables.put(entry.group, new ArrayList<>());
                    randomThreshold.put(entry.group, 0L);
                }
                entry.prefab = entry.item.equals("this") ? prefab : prefabManager.getPrefab(entry.item.toLowerCase());
                lootables.get(entry.group).add(entry);
                randomThreshold.put(entry.group, randomThreshold.get(entry.group) + entry.frequency);
            }
        }
    }

    /**
     * Sets seed for RNG to use.
     * <p>
     * If you are going for really deterministic results, please use this method before every group of calls
     * to getRandomLoot(), with param being seed modified by some variable, like block position.
     * Otherwise, if there would be unexpected call to getRandomLoot() in meantime, RNG would be in
     * somehow modified state and you would receive different items. Returns itself.
     *
     * @param seed The value to seed RNG with
     * @return This, for method chaining ({@code lootPool.setSeed(seed).getRandomLoot();})
     */
    public LootSystem setSeed(long seed) {
        this.random = new FastRandom(seed);
        return this;
    }

    /**
     * Returns a random {@code LootableItem} from given group.
     *
     * @param group Group of lootables to retrieve item from
     * @return Random {@code LootableItem}
     */
    public LootableItem getRandomLoot(@Nonnull String group) {
        // When requesting group for which no items have been defined, return something from the default group
        if (!lootables.containsKey(group)) {
            group = "general";
        }
        long randomNumber = Math.abs(random.nextLong()) % randomThreshold.get(group);
        for (LootableItem item : lootables.get(group)) {
            if (randomNumber < item.frequency) {
                return item;
            } else {
                randomNumber -= item.frequency;
            }
        }
        // return the last item - wouldn't be handled in the for loop
        return lootables.get(group).get(lootables.get(group).size() - 1);
    }

    /**
     * Returns a random {@code LootableItem} from the "general" group
     *
     * @return Random {@code LootableItem}
     */
    public LootableItem getRandomLoot() {
        return getRandomLoot("general");
    }

    /**
     * Converts given {@code LootableItem} to a list of entityRefs of the given LootableItem
     *
     * @param item LootableItem to convert
     * @return List of entityRefs representing the lootableItem
     */
    public List<EntityRef> toEntityList(LootableItem item) {
        List<EntityRef> list = new ArrayList<>(item.maxAmount);
        int amount = random.nextInt(item.minAmount, item.maxAmount);
        for (int i = 0; i < amount; i++) {
            list.add(entityManager.create(item.prefab));
        }
        return list;
    }
}

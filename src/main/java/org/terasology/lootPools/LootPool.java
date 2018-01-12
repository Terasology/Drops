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
import java.util.List;
import java.util.Map;

@RegisterSystem
@Share(LootPool.class)
public class LootPool extends BaseComponentSystem {
    @In
    private PrefabManager prefabManager;

    @In EntityManager entityManager;

    private Map<String, List<LootableItemEntry>> lootables = new HashMap<>();
    private long randomTreshold = 0;
    private Random random = new FastRandom();
    @Override
    public void preBegin() {
        final Collection<Prefab> prefabs = prefabManager.listPrefabs(LootableComponent.class);
        for (Prefab prefab : prefabs) {
            final LootableComponent component = prefab.getComponent(LootableComponent.class);
            for (LootableComponent.LootEntry entry : component.lootEntries) {
                if (!lootables.containsKey(entry.group)){
                    lootables.put(entry.group, new ArrayList<>());
                }
                lootables.get(entry.group).add(new LootableItemEntry(entry.regularity, prefab, entry.quantity));
                randomTreshold += entry.regularity;
            }
        }
    }

    public LootPool setSeed(long seed){
        this.random = new FastRandom(seed);
        return this;
    }

    List<EntityRef> getRandomLoot(@Nonnull String group, int amount){
        List<EntityRef> list = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++){
            long item = random.nextLong() % randomTreshold;
            for (LootableItemEntry entry : lootables.get(group)){
                item -= entry.regularity;
                if (item <= 0) {
                    for (int j = 0; j < entry.quantity; j++){
                        list.add(entityManager.create(entry.prefab));
                    }
                }
            }
        }
        return list;
    }
}

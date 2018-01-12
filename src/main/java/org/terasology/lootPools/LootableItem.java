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

import org.terasology.entitySystem.prefab.Prefab;

public class LootableItem {
    
    /**
     * How often item appears.
     */
    public int frequency;

    /**
     * Prefab pointing at the randomly generated loot item.
     */
    public Prefab prefab;

    /**
     * When item should not be received in quantities that are greater than 1, this controls that amount.
     * <p>
     * Usable for instance when dealing with decorative blocks, which would be kinda useless when received just one.
     */
    public int minAmount;

    /**
     * When item should not be received in quantities that are greater than 1, this controls that amount.
     * <p>
     * Usable for instance when dealing with decorative blocks, which would be kinda useless when received just one.
     */
    public int maxAmount;

    LootableItem(int frequency, Prefab prefab, int minAmount, int maxAmount) {
        this.frequency = frequency;
        this.prefab = prefab;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }
}

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

import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.gestalt.naming.Name;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class LootableItem {
    
    /**
     * How often item appears.
     */
    public int frequency = 100;

    /**
     * Prefab pointing at the randomly generated loot item.
     */
    public Prefab prefab = null;

    /**
     * When item should not be received in quantities that are greater than 1, this controls that amount.
     * <p>
     * Usable for instance when dealing with decorative blocks, which would be kinda useless when received just one.
     */
    public int minAmount = 1;

    /**
     * When item should not be received in quantities that are greater than 1, this controls that amount.
     * <p>
     * Usable for instance when dealing with decorative blocks, which would be kinda useless when received just one.
     */
    public int maxAmount = 1;

    /**
     * Group this item belongs to. May be something like "weapons", "armor" as well as "iron", "wooden"
     */
    public Name group = new Name("general");

    /**
     * Item to apply this Loot settings to. "this" means it will be applied to item in whose prefab specified.
     * Other items may be specified in format "module:item"
     */
    public Name item = new Name("this");
}

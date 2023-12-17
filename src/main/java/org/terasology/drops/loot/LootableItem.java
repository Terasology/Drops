// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.loot;

import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.gestalt.naming.Name;
import org.terasology.reflection.MappedContainer;

@MappedContainer
public class LootableItem {

    public static final Name DEFAULT_GROUP = new Name("general");
    public static final Name THIS_PREFAB = new Name("this");

    /** How often item appears. */
    public int frequency = 100;

    /** Prefab pointing at the randomly generated loot item. */
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
    public Name group = DEFAULT_GROUP;

    /**
     * Item to apply this Loot settings to. "this" means it will be applied to item in whose prefab specified.
     * Other items may be specified in format "module:item"
     */
    public Name item = THIS_PREFAB;
}

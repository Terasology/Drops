// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.loot;

import org.terasology.entitySystem.Component;

import java.util.List;

public class LootableComponent implements Component {
    public List<LootableItem> lootEntries;
}




// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.loot;

import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class LootableComponent implements Component<LootableComponent> {
    public List<LootableItem> lootEntries;

    @Override
    public void copy(LootableComponent other) {
        this.lootEntries = other.lootEntries;
    }
}




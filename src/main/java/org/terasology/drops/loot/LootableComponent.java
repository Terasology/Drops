// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.loot;

import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;
import java.util.stream.Collectors;

public class LootableComponent implements Component<LootableComponent> {
    public List<LootableItem> lootEntries;

    @Override
    public void copy(LootableComponent other) {
        this.lootEntries = other.lootEntries.stream()
                .map(lootableItem -> {
                    LootableItem newLootableItem = new LootableItem();
                    newLootableItem.frequency = lootableItem.frequency;
                    newLootableItem.prefab = lootableItem.prefab;
                    newLootableItem.minAmount = lootableItem.minAmount;
                    newLootableItem.maxAmount = lootableItem.maxAmount;
                    newLootableItem.group = lootableItem.group;
                    newLootableItem.item = lootableItem.item;
                    return newLootableItem;
                }).collect(Collectors.toList());
    }
}




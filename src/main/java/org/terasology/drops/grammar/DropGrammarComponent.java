// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Map;

/**
 */
public final class DropGrammarComponent implements Component<DropGrammarComponent> {
    public List<String> blockDrops;
    public List<String> itemDrops;

    public Map<String, DropDefinition> droppedWithTool = Maps.newLinkedHashMap();

    @Override
    public void copyFrom(DropGrammarComponent other) {
        this.blockDrops = Lists.newArrayList(other.blockDrops);
        this.itemDrops = Lists.newArrayList(other.blockDrops);
        this.droppedWithTool = Maps.newLinkedHashMap();
        for (Map.Entry<String, DropDefinition> entry : other.droppedWithTool.entrySet()) {
            String key = entry.getKey();
            DropDefinition old = entry.getValue();
            DropDefinition newDrop = new DropDefinition();
            newDrop.blockDrops = Lists.newArrayList(old.blockDrops);
            newDrop.itemDrops = Lists.newArrayList(old.itemDrops);
            this.droppedWithTool.put(key, newDrop);
        }
    }

    @MappedContainer
    public static class DropDefinition {
        public List<String> blockDrops;
        public List<String> itemDrops;
    }
}

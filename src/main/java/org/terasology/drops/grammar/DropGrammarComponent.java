// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        this.droppedWithTool = other.droppedWithTool.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().copy()));
    }

    @MappedContainer
    public static class DropDefinition {
        public List<String> blockDrops;
        public List<String> itemDrops;

        /** Create a deep copy of this drop definition. */
        DropDefinition copy() {
            DropDefinition copy = new DropDefinition();
            copy.blockDrops = Lists.newArrayList(this.blockDrops);
            copy.itemDrops = Lists.newArrayList(this.itemDrops);
            return copy;
        }
    }
}

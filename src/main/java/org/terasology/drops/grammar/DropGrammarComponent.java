// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

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
    public void copy(DropGrammarComponent other) {
        this.blockDrops = other.blockDrops;
        this.itemDrops = other.blockDrops;
        this.droppedWithTool = other.droppedWithTool;
    }

    @MappedContainer
    public static class DropDefinition {
        public List<String> blockDrops;
        public List<String> itemDrops;
    }
}

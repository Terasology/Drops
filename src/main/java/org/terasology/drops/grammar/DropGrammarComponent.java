// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

import com.google.common.collect.Maps;
import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Map;

/**
 */
public final class DropGrammarComponent implements Component {
    public List<String> blockDrops;
    public List<String> itemDrops;

    public Map<String, DropDefinition> droppedWithTool = Maps.newLinkedHashMap();

    @MappedContainer
    public static class DropDefinition {
        public List<String> blockDrops;
        public List<String> itemDrops;
    }
}

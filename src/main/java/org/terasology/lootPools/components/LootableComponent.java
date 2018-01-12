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
package org.terasology.lootPools.components;

import org.terasology.entitySystem.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;

public class LootableComponent implements Component {
    List<LootEntry> lootEntries;


    @MappedContainer
    public static class LootEntry {
        /**
         * The higher the regularity, the more often marked entity will appear
         */
        public long regularity = 100;
        public long quantity = 1;
        public String group = "general";
    }

}




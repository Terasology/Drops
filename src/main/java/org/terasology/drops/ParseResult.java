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
package org.terasology.drops;

/**
 * Denotes a drop parsed from a drop definition string.
 *
 * @see DropParser
 */
public class ParseResult {
    private final String drop;
    private final int count;

    public ParseResult(String drop, int count) {
        this.drop = drop;
        this.count = count;
    }

    /**
     * The identifier of the drop parsed from a drop definition string.
     * <p>
     * Note that the string is not checked to be a valid block or item identifier.
     *
     * @return the identifier of the drop
     */
    public String getDrop() {
        return drop;
    }

    /**
     * The (possibly random) count for the object to drop.
     * <p>
     * If the drop is specified with a range the value of count is a random number within that range determined by the
     * {@link DropParser};
     *
     * @return the drop count, greater or equal to zero
     */
    public int getCount() {
        return count;
    }
}

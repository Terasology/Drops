// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

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

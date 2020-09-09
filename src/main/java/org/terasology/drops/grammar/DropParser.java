// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

import org.terasology.engine.utilities.random.Random;

import java.util.Optional;

/**
 * <pre>
 *    DROP_DEFINITION   := [CHANCE '|'] [COUNT '*'] DROP
 *    CHANCE            := FLOAT in [0..1]
 *    COUNT             := INT | RANGE
 *    RANGE             := INT '-' INT
 *    DROP              := STRING
 * </pre>
 * <p>
 * Multiply a drop definition by a fixed <i>factor</i>:
 * <pre>
 *     42 * CoreAssets:Dirt
 * </pre>
 * <p>
 * Define a <i>range</i> multiplier from which a random value is picked:
 * <pre>
 *     2-5 * CoreAssets:Dirt
 * </pre>
 */
class DropParser {
    final private Random rnd;

    /**
     * Instantiate a parser for the given drop string with the specified RNG.
     *
     * @param rnd the random number generator used to determine a drop count for a range
     */
    DropParser(final Random rnd) {
        this.rnd = rnd;
    }

    /**
     * Computes the drop and drop count for the string given at initialization.
     * <p>
     * Subsequent invocations may change the count, but will never change the drop.
     *
     * @param drop the full drop string to be parsed
     * @return this parser with updated values for drop and drop count.
     */
    public Optional<ParseResult> invoke(final String drop) {
        String resultDrop = drop;

        boolean dropping = true;
        int pipeIndex = resultDrop.indexOf('|');
        if (pipeIndex > -1) {
            float chance = Float.parseFloat(resultDrop.substring(0, pipeIndex).trim());
            if (rnd.nextFloat() >= chance) {
                dropping = false;
            }
            resultDrop = resultDrop.substring(pipeIndex + 1);
        }

        if (!dropping) {
            return Optional.empty();
        }

        int timesIndex = resultDrop.indexOf('*');
        int countMin = 1;
        int countMax = 1;

        if (timesIndex > -1) {
            String timesStr = resultDrop.substring(0, timesIndex).trim();
            int minusIndex = timesStr.indexOf('-');
            if (minusIndex > -1) {
                countMin = Integer.parseInt(timesStr.substring(0, minusIndex).trim());
                countMax = Integer.parseInt(timesStr.substring(minusIndex + 1).trim());
            } else {
                countMin = Integer.parseInt(timesStr.trim());
                countMax = countMin;
            }
            resultDrop = resultDrop.substring(timesIndex + 1).trim();
        }

        int count = rnd.nextInt(countMin, countMax);
        return Optional.of(new ParseResult(resultDrop, count));
    }
}

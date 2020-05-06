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

import org.terasology.utilities.random.Random;

/**
 * Parser to
 *
 * Multiply a drop definition by a fixed <i>factor</i>:
 * <pre>
 *     42 * CoreAssets:Dirt
 * </pre>
 *
 * Define a <i>range</i> multiplier from which a random value is picked:
 * <pre>
 *     2-5 * CoreAssets:Dirt
 * </pre>
 *
 */
class DropParser {
    final private Random rnd;
    final private String drop;
    private int count;
    private String resultDrop;

    /**
     * Instantiate a parser for the given drop string with the specified RNG.
     *
     * @param rnd the random number generator used to determine a drop count for a range
     * @param drop the full drop string to be parsed
     */
    DropParser(final Random rnd, final String drop) {
        this.rnd = rnd;
        this.drop = drop;
    }

    /**
     * The identifier of the drop parsed from string given at initialization.
     *
     * Note that the string is not checked to be a valid block or item identifier.
     *
     * @return the identifier of the drop, or null if the parser has not been invoked
     */
    public String getDrop() {
        return resultDrop;
    }

    /**
     * A (possibly random) count for the object to drop.
     *
     * If the drop is specified within a range the value of count may change with subsequent invocations of the
     * parser.
     *
     * @return the drop count - may be zero if the parser has not been invoked
     */
    public int getCount() {
        return count;
    }

    /**
     * Computes the drop and drop count for the string given at initialization.
     *
     * Subsequent invocations may change the count, but will never change the drop.
     *
     * @return this parser with updated values for drop and drop count.
     */
    public DropParser invoke() {
        resultDrop = drop;
        int timesIndex = resultDrop.indexOf('*');
        int countMin = 1;
        int countMax = 1;

        if (timesIndex > -1) {
            String timesStr = resultDrop.substring(0, timesIndex);
            int minusIndex = timesStr.indexOf('-');
            if (minusIndex > -1) {
                countMin = Integer.parseInt(timesStr.substring(0, minusIndex));
                countMax = Integer.parseInt(timesStr.substring(minusIndex + 1));
            } else {
                countMin = Integer.parseInt(timesStr);
                countMax = countMin;
            }
            resultDrop = resultDrop.substring(timesIndex + 1);
        }

        count = rnd.nextInt(countMin, countMax);
        return this;
    }
}

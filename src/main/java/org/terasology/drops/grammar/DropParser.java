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
package org.terasology.drops.grammar;

import org.terasology.utilities.random.Random;

/**
 * <pre>
 *    DROP_DEFINITION   := [CHANCE '|'] [COUNT '*'] DROP
 *    CHANCE            := FLOAT in [0..1]
 *    COUNT             := INT | RANGE
 *    RANGE             := INT '-' INT
 *    DROP              := STRING
 * </pre>
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
     *
     * Subsequent invocations may change the count, but will never change the drop.
     *
     * @param drop the full drop string to be parsed
     *
     * @return this parser with updated values for drop and drop count.
     */
    public ParseResult invoke(final String drop) {
        String resultDrop = drop;
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

        int count = rnd.nextInt(countMin, countMax);
        return new ParseResult(resultDrop, count);
    }
}

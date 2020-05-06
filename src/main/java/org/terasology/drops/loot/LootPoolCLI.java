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
package org.terasology.drops.loot;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;

@RegisterSystem
public class LootPoolCLI extends BaseComponentSystem {

    @In
    private Console console;

    @In
    private LootSystem lootSystem;

    /**
     * Quick and dirty solution - fails if core is not set-up
     *
     * Since this module is kind of API, it's fine.
     * @param client
     * @param group
     * @return
     */
    @Command(
            shortDescription = "Adds a random item from a specified loot pool, defaults to general. DO NOT USE if possible",
            requiredPermission = PermissionManager.CHEAT_PERMISSION
    )
    public String giveRandomLootItem(@Sender EntityRef client,
                                     @CommandParam(value = "group", required = false) String group) {
        Random random = new FastRandom();
        LootableItem randomLoot;
        if (group != null){
            randomLoot = lootSystem.getRandomLoot(group);
        }
        else {
            randomLoot = lootSystem.getRandomLoot();
        }
        int amount = random.nextInt(randomLoot.minAmount, randomLoot.maxAmount);
        String name = randomLoot.prefab.getName();
        boolean result = console.execute("give " + name + " " + amount, client);
        return result ? "Success" : "Fail";
    }
}

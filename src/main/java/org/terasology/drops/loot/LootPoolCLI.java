// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
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
     * <p>
     * Since this module is kind of API, it's fine.
     *
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
        if (group != null) {
            randomLoot = lootSystem.getRandomLoot(group);
        } else {
            randomLoot = lootSystem.getRandomLoot();
        }
        int amount = random.nextInt(randomLoot.minAmount, randomLoot.maxAmount);
        String name = randomLoot.prefab.getName();
        boolean result = console.execute("give " + name + " " + amount, client);
        return result ? "Success" : "Fail";
    }
}

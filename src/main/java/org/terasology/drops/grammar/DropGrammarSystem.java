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

import com.google.common.collect.Lists;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.health.DoDestroyEvent;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.logic.inventory.events.DropItemEvent;
import org.terasology.logic.inventory.events.GiveItemEvent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.geom.Vector3f;
import org.terasology.physics.events.ImpulseEvent;
import org.terasology.registry.In;
import org.terasology.utilities.random.FastRandom;
import org.terasology.utilities.random.Random;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.entity.CreateBlockDropsEvent;
import org.terasology.world.block.entity.damage.BlockDamageModifierComponent;
import org.terasology.world.block.items.BlockItemFactory;

import java.util.List;
import java.util.function.Function;

/**
 * Drops objects specified by a {@link DropGrammarComponent} when an entity with that component is destroyed.
 *
 * A {@link DropParser} is used to determine the (possibly) random drop.
 * Each drop definition is evaluated separately without affecting other definitions. Each drop chance is for a single
 * drop definition.
 *
 * @see DoDestroyEvent
 * @see DropGrammarComponent
 * @see DropParser
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class DropGrammarSystem extends BaseComponentSystem {
    @In
    private EntityManager entityManager;
    @In
    private BlockManager blockManager;

    private BlockItemFactory blockItemFactory;
    private Random random;
    private DropParser parser;

    @Override
    public void initialise() {
        blockItemFactory = new BlockItemFactory(entityManager);
        random = new FastRandom();
        parser = new DropParser(random);
    }

    @ReceiveEvent(components = {DropGrammarComponent.class})
    public void whenBlockDropped(CreateBlockDropsEvent event, EntityRef blockEntity) {
        event.consume();
    }

    @ReceiveEvent
    public void onDestroyed(DoDestroyEvent event, EntityRef entity, DropGrammarComponent blockDrop, LocationComponent locationComp) {
        BlockDamageModifierComponent blockDamageModifierComponent = event.getDamageType().getComponent(BlockDamageModifierComponent.class);
        float chanceOfBlockDrop = 1;

        if (blockDamageModifierComponent != null) {
            chanceOfBlockDrop = 1 - blockDamageModifierComponent.blockAnnihilationChance;
        }

        if (random.nextFloat() < chanceOfBlockDrop) {
            List<String> blockDrops = blockDrop.blockDrops;
            List<String> itemDrops = blockDrop.itemDrops;

            if (blockDamageModifierComponent != null && blockDrop.droppedWithTool != null) {
                for (String toolType : blockDamageModifierComponent.materialDamageMultiplier.keySet()) {
                    if (blockDrop.droppedWithTool.containsKey(toolType)) {
                        DropGrammarComponent.DropDefinition dropDefinition = blockDrop.droppedWithTool.get(toolType);
                        blockDrops = dropDefinition.blockDrops;
                        itemDrops = dropDefinition.itemDrops;
                        break;
                    }
                }
            }

            if (blockDrops != null) {
                for (String drop : blockDrops) {
                    parser.invoke(drop).ifPresent(dropParseResult -> {
                        EntityRef dropItem = blockItemFactory.newInstance(blockManager.getBlockFamily(dropParseResult.getDrop()), dropParseResult.getCount());
                        if (shouldDropToWorld(event, blockDamageModifierComponent, dropItem)) {
                            createDrop(dropItem, locationComp.getWorldPosition(), true);
                        }
                    });
                }
            }

            if (itemDrops != null) {
                for (String drop : itemDrops) {
                    parser.invoke(drop).ifPresent(dropParseResult -> {
                        EntityBuilder dropEntity = entityManager.newBuilder(dropParseResult.getDrop());
                        if (dropParseResult.getCount() > 1) {
                            ItemComponent itemComponent = dropEntity.getComponent(ItemComponent.class);
                            itemComponent.stackCount = (byte) dropParseResult.getCount();
                        }
                        EntityRef dropItem = dropEntity.build();
                        if (shouldDropToWorld(event, blockDamageModifierComponent, dropItem)) {
                            createDrop(dropItem, locationComp.getWorldPosition(), false);
                        }
                    });
                }
            }
        }
    }

    private boolean shouldDropToWorld(DoDestroyEvent event, BlockDamageModifierComponent blockDamageModifierComponent, EntityRef dropItem) {
        return blockDamageModifierComponent == null || !blockDamageModifierComponent.directPickup
                || !giveItem(event.getInstigator(), dropItem);
    }

    private boolean giveItem(EntityRef instigator, EntityRef dropItem) {
        GiveItemEvent giveItemEvent = new GiveItemEvent(instigator);
        dropItem.send(giveItemEvent);
        return giveItemEvent.isHandled();
    }

    private void createDrop(EntityRef item, Vector3f location, boolean applyMovement) {
        item.send(new DropItemEvent(location));
        if (applyMovement) {
            item.send(new ImpulseEvent(random.nextVector3f(30.0f, new org.joml.Vector3f())));
        }
    }
}

// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.drops.grammar;

import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.destruction.DoDestroyEvent;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.logic.inventory.events.DropItemEvent;
import org.terasology.engine.logic.inventory.events.GiveItemEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.physics.events.ImpulseEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.entity.CreateBlockDropsEvent;
import org.terasology.engine.world.block.entity.damage.BlockDamageModifierComponent;
import org.terasology.engine.world.block.items.BlockItemFactory;
import org.terasology.math.geom.Vector3f;

import java.util.List;

/**
 * Drops objects specified by a {@link DropGrammarComponent} when an entity with that component is destroyed.
 * <p>
 * A {@link DropParser} is used to determine the (possibly) random drop. Each drop definition is evaluated separately
 * without affecting other definitions. Each drop chance is for a single drop definition.
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
    public void onDestroyed(DoDestroyEvent event, EntityRef entity, DropGrammarComponent blockDrop,
                            LocationComponent locationComp) {
        BlockDamageModifierComponent blockDamageModifierComponent =
                event.getDamageType().getComponent(BlockDamageModifierComponent.class);
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
                        EntityRef dropItem =
                                blockItemFactory.newInstance(blockManager.getBlockFamily(dropParseResult.getDrop()),
                                        dropParseResult.getCount());
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

    private boolean shouldDropToWorld(DoDestroyEvent event, BlockDamageModifierComponent blockDamageModifierComponent
            , EntityRef dropItem) {
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
            item.send(new ImpulseEvent(random.nextVector3f(30.0f)));
        }
    }
}

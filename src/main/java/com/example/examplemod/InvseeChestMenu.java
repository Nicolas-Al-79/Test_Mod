package com.example.examplemod;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class InvseeChestMenu extends AbstractContainerMenu {

    private static final int CONTAINER_ROWS = 5;
    private static final int CONTAINER_SIZE = 45;

    private static final int BLOCKED_START = 5;
    private static final int BLOCKED_END = 8;

    private final Container container;

    public InvseeChestMenu(
            int containerId,
            Inventory playerInventory,
            Container container
    ) {
        super(MenuType.GENERIC_9x5, containerId);

        checkContainerSize(container, CONTAINER_SIZE);

        this.container = container;

        container.startOpen(playerInventory.player);

        // -----------------------------
        // INVENTÁRIO DO JOGADOR ALVO
        // -----------------------------

        for (int row = 0; row < CONTAINER_ROWS; row++) {
            for (int column = 0; column < 9; column++) {

                int index = column + row * 9;

                int x = 8 + column * 18;
                int y = 18 + row * 18;

                if (index <= 3) {
                    final EquipmentSlot equipmentSlot = switch (index) {
                        case 0 -> EquipmentSlot.HEAD;
                        case 1 -> EquipmentSlot.CHEST;
                        case 2 -> EquipmentSlot.LEGS;
                        case 3 -> EquipmentSlot.FEET;
                        default -> throw new IllegalStateException();
                    };
                    addSlot(new Slot(container, index, x, y) {
                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return stack.canEquip(equipmentSlot, playerInventory.player);
                        }
                    });
                }

                else if (isBlockedSlot(index)) {

                    addSlot(new Slot(container, index, x, y) {

                        @Override
                        public boolean mayPlace(ItemStack stack) {
                            return false;
                        }

                        @Override
                        public boolean mayPickup(Player player) {
                            return false;
                        }
                    });

                } else {

                    addSlot(new Slot(
                            container,
                            index,
                            x,
                            y
                    ));
                }
            }
        }

        // -----------------------------
        // INVENTÁRIO DO EXECUTOR
        // -----------------------------

        int verticalOffset = (CONTAINER_ROWS - 4) * 18;

        // Inventário principal
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {

                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        103 + row * 18 + verticalOffset
                ));
            }
        }

        // Hotbar
        for (int column = 0; column < 9; column++) {

            addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    161 + verticalOffset
            ));
        }
    }

    private static boolean isBlockedSlot(int slot) {
        return slot >= BLOCKED_START && slot <= BLOCKED_END;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public void clicked(
            int slotId,
            int button,
            ClickType clickType,
            Player player
    ) {

        /*
         * Ignora completamente qualquer clique
         * direcionado aos slots 5-8.
         */
        if (isBlockedSlot(slotId)) {
            return;
        }

        super.clicked(
                slotId,
                button,
                clickType,
                player
        );
    }

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index
    ) {

        ItemStack result = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);

        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        /*
         * Segurança adicional.
         *
         * Mesmo que quickMoveStack seja chamado diretamente
         * para um dos slots bloqueados, nada acontece.
         */
        if (isBlockedSlot(index)) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if (index < CONTAINER_SIZE) {

            /*
             * Inventário alvo -> inventário executor.
             */
            if (!moveItemStackTo(
                    stack,
                    CONTAINER_SIZE,
                    this.slots.size(),
                    true
            )) {
                return ItemStack.EMPTY;
            }

        } else {

            /*
             * Inventário executor -> inventário alvo.
             *
             * Usamos apenas slots 9-44.
             *
             * Portanto:
             *
             * 0-4 = armadura/offhand
             * 5-8 = bloqueados
             *
             * nunca recebem Shift+Click.
             */
            if (!moveItemStackTo(
                    stack,
                    9,
                    CONTAINER_SIZE,
                    false
            )) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);

        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        container.stopOpen(player);
    }
}
package com.tonkaew131.tkclothslot;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class TkClothSlot extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // event.getPlayer().sendMessage(Component.text("สวัสดีครับพี่น้องประชาชน, " + event.getPlayer().getName() + "!"));
        Player player = event.getPlayer();
        Inventory playerInventory = player.getInventory();

        LockPlayerSlot(playerInventory, player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();

        if (clickedItem == null) {
            return;
        }

        Material itemType = clickedItem.getType();
        Component itemName = clickedItem.getItemMeta().displayName();
        if (itemName == null && itemType == Material.BARRIER) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInventoryChange(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();
        int slotChanged = event.getSlot();

        // Not Armor Slot
        if (slotChanged > 36 || slotChanged < 39) {
            return;
        }

        Inventory playerInventory = player.getInventory();
        ItemStack changedItem = playerInventory.getItem(slotChanged);
        if (changedItem == null) {
            return;
        }

        // 39, 38, 37, 36: Helmet, Chestplate, Leggings, Boots
        int LETTER_HELMET_UNLOCK = 0;
        int LETTER_CHESTPLATE_UNLOCK = 4;
        int LETTER_LEGGINGS_UNLOCK = 2;
        int LETTER_BOOTS_UNLOCK = 0;

        int CHAIN_HELMET_UNLOCK = 0;
        int CHAIN_CHESTPLATE_UNLOCK = 4;
        int CHAIN_LEGGINGS_UNLOCK = 4;
        int CHAIN_BOOTS_UNLOCK = 0;

        int IRON_HELMET_UNLOCK = 0;
        int IRON_CHESTPLATE_UNLOCK = 6;
        int IRON_LEGGINGS_UNLOCK = 4;
        int IRON_BOOTS_UNLOCK = 1;

        int totalSlotRemoved = 0;
        LockPlayerSlot(playerInventory, player);

        Material playerHelmet = playerInventory.getItem(39).getType();
        if (playerHelmet == Material.LEATHER_HELMET) totalSlotRemoved += LETTER_HELMET_UNLOCK;
        if (playerHelmet == Material.CHAINMAIL_HELMET) totalSlotRemoved += CHAIN_HELMET_UNLOCK;
        if (playerHelmet == Material.IRON_HELMET) totalSlotRemoved += IRON_HELMET_UNLOCK;

        Material playerChestplate = playerInventory.getItem(38).getType();
        if (playerChestplate == Material.LEATHER_CHESTPLATE) totalSlotRemoved += LETTER_CHESTPLATE_UNLOCK;
        if (playerChestplate == Material.CHAINMAIL_CHESTPLATE) totalSlotRemoved += CHAIN_CHESTPLATE_UNLOCK;
        if (playerChestplate == Material.IRON_CHESTPLATE) totalSlotRemoved += IRON_CHESTPLATE_UNLOCK;

        Material playerLeggings = playerInventory.getItem(37).getType();
        if (playerLeggings == Material.LEATHER_LEGGINGS) totalSlotRemoved += LETTER_LEGGINGS_UNLOCK;
        if (playerLeggings == Material.CHAINMAIL_LEGGINGS) totalSlotRemoved += CHAIN_LEGGINGS_UNLOCK;
        if (playerLeggings == Material.IRON_LEGGINGS) totalSlotRemoved += IRON_LEGGINGS_UNLOCK;

        Material playerBoots = playerInventory.getItem(36).getType();
        if (playerBoots == Material.LEATHER_BOOTS) totalSlotRemoved += LETTER_BOOTS_UNLOCK;
        if (playerBoots == Material.CHAINMAIL_BOOTS) totalSlotRemoved += CHAIN_BOOTS_UNLOCK;
        if (playerBoots == Material.IRON_BOOTS) totalSlotRemoved += IRON_BOOTS_UNLOCK;

        int SLOT_TO_REMOVED[] = {9, 18, 10, 19, 11, 20, 12, 21, 13, 22, 14, 23, 15, 24, 16, 25, 17, 26};
        for (int i = 0; i < totalSlotRemoved; i++) {
            playerInventory.setItem(SLOT_TO_REMOVED[i], null);
        }

        player.sendMessage(Component.text("จะเพิ่ม Slot ให้ " + totalSlotRemoved));
        // player.sendMessage(Component.text("Slot ที่ " + slotChanged + " เป็น " + itemType.toString()));
    }

    ItemStack lockedItem() {
        ItemStack lockedItem = new ItemStack(Material.BARRIER);
        ItemMeta lockedItemMeta = lockedItem.getItemMeta();
        lockedItemMeta.displayName(Component.text(""));
        lockedItem.setItemMeta(lockedItemMeta);
        return lockedItem;
    }

    void LockPlayerSlot(Inventory playerInventory, Player player) {
        ItemStack lockedItem = lockedItem();

        int START = 9;
        int END = 35;
        for (int i = START; i <= END; i++) {
            if(playerInventory.getItem(i) != lockedItem) {
                player.getWorld().dropItemNaturally(player.getLocation(), playerInventory.getItem(i));
            }

            playerInventory.setItem(i, lockedItem);
        }
        int FREE_START = 27;
        int FREE_END = 30;
        for (int i = FREE_START; i <= FREE_END; i++) {
            playerInventory.setItem(i, null);
        }
    }
}

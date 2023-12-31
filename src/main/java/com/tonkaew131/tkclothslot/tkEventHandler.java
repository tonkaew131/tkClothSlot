package com.tonkaew131.tkclothslot;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Map;

public class tkEventHandler implements Listener {
    public Map<String, Object> config;
    public Plugin plugin;

    public tkEventHandler(Map<String, Object> config, Plugin pluginInstance) {
        this.config = config;
        this.plugin = pluginInstance;
    }

    public void checkAndClearInventory(Player player) {
        Inventory inventory = player.getInventory();

        ArrayList<Integer> slotToRemoved = (ArrayList<Integer>) this.config.get("slot_locked");

        ItemStack lockedItem = lockedItem();
        ItemStack lockedHotBar = getLockedHotBar();

        // 39, 38, 37, 36: Helmet, Chestplate, Leggings, Boots
        // 0 - 8 Hot bar

        for (int i = 0; i <= slotToRemoved.size(); i++) {
            if (inventory.getItem(i) != null && !isLockedItem(inventory.getItem(i))) {
                player.getWorld().dropItemNaturally(player.getLocation(), inventory.getItem(i));
            }

            if (i >= 0 && i <= 8) {
                inventory.setItem(i, lockedHotBar);
            } else {
                inventory.setItem(i, lockedItem);
            }
        }

        player.updateInventory();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item != null && isLockedItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // event.getPlayer().sendMessage(Component.text("สวัสดีครับพี่น้องประชาชน, " + event.getPlayer().getName() + "!"));
        Player player = event.getPlayer();

        // If Creative
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Inventory playerInventory = player.getInventory();

        ItemStack lockedItem = lockedItem();

        int START = 9;
        int END = 35;
        for (int i = START; i <= END; i++) {
            if (playerInventory.getItem(i) != null && !isLockedItem(playerInventory.getItem(i))) {
                player.getWorld().dropItemNaturally(player.getLocation(), playerInventory.getItem(i));
            }

            playerInventory.setItem(i, lockedItem);
        }

        playerInventory.setItem(0, lockedItem);
        playerInventory.setItem(1, lockedItem);
        playerInventory.setItem(2, lockedItem);
        playerInventory.setItem(6, lockedItem);
        playerInventory.setItem(7, lockedItem);
        playerInventory.setItem(8, lockedItem);

        int FREE_START = 27;
        int FREE_END = 30;
        for (int i = FREE_START; i <= FREE_END; i++) {
            playerInventory.setItem(i, null);
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();

        if (clickedItem == null) {
            return;
        }

        // If Creative
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (isLockedItem(clickedItem)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerInventoryChange(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();
        int slotChanged = event.getSlot();

        // If Creative
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Not Armor Slot
        /*
        if (slotChanged > 36 || slotChanged < 39) {
            return;
        }*/

        Inventory playerInventory = player.getInventory();

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

        ItemStack playerHelmet = playerInventory.getItem(39);
        if (playerHelmet != null) {
            Material helmetType = playerHelmet.getType();
            if (helmetType == Material.LEATHER_HELMET) totalSlotRemoved += LETTER_HELMET_UNLOCK;
            if (helmetType == Material.CHAINMAIL_HELMET) totalSlotRemoved += CHAIN_HELMET_UNLOCK;
            if (helmetType == Material.IRON_HELMET) totalSlotRemoved += IRON_HELMET_UNLOCK;
        }

        ItemStack playerChestplate = playerInventory.getItem(38);
        if (playerChestplate != null) {
            Material chestPlateType = playerChestplate.getType();
            if (chestPlateType == Material.LEATHER_CHESTPLATE) totalSlotRemoved += LETTER_CHESTPLATE_UNLOCK;
            if (chestPlateType == Material.CHAINMAIL_CHESTPLATE) totalSlotRemoved += CHAIN_CHESTPLATE_UNLOCK;
            if (chestPlateType == Material.IRON_CHESTPLATE) totalSlotRemoved += IRON_CHESTPLATE_UNLOCK;
        }

        ItemStack playerLeggings = playerInventory.getItem(37);
        if (playerLeggings != null) {
            Material leggingsType = playerLeggings.getType();
            if (leggingsType == Material.LEATHER_LEGGINGS) totalSlotRemoved += LETTER_LEGGINGS_UNLOCK;
            if (leggingsType == Material.CHAINMAIL_LEGGINGS) totalSlotRemoved += CHAIN_LEGGINGS_UNLOCK;
            if (leggingsType == Material.IRON_LEGGINGS) totalSlotRemoved += IRON_LEGGINGS_UNLOCK;
        }

        ItemStack playerBoots = playerInventory.getItem(36);
        if (playerBoots != null) {
            Material bootsType = playerBoots.getType();
            if (bootsType == Material.LEATHER_BOOTS) totalSlotRemoved += LETTER_BOOTS_UNLOCK;
            if (bootsType == Material.CHAINMAIL_BOOTS) totalSlotRemoved += CHAIN_BOOTS_UNLOCK;
            if (bootsType == Material.IRON_BOOTS) totalSlotRemoved += IRON_BOOTS_UNLOCK;
        }

        ItemStack lockedItem = lockedItem();

        ArrayList<Integer> slotToRemoved = new ArrayList<Integer>();

        int START = 9;
        int END = 35;
        for (int i = START; i <= END; i++) {
            slotToRemoved.add(i);
        }
        int FREE_START = 27;
        int FREE_END = 30;
        for (int i = FREE_START; i <= FREE_END; i++) {
            slotToRemoved.remove(slotToRemoved.indexOf(i));
        }

        slotToRemoved.add(0);
        slotToRemoved.add(1);
        slotToRemoved.add(2);
        slotToRemoved.add(6);
        slotToRemoved.add(7);
        slotToRemoved.add(8);

        int SLOT_TO_ADDED[] = {9, 18, 10, 19, 11, 20, 12, 21, 13, 22, 14, 23, 15, 24, 16, 25, 17, 26};
        for (int i = 0; i < totalSlotRemoved; i++) {
            slotToRemoved.remove(slotToRemoved.indexOf(SLOT_TO_ADDED[i]));
        }

        // Removed Old Barrier
        for (int i = 0; i <= 40; i++) {
            if (playerInventory.getItem(i) != null && isLockedItem(playerInventory.getItem(i))) {
                playerInventory.setItem(i, null);
            }
        }

        for (int i = 0; i < slotToRemoved.size(); i++) {
            int _slot = slotToRemoved.get(i);
            if (playerInventory.getItem(_slot) != null && !isLockedItem(playerInventory.getItem(_slot))) {
                player.getWorld().dropItemNaturally(player.getLocation(), playerInventory.getItem(_slot));
            }

            playerInventory.setItem(_slot, lockedItem);
        }
    }

    /*@EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Inventory playerInventory = player.getInventory();

        List<ItemStack> dropItems = event.getDrops();
        dropItems.removeIf(item -> {
            if (item != null && item == lockedItem()) {
                return true;
            }
            return false;
        });
    }*/

    ItemStack lockedItem() {
        ItemStack lockedItem = new ItemStack(Material.ECHO_SHARD);
        ItemMeta lockedItemMeta = lockedItem.getItemMeta();
        lockedItemMeta.displayName(Component.text(""));
        lockedItem.setItemMeta(lockedItemMeta);
        return lockedItem;
    }

    ItemStack getLockedHotBar() {
        ItemStack lockedItem = new ItemStack(Material.ECHO_SHARD);
        ItemMeta lockedItemMeta = lockedItem.getItemMeta();
        lockedItemMeta.displayName(Component.text(""));

        NamespacedKey keyId = new NamespacedKey(this.plugin, "tkId");
        lockedItemMeta.getPersistentDataContainer().set(keyId, PersistentDataType.STRING, (String) this.config.get("slot_hotbar_id"));

        lockedItem.setItemMeta(lockedItemMeta);
        return lockedItem;
    }

    boolean isLockedItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        if (item.getType() == Material.ECHO_SHARD) {
            return true;
        }

        return false;
        /*TextComponent itemDisplayName = (TextComponent) itemMeta.displayName();
        String itemName = "";
        if (itemDisplayName != null) {
            itemName = itemDisplayName.content();
        }

        if (itemName == "" && item.getType() == Material.BARRIER) {
            return true;
        }

        return false;*/
    }
}

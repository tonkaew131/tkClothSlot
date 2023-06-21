package com.tonkaew131.tkclothslot;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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

        ItemStack lockedItem = lockedItem();

        int START = 9;
        int END = 35;
        for (int i = START; i <= END; i++) {
            if (playerInventory.getItem(i) != null && !isLockedItem(playerInventory.getItem(i))) {
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();

        if (clickedItem == null) {
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        /*Player player = event.getPlayer();
        Inventory playerInventory = player.getInventory();

        List<ItemStack> dropItems = event.getDrops();
        dropItems.removeIf(item -> {
            if (item != null && item == lockedItem()) {
                return true;
            }
            return false;
        });*/
    }

    ItemStack lockedItem() {
        ItemStack lockedItem = new ItemStack(Material.BARRIER);
        ItemMeta lockedItemMeta = lockedItem.getItemMeta();
        lockedItemMeta.displayName(Component.text(""));
        lockedItem.setItemMeta(lockedItemMeta);
        return lockedItem;
    }

    boolean isLockedItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        TextComponent itemDisplayName = (TextComponent) itemMeta.displayName();
        String itemName = "";
        if (itemDisplayName != null) {
            itemName = itemDisplayName.content();
        }

        if (itemName == "" && item.getType() == Material.BARRIER) {
            return true;
        }

        return false;
    }
}

package com.tonkaew131.tkclothslot;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TkClothSlot extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("สวัสดีครับพี่น้องประชาชน, " + event.getPlayer().getName() + "!"));
    }

    @EventHandler
    public void onPlayerInventoryChange(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();
        int slotChanged = event.getSlot();

        ItemStack changedItem = player.getInventory().getItem(slotChanged);
        Material itemType = changedItem.getType();

        player.sendMessage(Component.text("Slot ที่ "+slotChanged + " เป็น " +itemType.toString()));
    }
}

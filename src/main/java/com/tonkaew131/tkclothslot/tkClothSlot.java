package com.tonkaew131.tkclothslot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class tkClothSlot extends JavaPlugin {
    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Map<String, Object> map = new HashMap<>();
    private File configFile;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        configFile = new File(getDataFolder(), "config.json");
        if (!configFile.exists()) {
            getLogger().log(Level.INFO, "No config file found, creating...");
            FileWriter writer = null;

            try {
                writer = new FileWriter(configFile);
                writer.write("{\\\"slot_hotbar_id\\\":\\\"LOCKED_HOT\\\",\\\"slot_inv_id\\\":\\\"LOCKED_INV\\\",\\\"slot_locked\\\":[0,1,2,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,31,32,33,34,35]}");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                saveResource(configFile.getName(), false);
            } catch (IllegalArgumentException e) {
                try {
                    configFile.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        try {
            map = gson.fromJson(new FileReader(configFile), new HashMap<String, Object>().getClass());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new tkEventHandler(map, this), this);
    }

    @Override
    public void onDisable() {
    }
}

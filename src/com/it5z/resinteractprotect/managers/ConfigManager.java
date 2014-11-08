package com.it5z.resinteractprotect.managers;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
	private JavaPlugin plugin;
	private MessageManager messagemanager;
	private FileConfiguration config;
	
	public ConfigManager(ManagerBase plugin) {
		this.plugin = (JavaPlugin) plugin;
		messagemanager = ((ManagerBase)this.plugin).getMessageManager();
	}
	
	public void init() {
		messagemanager.sendConsoleMessage(ChatColor.GREEN + "读取配置文件。");
		if(! new File(plugin.getDataFolder(), "config.yml").exists()) {
			messagemanager.sendConsoleMessage(ChatColor.GREEN + "没有找到配置文件，正在生成。");
			plugin.saveDefaultConfig();
		}
		plugin.reloadConfig();
		config = plugin.getConfig();
	}
	
	public boolean getBoolean(String path, boolean def) {
		if(config == null) {
			throw new RuntimeException("ConfigManager没有初始化!");
		}
		boolean value = config.getBoolean(path, def);
		plugin.saveConfig();
		return value;
	}
	
	public List<String> getStringList(String path) {
		if(config == null) {
			throw new RuntimeException("ConfigManager没有初始化!");
		}
		List<String> value = config.getStringList(path);
		plugin.saveConfig();
		return value;
	}
}

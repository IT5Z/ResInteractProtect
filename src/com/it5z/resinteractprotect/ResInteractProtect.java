package com.it5z.resinteractprotect;

import com.bekvon.bukkit.residence.protection.FlagPermissions;
import com.it5z.resinteractprotect.listeners.PlayerListener;
import com.it5z.resinteractprotect.managers.ConfigManager;
import com.it5z.resinteractprotect.managers.ManagerBase;
import com.it5z.resinteractprotect.managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ResInteractProtect extends JavaPlugin implements ManagerBase {
	private static ResInteractProtect instance;
	private PluginManager pluginmanager;
	private MessageManager messagemanager;
	private ConfigManager configmanager;
	private PlayerListener playerlistener;
	
	@Override
	public void onEnable() {
		instance = this;
		pluginmanager = Bukkit.getPluginManager();
		messagemanager = new MessageManager(instance);
		configmanager = new ConfigManager(instance);
		configmanager.init();
		if(pluginmanager.getPlugin("Residence").isEnabled()) {
			FlagPermissions.addFlag("interact");
			messagemanager.sendConsoleMessage(ChatColor.GREEN + "挂接到Residence!");
		} else {
			messagemanager.sendConsoleMessage(ChatColor.GREEN + "没有找到Residence!");
		}
		playerlistener = new PlayerListener(instance);
		registerListener();
		messagemanager.sendConsoleMessage(ChatColor.GREEN + "插件已被加载!");
	}
	
	@Override
	public void onDisable() {
		messagemanager.sendConsoleMessage(ChatColor.GREEN + "插件已被卸载!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("resinteractprotect")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(ChatColor.AQUA + "=====ResInteractProtect帮助=====");
					sender.sendMessage(ChatColor.GREEN + "/" + label + " help\tResInteractProtect帮助");
					sender.sendMessage(ChatColor.GREEN + "/" + label + " version\tResInteractProtect版本信息");
					sender.sendMessage(ChatColor.GREEN + "/" + label + " reload\t重载ResInteractProtect");
				} else if(args[0].equalsIgnoreCase("version")) {
					PluginDescriptionFile description = instance.getDescription();
					sender.sendMessage(ChatColor.AQUA + "=====ResInteractProtect版本信息=====");
					sender.sendMessage(ChatColor.GREEN + "作者: " + description.getAuthors());
					sender.sendMessage(ChatColor.GREEN + "描述: " + description.getDescription());
					sender.sendMessage(ChatColor.GREEN + "版本: " + description.getVersion());
					sender.sendMessage(ChatColor.GREEN + "网站: " + description.getWebsite());
				} else if(args[0].equalsIgnoreCase("reload")) {
					configmanager.init();
					registerListener();
					messagemanager.sendMessage(ChatColor.GREEN + "ResInteractProtect已重载!", sender);
				} else {
					messagemanager.sendMessage(ChatColor.RED + "错误的指令!", sender);
				}
			} else {
				messagemanager.sendMessage(ChatColor.RED + "输入\"/" + label + " help\"查看帮助.", sender);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public static ResInteractProtect getInstance() {
		return instance;
	}
	
	@Override
	public MessageManager getMessageManager() {
		return messagemanager;
	}
	
	@Override
	public ConfigManager getConfigManager() {
		return configmanager;
	}
	
	private void registerListener() {
		HandlerList.unregisterAll(playerlistener);
		if(configmanager.getBoolean("Enable", false)) {
			pluginmanager.registerEvents(playerlistener, instance);
		}
	}
}

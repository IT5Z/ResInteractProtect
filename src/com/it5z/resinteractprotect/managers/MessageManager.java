package com.it5z.resinteractprotect.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager {
	private String prefix;
	private ConsoleCommandSender console;
	
	public MessageManager(ManagerBase plugin) {
		prefix = ((JavaPlugin)plugin).getDescription().getPrefix();
		console = Bukkit.getConsoleSender();
	}
	
	public void sendMessage(String string, CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "[" + prefix + "] " + ChatColor.RESET + string);
	}
	
	public void sendConsoleMessage(String string) {
		console.sendMessage(ChatColor.AQUA + "[" + prefix + "] " + ChatColor.RESET + string);
	}
}

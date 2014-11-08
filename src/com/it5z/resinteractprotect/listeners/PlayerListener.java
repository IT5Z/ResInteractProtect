package com.it5z.resinteractprotect.listeners;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.it5z.resinteractprotect.ResInteractProtect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("deprecation")
public class PlayerListener implements Listener {
	private ResInteractProtect plugin;
	private static final HashSet<Byte> TRANSPARENT_MATERIALS;
	private static final HashSet<Byte> REFRACTION_MATERIALS;
	
	static {
		TRANSPARENT_MATERIALS = new HashSet<Byte>();
		TRANSPARENT_MATERIALS.add((byte) Material.AIR.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.SAPLING.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.POWERED_RAIL.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.DETECTOR_RAIL.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.LONG_GRASS.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.DEAD_BUSH.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.YELLOW_FLOWER.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.RED_ROSE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.BROWN_MUSHROOM.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.RED_MUSHROOM.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.TORCH.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.REDSTONE_WIRE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.SEEDS.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.SIGN_POST.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.WOODEN_DOOR.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.LADDER.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.RAILS.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.WALL_SIGN.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.LEVER.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.STONE_PLATE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.IRON_DOOR_BLOCK.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.WOOD_PLATE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.REDSTONE_TORCH_OFF.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.REDSTONE_TORCH_ON.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.STONE_BUTTON.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.SNOW.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.SUGAR_CANE_BLOCK.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.DIODE_BLOCK_OFF.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.DIODE_BLOCK_ON.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.PUMPKIN_STEM.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.MELON_STEM.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.VINE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.FENCE_GATE.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.WATER_LILY.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.NETHER_WARTS.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.CARPET.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.WATER.getId());
		TRANSPARENT_MATERIALS.add((byte) Material.STATIONARY_WATER.getId());
		REFRACTION_MATERIALS = (HashSet<Byte>) TRANSPARENT_MATERIALS.clone();
		REFRACTION_MATERIALS.add((byte) Material.GLASS.getId());
		REFRACTION_MATERIALS.add((byte) Material.THIN_GLASS.getId());
		REFRACTION_MATERIALS.add((byte) Material.STAINED_GLASS.getId());
		REFRACTION_MATERIALS.add((byte) Material.STAINED_GLASS_PANE.getId());
	}

	public PlayerListener (ResInteractProtect plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void Interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(!Residence.isResAdminOn(player) && event.getAction().name().startsWith("RIGHT_CLICK_") && event.getMaterial() != Material.AIR && event.hasItem() && !event.isBlockInHand()) {
			ClaimedResidence cr = Residence.getResidenceManager().getByLoc(Check(player));
			if (cr != null) {
				if (!cr.getPermissions().playerHas(player.getName(), "interact", true)) {
					event.setCancelled(true);
					plugin.getMessageManager().sendMessage(ChatColor.RED + "你没有交互的权限!", player);
				}
			}
		}
	}

	public Location Check(Player player) {
		List<String> list = plugin.getConfigManager().getStringList("ListenerList");
		for(String line:list) {
			if(! line.isEmpty() && line.matches("(\\d+(:\\d+)?|\\*) (\\d+(:\\d+)?|\\*) [0-2]")) {
				String[] value = line.split(" ", 3);
				String[] id = value[0].split(":", 2);
				if(id.length >= 1) {
					ItemStack item = player.getItemInHand();
					boolean has;
					has = id[0].equals("*") || item.getTypeId() == Integer.parseInt(id[0]);
					if(has && id.length >= 2) {
						has = Integer.parseInt(id[1]) == item.getDurability();
					}
					if(has) {
						id = value[1].split(":", 2);
						if(id.length >= 1) {
							HashSet<Byte> transparent;
							switch (Integer.parseInt(value[2])) {
								case 1:
									transparent = TRANSPARENT_MATERIALS;
									break;
								case 2:
									transparent = REFRACTION_MATERIALS;
									break;
								default:
									transparent = null;
							}
							Block block = player.getTargetBlock(transparent, 300);
							if(block != null) {
								has = id[0].equals("*") || Integer.parseInt(id[0]) == block.getTypeId();
								if (has && id.length >= 2) {
									has = Integer.parseInt(id[1]) == block.getData();
								}
								if (has) {
									return block.getLocation();
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
}

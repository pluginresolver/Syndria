package minecade.dungeonrealms.LootMechanics.commands;

import java.util.HashMap;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.InstanceMechanics.InstanceMechanics;
import minecade.dungeonrealms.LootMechanics.LootMechanics;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandHideLoot implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(!p.isOp()) return true;
		if(args.length != 1) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.RED + "/hideloot <radius>");
			return true;
		}
		
		int radius = Integer.parseInt(args[0]);
		Location loc = p.getLocation();
		World w = loc.getWorld();
		int i, j, k;
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		
		for(i = -radius; i <= radius; i++) {
			for(j = -radius; j <= radius; j++) {
				for(k = -radius; k <= radius; k++) {
					loc = w.getBlockAt(x + i, y + j, z + k).getLocation();
					HashMap<Location, String> map = InstanceMechanics.instance_loot.get(loc.getWorld().getName());
					if(LootMechanics.loot_spawns.containsKey(loc) || (map!=null&& map.containsKey(loc))) {
					    final Location tem_loc = loc;
					    new BukkitRunnable() {
                            public void run() {
                                tem_loc.getBlock().setType(Material.AIR);
                            }
                        }.runTask(Main.plugin);
						
					}
				}
			}
		}
		
		p.sendMessage(ChatColor.YELLOW + "Hiding mob spawners in a " + radius + " block radius...");
		return true;
	}
	
}

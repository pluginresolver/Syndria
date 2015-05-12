package minecade.dungeonrealms.LevelMechanics.StatsGUI;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map.Entry;

import me.vilsol.menuengine.engine.BonusItem;
import me.vilsol.menuengine.engine.DynamicMenuModel;
import me.vilsol.menuengine.engine.MenuItem;
import org.bukkit.event.inventory.ClickType;
import me.vilsol.menuengine.utils.Builder;
import minecade.dungeonrealms.LevelMechanics.PlayerLevel;
import minecade.dungeonrealms.models.PlayerModel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IntellectStatsItem implements MenuItem, BonusItem<PlayerModel> {

	private PlayerModel drPlayer;
	private PlayerLevel pLevel;
	private IntellectItem item;
	private int points = 0;
	private int slot = -1;
	private DecimalFormat df = new DecimalFormat("##.###");

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		if (slot == -1) {
			for (Entry<Integer, MenuItem> entry : DynamicMenuModel.getMenu(plr).getDynamicItems().entrySet()) {
				if (((MenuItem) entry.getValue()).getItem().equals(getItem())) {
					slot = entry.getKey();
				}
			}
		}
	}

	@Override
	public ItemStack getItem() {
		int in = pLevel.getIntPoints();
		int aPoints = item.getPoints() - in; // allocated points
		boolean spent = (aPoints > 0) ? true : false;
		return new Builder(Material.TRIPWIRE_HOOK)
				.setName(
						ChatColor.RED + "Intellect Bonuses: " + in
								+ (spent ? ChatColor.GREEN + " [+" + aPoints + "]" : ""))
				.setLore(
						Arrays.asList(ChatColor.GOLD + "ENERGY REGEN: " + ChatColor.AQUA + df.format(in * 0.015) + "%"
								+ (spent ? ChatColor.GREEN + " [+" + df.format(aPoints * 0.015) + "%]" : ""),
								ChatColor.GOLD + "ELE DMG: " + ChatColor.AQUA + df.format(in * 0.05)
										+ (spent ? ChatColor.GREEN + " [+" + df.format(aPoints * 0.05) + "%]" : ""),
								ChatColor.GOLD + "CRIT CHANCE: " + ChatColor.AQUA + df.format(in * 0.025) + "%"
										+ (spent ? ChatColor.GREEN + " [+" + df.format(aPoints * 0.025) + "%]" : ""),
								ChatColor.GOLD + "STAFF DMG: " + ChatColor.AQUA + df.format(in * 0.02) + "%"
										+ (spent ? ChatColor.GREEN + " [+" + df.format(aPoints * 0.02) + "%]" : "")))
				.getItem();
	}

	@Override
	public void setBonusData(PlayerModel player) {
		drPlayer = player;
		pLevel = drPlayer.getPlayerLevel();
		points = pLevel.getIntPoints();
		pLevel.setTempFreePoints(pLevel.getFreePoints());
		for (Entry<Integer, MenuItem> entry : DynamicMenuModel.getMenu(drPlayer.getPlayer()).getDynamicItems().entrySet()) {
			if (entry.getValue() instanceof IntellectItem) {
				item = (IntellectItem) entry.getValue();
				points = item.getPoints();
			}
		}
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
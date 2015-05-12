package me.vilsol.itemgenerator.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import minecade.dungeonrealms.enums.ItemRarity;
import minecade.dungeonrealms.enums.ItemTier;

import org.bukkit.inventory.meta.ItemMeta;

public class ModifierCondition {
	
	private ItemTier tier;
	private ItemRarity rarity;
	private ModifierRange range;
	private int chance = -1;
	private List<Class<? extends ItemModifier>> cantContain;
	private ModifierCondition bonus;
	private List<Class<? extends ItemModifier>> replacement;
	private String chosenPrefix;
	private String chosenSuffix;

	public ModifierCondition(ItemTier tier, ItemRarity rarity, ModifierRange range, int chance){
		this.tier = tier;
		this.rarity = rarity;
		this.range = range;
		this.chance = chance;
		this.cantContain = new ArrayList<Class<? extends ItemModifier>>();
	}
	
	public ModifierCondition(ItemTier tier, ItemRarity rarity, ModifierRange range){
		this.tier = tier;
		this.rarity = rarity;
		this.range = range;
		this.cantContain = new ArrayList<Class<? extends ItemModifier>>();
	}
	
	public void setChosenPrefix(String prefix){
		this.chosenPrefix = prefix;
	}
	
	public void setChosenSuffix(String suffix){
		this.chosenSuffix = suffix;
	}
	
	public String getChosenPrefix(){
		return chosenPrefix;
	}
	
	public String getChosenSuffix(){
		return chosenSuffix;
	}
	
	public ModifierCondition setBonus(ModifierCondition bonus){
		this.bonus = bonus;
		return this;
	}
	
	public ModifierCondition setReplacement(Class<? extends ItemModifier> replacement){
		this.replacement = new ArrayList<Class<? extends ItemModifier>>();
		this.replacement.add(replacement);
		return this;
	}
	
	public ModifierCondition setReplacement(List<Class<? extends ItemModifier>> replacement){
		this.replacement = replacement;
		return this;
	}
	
	public ModifierCondition setCantContain(Class<? extends ItemModifier> cantContain){
		this.cantContain.add(cantContain);
		return this;
	}
	
	public ModifierCondition setCantContain(List<Class<? extends ItemModifier>> cantContain){
		this.cantContain = cantContain;
		return this;
	}
	
	public boolean doesConclude(ItemTier tier, ItemRarity rarity, ItemMeta meta){
		if(this.tier != null && this.tier != tier) return false;
		if(this.rarity != null && this.rarity != rarity) return false;
		return true;
	}
	
	public ModifierRange getRange(){
		return range;
	}
	
	public int getChance(){
		return chance;
	}
	
	public ModifierCondition getBonus(){
		return bonus;
	}
	
	public List<Class<? extends ItemModifier>> getReplacement(){
		return replacement;
	}
	
	public List<Class<? extends ItemModifier>> getCantContain() {
	    return cantContain;
	}

	public boolean canApply(Set<ModifierCondition> conditions) {
		for(ModifierCondition mc : conditions){
			if(mc.equals(this)) continue;
			
			if(mc.getChosenPrefix() != null){
				if(mc.getChosenPrefix().equals(getChosenPrefix())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkCantContain(Class<? extends ItemModifier> mod) {
	    if (this.getCantContain().contains(mod)) return false;
	    return true;
	}
	
}

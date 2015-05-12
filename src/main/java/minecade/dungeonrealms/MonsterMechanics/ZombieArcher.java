package minecade.dungeonrealms.MonsterMechanics;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.server.v1_8_R2.EntityArrow;
import net.minecraft.server.v1_8_R2.EntityHuman;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.EntityZombie;
import net.minecraft.server.v1_8_R2.IRangedEntity;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.Items;
import net.minecraft.server.v1_8_R2.PathfinderGoal;
import net.minecraft.server.v1_8_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R2.World;

import org.bukkit.craftbukkit.v1_8_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_8_R2.util.UnsafeList;

public class ZombieArcher extends EntityZombie implements IRangedEntity {
	
	private PathfinderGoalArrowAttack bp = new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private PathfinderGoalMeleeAttack bq = new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.2D, false);
	
	public ZombieArcher(World world) {
		super(world);
		clearGoalSelectors();
		this.goalSelector.a(0, new PathfinderGoalRandomStroll(this, .6F));
		this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
		//if(world != null && !world.isStatic) {
		if(world != null ) {
			this.bZ();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void clearGoalSelectors() {
		try {
			Field a = PathfinderGoalSelector.class.getDeclaredField("b");
			Field b = PathfinderGoalSelector.class.getDeclaredField("c");
			a.setAccessible(true);
			b.setAccessible(true);
			((UnsafeList) a.get(this.goalSelector)).clear();
			((UnsafeList) b.get(this.goalSelector)).clear();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setEquipment(int i, ItemStack itemstack) {
		super.setEquipment(i, itemstack);
		this.bZ();
	}
	
	public boolean bZ() {
		this.goalSelector.a((PathfinderGoal) this.bq);
		this.goalSelector.a((PathfinderGoal) this.bp);
		ItemStack itemstack = this.getEquipment(0);
		
		if(itemstack != null && itemstack.getItem() == Items.BOW) {
			this.goalSelector.a(4, this.bp);
		} else {
			this.goalSelector.a(4, this.bq);
		}
		return false;
	}
	
	@Override
	public void a(EntityLiving entityliving, float f) {
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, (float) (14 - this.world.getDifficulty().a() * 4));
		// int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, this.bd());
		// int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, this.bd());
		
		entityarrow.b((double) (f * 2.0F) + this.random.nextGaussian() * 0.25D + (double) ((float) this.world.getDifficulty().a() * 0.11F));
		
		// CraftBukkit start
		org.bukkit.event.entity.EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, this.getEquipment(0), entityarrow, 0.8F);
		if(event.isCancelled()) {
			event.getProjectile().remove();
			return;
		}
		
		if(event.getProjectile() == entityarrow.getBukkitEntity()) {
			world.addEntity(entityarrow);
		}
		// CraftBukkit end
		
		this.makeSound("random.bow", 1.0F, 1.0F / (new Random().nextFloat() * 0.4F + 0.8F));
		// this.world.addEntity(entityarrow); // CraftBukkit - moved up
	}
	
}

package com.example.mutantvillager;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.util.RandomSource;

public class MutantVillagerEntity extends TamableAnimal {
    public MutantVillagerEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.xpReward = 10;
        this.setTame(false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.MAX_HEALTH, 300.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.5F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, MutantVillagerEntity.class, false));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Vex.class, false));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Witch.class, false));
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, Pillager.class, false));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, Illusioner.class, false));
        this.targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, Ravager.class, false));
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected SoundEvent getAmbientSound() { return SoundEvents.VILLAGER_AMBIENT; }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.VILLAGER_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.VILLAGER_DEATH; }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        Entity direct = source.getDirectEntity();
        if (source.is(DamageTypes.FALL) || direct instanceof AbstractArrow || direct instanceof ThrownPotion
                || source.is(DamageTypes.TRIDENT)) return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        this.spawnAtLocation(new ItemStack(MutantVillagerMod.RAW_MEAT.get()));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.PUMPKIN_PIE);
    }

    @Override
    public net.minecraft.world.InteractionResult mobInteract(Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (this.isTame() && this.isFood(held)) {
            if (!this.level().isClientSide && this.getHealth() < this.getMaxHealth()) {
                this.heal(held.getItem().getFoodProperties(held, this).getNutrition());
                if (!player.getAbilities().instabuild) held.shrink(1);
                this.gameEvent(net.minecraft.world.level.gameevent.GameEvent.EAT);
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.isTame() && held.is(Items.PUMPKIN_PIE)) {
            if (!this.level().isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
                if (!player.getAbilities().instabuild) held.shrink(1);
            }
            return net.minecraft.world.InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.level().isClientSide) {
            openTradeMenu(player);
        }
        return net.minecraft.world.InteractionResult.sidedSuccess(false);
    }

    private void openTradeMenu(Player player) {
        SimpleContainer container = new SimpleContainer(27);
        // The original GUI displayed three pages of randomized offers. The 1.20.1 port
        // exposes the same six-item weighted pool as a compact three-row trade display.
        ItemStack[] pool = {
                new ItemStack(Items.IRON_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.DIAMOND),
                new ItemStack(Items.EMERALD), new ItemStack(Items.NETHER_STAR), new ItemStack(Items.NAME_TAG)
        };
        for (int i = 0; i < 9; i++) {
            ItemStack offer = pool[this.random.nextInt(pool.length)].copy();
            offer.setCount(1 + this.random.nextInt(3));
            container.setItem(i, offer);
        }
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, p) -> new ChestMenu(MenuType.GENERIC_9x3, id, inventory, container, 3),
                Component.translatable("entity.mutantvillager.mutant_villager")
        ));
    }

    public boolean checkSpawnObstruction(Level level) {
        return level.noCollision(this, this.getBoundingBox());
    }

    public static boolean checkMutantSpawnRules(EntityType<MutantVillagerEntity> type, ServerLevelAccessor level,
                                                  MobSpawnType reason, BlockPos pos, RandomSource random) {
        return Mob.checkMobSpawnRules(type, level, reason, pos, random);
    }

    @Override
    public AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob partner) {
        MutantVillagerEntity child = MutantVillagerMod.MUTANT_VILLAGER.get().create(level);
        if (child != null) child.copyPosition(this);
        return child;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}

package net.orca.orcasadditions.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.orca.orcasadditions.entity.goals.PygmySJumpGoal;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class PygmySEntity extends WaterAnimal {

    private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
    static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();

    public PygmySEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);


    }
    public boolean canBreatheUnderwater() {
        return true;
    }
    public int getMoistnessLevel() {
        return this.entityData.get(MOISTNESS_LEVEL);
    }

    public void setMoisntessLevel(int pMoistnessLevel) {
        this.entityData.set(MOISTNESS_LEVEL, pMoistnessLevel);
    }
    private boolean isMovingInWater() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && this.isInWaterOrBubble();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOISTNESS_LEVEL, 2400);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Moistness", this.getMoistnessLevel());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setMoisntessLevel(pCompound.getInt("Moistness"));

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new PygmySEntity.PygmySSwimWithPlayerGoal(this, 4.0D));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new PygmySJumpGoal(this, 10));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, (double) 1.2F, true));
        this.goalSelector.addGoal(8, new PygmySEntity.PlayWithItemsGoal());
        this.goalSelector.addGoal(8, new FollowBoatGoal(this));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0D, 1.0D));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Drowned.class, 8.0F, 1.0D, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Guardian.class)).setAlertOthers());
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Drowned.class)).setAlertOthers());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, (double)1.2F).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, pEntity);
            this.playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
        }

        return flag;
    }
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return 0.3F;
    }

    public int getMaxHeadXRot() {
        return 1;
    }

    public int getMaxHeadYRot() {
        return 1;
    }

    public void tick() {
        super.tick();
        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.isInWaterRainOrBubble()) {
                this.setMoisntessLevel(2400);
            } else {
                this.setMoisntessLevel(this.getMoistnessLevel() - 1);
                if (this.getMoistnessLevel() <= 0) {
                    this.hurt(this.damageSources().dryOut(), 1.0F);
                }

                if (this.onGround()) {
                    this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.setOnGround(false);
                    this.hasImpulse = true;
                }
            }

            if (this.level().isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
                Vec3 vec3 = this.getViewVector(0.0F);
                float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0D, 0.0D, 0.0D);
                    this.level().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0D, 0.0D, 0.0D);
                }
            }

        }
    }
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DOLPHIN_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.DOLPHIN_SPLASH;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    protected boolean closeToNextPos() {
        BlockPos blockpos = this.getNavigation().getTargetPos();
        return blockpos != null ? blockpos.closerToCenterThan(this.position(), 12.0D) : false;
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(pTravelVector);
        }

    }

    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }
    static class PygmySSwimWithPlayerGoal extends Goal {
        private final PygmySEntity dolphin;
        private final double speedModifier;
        @Nullable
        private Player player;

        PygmySSwimWithPlayerGoal(PygmySEntity pDolphin, double pSpeedModifier) {
            this.dolphin = pDolphin;
            this.speedModifier = pSpeedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            this.player = this.dolphin.level().getNearestPlayer(PygmySEntity.SWIM_WITH_PLAYER_TARGETING, this.dolphin);
            if (this.player == null) {
                return false;
            } else {
                return this.player.isSwimming() && this.dolphin.getTarget() != this.player;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr(this.player) < 256.0D;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.player = null;
            this.dolphin.getNavigation().stop();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.dolphin.getLookControl().setLookAt(this.player, (float)(this.dolphin.getMaxHeadYRot() + 20), (float)this.dolphin.getMaxHeadXRot());
            if (this.dolphin.distanceToSqr(this.player) < 6.25D) {
                this.dolphin.getNavigation().stop();
            } else {
                this.dolphin.getNavigation().moveTo(this.player, this.speedModifier);
            }

            if (this.player.isSwimming() && this.player.level().random.nextInt(6) == 0) {
                this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), this.dolphin);
            }

        }
    }

    class PlayWithItemsGoal extends Goal {
        private int cooldown;

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (this.cooldown > PygmySEntity.this.tickCount) {
                return false;
            } else {
                List<ItemEntity> list = PygmySEntity.this.level().getEntitiesOfClass(ItemEntity.class, PygmySEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
                return !list.isEmpty() || !PygmySEntity.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            List<ItemEntity> list = PygmySEntity.this.level().getEntitiesOfClass(ItemEntity.class, PygmySEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
            if (!list.isEmpty()) {
                PygmySEntity.this.getNavigation().moveTo(list.get(0), (double)1.2F);
                PygmySEntity.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
            }

            this.cooldown = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            ItemStack itemstack = PygmySEntity.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                this.drop(itemstack);
                PygmySEntity.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                this.cooldown = PygmySEntity.this.tickCount + PygmySEntity.this.random.nextInt(100);
            }

        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            List<ItemEntity> list = PygmySEntity.this.level().getEntitiesOfClass(ItemEntity.class, PygmySEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
            ItemStack itemstack = PygmySEntity.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                this.drop(itemstack);
                PygmySEntity.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            } else if (!list.isEmpty()) {
                PygmySEntity.this.getNavigation().moveTo(list.get(0), (double)1.2F);
            }

        }

        private void drop(ItemStack pStack) {
            if (!pStack.isEmpty()) {
                double d0 = PygmySEntity.this.getEyeY() - (double)0.3F;
                ItemEntity itementity = new ItemEntity(PygmySEntity.this.level(), PygmySEntity.this.getX(), d0, PygmySEntity.this.getZ(), pStack);
                itementity.setPickUpDelay(40);
                itementity.setThrower(PygmySEntity.this.getUUID());
                float f = 0.3F;
                float f1 = PygmySEntity.this.random.nextFloat() * ((float)Math.PI * 2F);
                float f2 = 0.02F * PygmySEntity.this.random.nextFloat();
                itementity.setDeltaMovement((double)(0.3F * -Mth.sin(PygmySEntity.this.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(PygmySEntity.this.getXRot() * ((float)Math.PI / 180F)) + Mth.cos(f1) * f2), (double)(0.3F * Mth.sin(PygmySEntity.this.getXRot() * ((float)Math.PI / 180F)) * 1.5F), (double)(0.3F * Mth.cos(PygmySEntity.this.getYRot() * ((float)Math.PI / 180F)) * Mth.cos(PygmySEntity.this.getXRot() * ((float)Math.PI / 180F)) + Mth.sin(f1) * f2));
                PygmySEntity.this.level().addFreshEntity(itementity);
            }
        }
    }

}

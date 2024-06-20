package net.orca.orcasadditions.entity.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.orca.orcasadditions.entity.custom.PygmySEntity;


public class PygmySJumpGoal extends JumpGoal {
    private static final int[] STEPS_TO_CHECK = new int[]{0, 1, 4, 5, 6, 7};
    private final PygmySEntity pygmys;
    private final int interval;
    private boolean breached;

    public PygmySJumpGoal(PygmySEntity pPygmySEntity, int pInterval) {
        this.pygmys = pPygmySEntity;
        this.interval = reducedTickDelay(pInterval);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (this.pygmys.getRandom().nextInt(this.interval) != 0) {
            return false;
        } else {
            Direction direction = this.pygmys.getMotionDirection();
            int i = direction.getStepX();
            int j = direction.getStepZ();
            BlockPos blockpos = this.pygmys.blockPosition();

            for(int k : STEPS_TO_CHECK) {
                if (!this.waterIsClear(blockpos, i, j, k) || !this.surfaceIsClear(blockpos, i, j, k)) {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean waterIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
        BlockPos blockpos = pPos.offset(pDx * pScale, 0, pDz * pScale);
        return this.pygmys.level().getFluidState(blockpos).is(FluidTags.WATER) && !this.pygmys.level().getBlockState(blockpos).blocksMotion();
    }

    private boolean surfaceIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
        return this.pygmys.level().getBlockState(pPos.offset(pDx * pScale, 1, pDz * pScale)).isAir() && this.pygmys.level().getBlockState(pPos.offset(pDx * pScale, 2, pDz * pScale)).isAir();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        double d0 = this.pygmys.getDeltaMovement().y;
        return (!(d0 * d0 < (double)0.03F) || this.pygmys.getXRot() == 0.0F || !(Math.abs(this.pygmys.getXRot()) < 10.0F) || !this.pygmys.isInWater()) && !this.pygmys.onGround();
    }

    public boolean isInterruptable() {
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        Direction direction = this.pygmys.getMotionDirection();
        this.pygmys.setDeltaMovement(this.pygmys.getDeltaMovement().add((double)direction.getStepX() * 0.6D, 0.7D, (double)direction.getStepZ() * 0.6D));
        this.pygmys.getNavigation().stop();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.pygmys.setXRot(0.0F);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        boolean flag = this.breached;
        if (!flag) {
            FluidState fluidstate = this.pygmys.level().getFluidState(this.pygmys.blockPosition());
            this.breached = fluidstate.is(FluidTags.WATER);
        }

        if (this.breached && !flag) {
            this.pygmys.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3 vec3 = this.pygmys.getDeltaMovement();
        if (vec3.y * vec3.y < (double)0.03F && this.pygmys.getXRot() != 0.0F) {
            this.pygmys.setXRot(Mth.rotLerp(0.2F, this.pygmys.getXRot(), 0.0F));
        } else if (vec3.length() > (double)1.0E-5F) {
            double d0 = vec3.horizontalDistance();
            double d1 = Math.atan2(-vec3.y, d0) * (double)(180F / (float)Math.PI);
            this.pygmys.setXRot((float)d1);
        }

    }
}


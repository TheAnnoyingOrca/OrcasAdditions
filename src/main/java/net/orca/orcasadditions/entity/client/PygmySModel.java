package net.orca.orcasadditions.entity.client;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.orca.orcasadditions.entity.custom.PygmySEntity;
import org.joml.Vector3f;

public class PygmySModel<T extends PygmySEntity> extends HierarchicalModel<T> {
	private final ModelPart head;
	private final ModelPart tail;
	private final ModelPart fluke;
	private final ModelPart flipperleft;
	private final ModelPart flipperright;

	public PygmySModel(ModelPart root) {
		this.head = root.getChild("head");
		this.tail = head.getChild("tail");
		this.fluke = tail.getChild("fluke");
		this.flipperleft = head.getChild("flipperleft");
		this.flipperright = head.getChild("flipperright");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -8.0F, -12.0F, 7.0F, 8.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-3.5F, -8.0F, -14.0F, 7.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(38, 0).addBox(-1.0F, -9.0F, 9.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition tail = head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 32).addBox(-2.0F, -2.05F, -1.0F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 12.0F));

		PartDefinition fluke = tail.addOrReplaceChild("fluke", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -0.5F, 0.0F, 12.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 8.0F));

		PartDefinition flipperleft = head.addOrReplaceChild("flipperleft", CubeListBuilder.create(), PartPose.offset(3.5F, 0.0F, -2.0F));

		PartDefinition flippercubeleft_r1 = flipperleft.addOrReplaceChild("flippercubeleft_r1", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, -0.5F, 0.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.2618F, 0.7854F));

		PartDefinition flipperright = head.addOrReplaceChild("flipperright", CubeListBuilder.create(), PartPose.offset(-3.5F, 0.0F, -2.0F));

		PartDefinition flippercuberight_r1 = flipperright.addOrReplaceChild("flippercuberight_r1", CubeListBuilder.create().texOffs(0, 9).mirror().addBox(-5.0F, -0.5F, 0.0F, 6.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.2618F, -0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public ModelPart root() {
		return head;
	}

	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pHeadYaw, float pHeadPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
		this.head.yRot = pHeadYaw * ((float) Math.PI / 180F);
		if (pEntity.isInWaterOrBubble()) {
			if (pEntity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D) {
				this.head.xRot += -0.05F - 0.05F * Mth.cos(pAgeInTicks * 0.3F);
				this.tail.xRot = -0.2F * Mth.cos(+2.0F - pAgeInTicks * 0.3F);
				this.fluke.xRot = -0.4F * Mth.cos(+4.0F - pAgeInTicks * 0.3F);

			}


		}
	}
}
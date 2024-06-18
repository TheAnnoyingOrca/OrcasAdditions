package net.orca.orcasadditions.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.orca.orcasadditions.OrcasAdditions;
import net.orca.orcasadditions.entity.custom.PygmySEntity;

public class PygmySRenderer extends MobRenderer<PygmySEntity, PygmySModel<PygmySEntity>> {
    public PygmySRenderer(EntityRendererProvider.Context pContext ) {
        super(pContext, new PygmySModel<>(pContext.bakeLayer(ModModelLayers.PYGMYS_LAYER)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(PygmySEntity pEntity) {
        return new ResourceLocation(OrcasAdditions.MOD_ID, "textures/entity/pygmys/pygmys.png");
    }


    @Override
    public void render(PygmySEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}


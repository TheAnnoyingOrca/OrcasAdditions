package net.orca.orcasadditions.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orca.orcasadditions.OrcasAdditions;
import net.orca.orcasadditions.entity.OAdditionalEntities;
import net.orca.orcasadditions.entity.client.ModModelLayers;
import net.orca.orcasadditions.entity.client.PygmySModel;
import net.orca.orcasadditions.entity.client.PygmySRenderer;

@Mod.EventBusSubscriber(modid = OrcasAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OrcasAdditionsEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.PYGMYS_LAYER, PygmySModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OAdditionalEntities.PYGMYS.get(), PygmySRenderer::new);
    }
}

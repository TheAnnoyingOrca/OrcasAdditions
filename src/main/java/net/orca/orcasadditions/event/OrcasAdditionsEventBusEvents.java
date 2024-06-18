package net.orca.orcasadditions.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.orca.orcasadditions.OrcasAdditions;
import net.orca.orcasadditions.entity.OAdditionalEntities;
import net.orca.orcasadditions.entity.custom.PygmySEntity;

@Mod.EventBusSubscriber(modid = OrcasAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrcasAdditionsEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(OAdditionalEntities.PYGMYS.get(), PygmySEntity.createMobAttributes().build());
    }
}

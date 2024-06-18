package net.orca.orcasadditions;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.orca.orcasadditions.entity.OAdditionalEntities;
import net.orca.orcasadditions.entity.client.PygmySRenderer;
import net.orca.orcasadditions.item.OAdditionalItems;
import org.slf4j.Logger;

import static net.orca.orcasadditions.entity.OAdditionalEntities.register;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OrcasAdditions.MOD_ID)
public class OrcasAdditions
{
    public static final String MOD_ID = "orcasadditions";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OrcasAdditions()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        OAdditionalItems.OADDITIONAL_ITEMS.register(modEventBus);
        OAdditionalEntities.OADDITIONAL_ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(OAdditionalEntities.PYGMYS.get(), PygmySRenderer::new);
        }
    }
}

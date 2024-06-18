package net.orca.orcasadditions.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.orca.orcasadditions.OrcasAdditions;

public class OAdditionalCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OrcasAdditions.MOD_ID);

    public static final RegistryObject<CreativeModeTab> OADDTIONSTAB = CREATIVE_MODE_TABS.register("oadditions_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(OAdditionalItems.PYGMYS_SPAWN_EGG.get()))
                    .title(Component.translatable("creativetab.oadditions_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(OAdditionalItems.PYGMYS_SPAWN_EGG.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}



package net.orca.orcasadditions.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.orca.orcasadditions.OrcasAdditions;
import net.orca.orcasadditions.entity.OAdditionalEntities;

public class OAdditionalItems {
    public static final DeferredRegister<Item> OADDITIONAL_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OrcasAdditions.MOD_ID);

    public static final RegistryObject<Item> PYGMYS_SPAWN_EGG = OADDITIONAL_ITEMS.register("pygmys_spawn_egg",
            () -> new ForgeSpawnEggItem(OAdditionalEntities.PYGMYS, 0Xd2d5d9, 0X3a4456,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        OADDITIONAL_ITEMS.register(eventBus);
    }
}

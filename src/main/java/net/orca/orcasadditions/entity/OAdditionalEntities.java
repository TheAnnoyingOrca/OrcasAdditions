package net.orca.orcasadditions.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.orca.orcasadditions.OrcasAdditions;
import net.orca.orcasadditions.entity.custom.PygmySEntity;

public class OAdditionalEntities {
    public static final DeferredRegister<EntityType<?>> OADDITIONAL_ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OrcasAdditions.MOD_ID);

    public static final RegistryObject<EntityType<PygmySEntity>> PYGMYS =
            OADDITIONAL_ENTITY_TYPES.register("pygmys", () -> EntityType.Builder.of(PygmySEntity::new, MobCategory.WATER_CREATURE).sized(1.5f, 0.8f).build("pygmys"));

    public static void register(IEventBus eventBus) {
        OADDITIONAL_ENTITY_TYPES.register(eventBus);
    }
}

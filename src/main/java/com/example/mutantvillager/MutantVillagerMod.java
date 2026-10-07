package com.example.mutantvillager;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.level.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;

@Mod(MutantVillagerMod.MODID)
public class MutantVillagerMod {
    public static final String MODID = "mutantvillager";

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<EntityType<MutantVillagerEntity>> MUTANT_VILLAGER = ENTITIES.register("mutant_villager", () ->
            EntityType.Builder.of(MutantVillagerEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 2.5f)
                    .clientTrackingRange(64)
                    .updateInterval(3)
                    .build(new ResourceLocation(MODID, "mutant_villager").toString()));

    public static final RegistryObject<Item> RAW_MEAT = ITEMS.register("mutant_meat_villager", () ->
            new Item(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3f).meat().build())));

    public static final RegistryObject<Item> COOKED_MEAT = ITEMS.register("mutabt_cooked_meat_villager", () ->
            new Item(new Item.Properties().stacksTo(64).food(new FoodProperties.Builder().nutrition(10).saturationMod(0.3f).meat().build())));

    public static final RegistryObject<Item> SPAWN_EGG = ITEMS.register("mutant_villager_spawn_egg", () ->
            new ForgeSpawnEggItem(MUTANT_VILLAGER, 0xFFCC00, 0xC44432, new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("mutant_villager_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tabmutant_villager_tab"))
                    .icon(() -> SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(SPAWN_EGG.get());
                        output.accept(RAW_MEAT.get());
                        output.accept(COOKED_MEAT.get());
                    }).build());

    public MutantVillagerMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(modBus);
        ITEMS.register(modBus);
        TABS.register(modBus);
        modBus.addListener(this::attributes);
        modBus.addListener(this::spawnPlacements);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void attributes(EntityAttributeCreationEvent event) {
        event.put(MUTANT_VILLAGER.get(), MutantVillagerEntity.createAttributes().build());
    }

    private void spawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(MUTANT_VILLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                MutantVillagerEntity::checkMutantSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public void addSpawns(BiomeLoadingEvent event) {
        if (event.getName() != null && event.getName().getNamespace().equals("minecraft") && !event.getName().getPath().equals("the_void")) {
            // The original 1.16.5 MCreator mod added the monster to a broad set of overworld biomes.
            // In 1.20.1 this keeps the same broad behavior without relying on removed 1.16 biome names.
            if (!event.getName().getPath().contains("nether") && !event.getName().getPath().contains("end")) {
                event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.Spawner(MUTANT_VILLAGER.get(), 100, 1, 1));
            }
        }
    }
}

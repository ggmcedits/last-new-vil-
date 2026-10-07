package com.example.mutantvillager.client;

import com.example.mutantvillager.MutantVillagerMod;
import com.example.mutantvillager.client.model.MutantVillagerModel;
import com.example.mutantvillager.client.renderer.MutantVillagerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MutantVillagerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MutantVillagerModel.LAYER_LOCATION, MutantVillagerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MutantVillagerMod.MUTANT_VILLAGER.get(), MutantVillagerRenderer::new);
    }
}

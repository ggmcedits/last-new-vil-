package com.example.mutantvillager.client.renderer;

import com.example.mutantvillager.MutantVillagerEntity;
import com.example.mutantvillager.client.model.MutantVillagerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MutantVillagerRenderer extends MobRenderer<MutantVillagerEntity, MutantVillagerModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("mutantvillager", "textures/mutantvillager12.png");

    public MutantVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new MutantVillagerModel(context.bakeLayer(MutantVillagerModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(MutantVillagerEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(MutantVillagerEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.0F, 1.0F, 1.0F);
    }
}

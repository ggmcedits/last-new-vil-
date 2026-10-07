package com.example.mutantvillager.client.model;

import com.example.mutantvillager.MutantVillagerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MutantVillagerModel extends HierarchicalModel<MutantVillagerEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("mutantvillager", "mutant_villager"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leg;
    private final ModelPart leg2;
    private final ModelPart arm1;
    private final ModelPart arm2;

    public MutantVillagerModel(ModelPart root) {
        this.root = root.getChild("calosc");
        this.body = this.root.getChild("body");
        this.head = body.getChild("head");
        this.leg = this.root.getChild("leg");
        this.leg2 = this.root.getChild("leg2");
        this.arm1 = this.root.getChild("arm1");
        this.arm2 = this.root.getChild("arm2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition calosc = root.addOrReplaceChild("calosc", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 6.0F));

        PartDefinition body = calosc.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-4.0F, -11.3572F, -2.234F, 9.0F, 15.0F, 6.0F)
                        .texOffs(62, 21).addBox(-3.0F, -18.0F, -4.0F, 7.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition bodyRidge = body.addOrReplaceChild("body_ridge",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -10.0F, -4.0F, 13.0F, 10.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -6.3572F, 0.766F, 0.2182F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(34, 0).addBox(-2.0F, -4.0F, -4.0F, 3.0F, 5.0F, 2.0F)
                        .texOffs(30, 18).addBox(-5.0F, -12.0F, -2.0F, 9.0F, 11.0F, 7.0F),
                PartPose.offsetAndRotation(1.0F, -16.0F, -3.0F, 0.2182F, 0.0F, 0.0F));

        calosc.addOrReplaceChild("leg",
                CubeListBuilder.create().texOffs(62, 7).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F),
                PartPose.offset( -4.0F, 7.0F, -1.0F));
        calosc.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(51, 63).addBox(0.3F, 13.1004F, -3.9426F, 3.0F, 2.0F, 8.0F)
                        .texOffs(0, 57).addBox(0.3F, 12.1004F, -0.9426F, 3.0F, 1.0F, 5.0F),
                PartPose.offset(4.0F, -3.0F, -1.0F));

        PartDefinition legR = calosc.addOrReplaceChild("legRidge",
                CubeListBuilder.create().texOffs(65, 30).addBox(0.3F, -0.8996F, -0.9426F, 3.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(-1.0F, 7.0F, -3.0F, 0.48F, 0.0F, -0.1745F));
        PartDefinition legR2 = calosc.addOrReplaceChild("legRidge2",
                CubeListBuilder.create().texOffs(59, 49).addBox(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, -0.3927F, 0.0F, -0.1745F));

        PartDefinition arm1 = calosc.addOrReplaceChild("arm1",
                CubeListBuilder.create().texOffs(46, 36).addBox(-5.5F, 10.5643F, -4.0F, 6.0F, 6.0F, 7.0F)
                        .texOffs(17, 54).addBox(-4.5F, -2.4357F, -3.0F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(-6.0F, -14.0F, -10.0F));
        PartDefinition arm1R = arm1.addOrReplaceChild("arm1_ridge",
                CubeListBuilder.create().texOffs(42, 0).addBox(-5.5F, 10.5643F, -4.0F, 6.0F, 6.0F, 7.0F)
                        .texOffs(41, 49).addBox(-4.5F, -2.4357F, -3.0F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(0.0F, 10.0F, -3.0F));

        PartDefinition arm2 = calosc.addOrReplaceChild("arm2",
                CubeListBuilder.create().texOffs(42, 0).addBox(-5.5F, 10.5643F, -4.0F, 6.0F, 6.0F, 7.0F)
                        .texOffs(41, 49).addBox(-4.5F, -2.4357F, -3.0F, 4.0F, 14.0F, 5.0F),
                PartPose.offset(4.0F, -10.0F, -3.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() { return root; }

    @Override
    public void setupAnim(MutantVillagerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD + 0.2182F;
        this.leg.xRot = -Mth.cos(limbSwing) * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing) * limbSwingAmount;
        this.arm1.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * limbSwingAmount;
        this.arm2.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, consumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

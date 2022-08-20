package net.mca.client.render;

import net.mca.client.model.VillagerEntityModelMCA;
import net.mca.client.model.ZombieVillagerEntityModelMCA;
import net.mca.client.render.layer.ClothingLayer;
import net.mca.client.render.layer.FaceLayer;
import net.mca.client.render.layer.HairLayer;
import net.mca.client.render.layer.SkinLayer;
import net.mca.entity.ZombieVillagerEntityMCA;
import net.mca.util.compat.model.Dilation;
import net.mca.util.compat.model.ModelData;
import net.mca.util.compat.model.TexturedModelData;
import net.minecraft.client.render.entity.EntityRenderDispatcher;

public class ZombieVillagerEntityMCARenderer extends VillagerLikeEntityMCARenderer<ZombieVillagerEntityMCA> {
    public ZombieVillagerEntityMCARenderer(EntityRenderDispatcher ctx) {
        super(ctx, createModel(VillagerEntityModelMCA.bodyData(Dilation.NONE)).hideWears());

        addFeature(new SkinLayer<>(this, model));
        addFeature(new FaceLayer<>(this, createModel(VillagerEntityModelMCA.bodyData(new Dilation(0.01F))).hideWears(), "zombie"));
        addFeature(new ClothingLayer<>(this, createModel(VillagerEntityModelMCA.bodyData(new Dilation(0.075F))), "zombie"));
        addFeature(new HairLayer<>(this, createModel(VillagerEntityModelMCA.hairData(new Dilation(0.1F)))));
    }

    private static VillagerEntityModelMCA<ZombieVillagerEntityMCA> createModel(ModelData data) {
        return new ZombieVillagerEntityModelMCA<>(TexturedModelData.of(data, 64, 64).createModel());
    }

    @Override
    protected boolean isShaking(ZombieVillagerEntityMCA entity) {
        return entity.isConverting() || entity.isConvertingInWater();
    }
}

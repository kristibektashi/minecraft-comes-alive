package mca.client.render;

import mca.client.model.ModelVillagerMCA;
import mca.entity.EntityVillagerMCA;
import mca.enums.EnumGender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class LayerFace extends LayerVillager {
    public LayerFace(RenderLivingBase<?> rendererIn) {
        super(rendererIn, 0.0f, 0.0f);

        // LayerViller is only designed for ModelVillagerMCA anyways
        ((ModelVillagerMCA) this.model).setVisible(false);
        ((ModelVillagerMCA) this.model).bipedHead.showModel = true;
    }

    @Override
    public void doRenderLayer(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.enableBlend();
        super.doRenderLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.disableBlend();
    }

    @Override
    String getTexture(LivingEntity entity) {
        EntityVillagerMCA villager = (EntityVillagerMCA) entity;
        EnumGender gender = EnumGender.byId(villager.get(EntityVillagerMCA.gender));
        int skin = (int) Math.min(1, Math.max(0, villager.get(EntityVillagerMCA.GENE_SKIN) * 2));
        return String.format("mca:skins/faces/%s/%d.png", gender == EnumGender.FEMALE ? "female" : "male", skin);
    }
}

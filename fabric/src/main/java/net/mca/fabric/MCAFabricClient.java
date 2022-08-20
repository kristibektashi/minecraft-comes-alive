package net.mca.fabric;

import me.shedaniel.architectury.registry.ParticleProviderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.mca.ClientProxyAbstractImpl;
import net.mca.Config;
import net.mca.MCAClient;
import net.mca.ParticleTypesMCA;
import net.mca.block.BlockEntityTypesMCA;
import net.mca.block.BlocksMCA;
import net.mca.client.particle.InteractionParticle;
import net.mca.client.render.GrimReaperRenderer;
import net.mca.client.render.TombstoneBlockEntityRenderer;
import net.mca.client.render.VillagerEntityMCARenderer;
import net.mca.client.render.ZombieVillagerEntityMCARenderer;
import net.mca.entity.EntitiesMCA;
import net.mca.fabric.client.gui.FabricMCAScreens;
import net.mca.fabric.resources.FabricColorPaletteLoader;
import net.mca.fabric.resources.FabricSupportersLoader;
import net.mca.item.BabyItem;
import net.mca.item.ItemsMCA;
import net.mca.item.SirbenBabyItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.client.render.entity.ZombieVillagerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public final class MCAFabricClient extends ClientProxyAbstractImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (Config.getInstance().useSquidwardModels) {
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.MALE_VILLAGER.get(), (dispatcher, ctx) -> new VillagerEntityRenderer(dispatcher, ctx.getResourceManager()));
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.FEMALE_VILLAGER.get(), (dispatcher, ctx) -> new VillagerEntityRenderer(dispatcher, ctx.getResourceManager()));

            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.MALE_ZOMBIE_VILLAGER.get(), (dispatcher, ctx) -> new ZombieVillagerEntityRenderer(dispatcher, ctx.getResourceManager()));
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.FEMALE_ZOMBIE_VILLAGER.get(), (dispatcher, ctx) -> new ZombieVillagerEntityRenderer(dispatcher, ctx.getResourceManager()));
        } else {
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.MALE_VILLAGER.get(), (dispatcher, ctx) -> new VillagerEntityMCARenderer(dispatcher));
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.FEMALE_VILLAGER.get(), (dispatcher, ctx) -> new VillagerEntityMCARenderer(dispatcher));

            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.MALE_ZOMBIE_VILLAGER.get(), (dispatcher, ctx) -> new ZombieVillagerEntityMCARenderer(dispatcher));
            EntityRendererRegistry.INSTANCE.register(EntitiesMCA.FEMALE_ZOMBIE_VILLAGER.get(), (dispatcher, ctx) -> new ZombieVillagerEntityMCARenderer(dispatcher));
        }

        EntityRendererRegistry.INSTANCE.register(EntitiesMCA.GRIM_REAPER.get(), (dispatcher, ctx) -> new GrimReaperRenderer(dispatcher));

        ParticleProviderRegistry.register(ParticleTypesMCA.NEG_INTERACTION.get(), InteractionParticle.Factory::new);
        ParticleProviderRegistry.register(ParticleTypesMCA.POS_INTERACTION.get(), InteractionParticle.Factory::new);

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityTypesMCA.TOMBSTONE.get(), TombstoneBlockEntityRenderer::new);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new FabricMCAScreens());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new FabricColorPaletteLoader());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new FabricSupportersLoader());

        FabricModelPredicateProviderRegistry.register(ItemsMCA.BABY_BOY.get(), new Identifier("invalidated"), (stack, world, entity) ->
                BabyItem.hasBeenInvalidated(stack) ? 1 : 0
        );
        FabricModelPredicateProviderRegistry.register(ItemsMCA.BABY_GIRL.get(), new Identifier("invalidated"), (stack, world, entity) ->
                BabyItem.hasBeenInvalidated(stack) ? 1 : 0
        );
        FabricModelPredicateProviderRegistry.register(ItemsMCA.SIRBEN_BABY_BOY.get(), new Identifier("invalidated"), (stack, world, entity) ->
                SirbenBabyItem.hasBeenInvalidated(stack) ? 1 : 0
        );
        FabricModelPredicateProviderRegistry.register(ItemsMCA.SIRBEN_BABY_GIRL.get(), new Identifier("invalidated"), (stack, world, entity) ->
                SirbenBabyItem.hasBeenInvalidated(stack) ? 1 : 0
        );

        ClientPlayConnectionEvents.JOIN.register((handler, sender, server) ->
                MCAClient.onLogin()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksMCA.INFERNAL_FLAME.get(), RenderLayer.getCutout());

        ClientTickEvents.START_CLIENT_TICK.register(MCAClient::tickClient);
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return MinecraftClient.getInstance().player;
    }
}

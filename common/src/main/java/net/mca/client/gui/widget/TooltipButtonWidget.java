package net.mca.client.gui.widget;

import net.mca.util.localization.FlowingText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class TooltipButtonWidget extends ButtonWidget {
    public TooltipButtonWidget(int x, int y, int width, int height, String message, PressAction onPress) {
        super(x, y, width, height, new TranslatableText(message), onPress, (ButtonWidget buttonWidget, MatrixStack matrixStack, int mx, int my) ->
        {
            assert MinecraftClient.getInstance().currentScreen != null;
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, FlowingText.wrap(new TranslatableText(message + ".tooltip"), 160), mx, my);
        });
    }
    public TooltipButtonWidget(int x, int y, int width, int height, TranslatableText message, PressAction onPress) {
        super(x, y, width, height, message, onPress, (ButtonWidget buttonWidget, MatrixStack matrixStack, int mx, int my) ->
        {
            assert MinecraftClient.getInstance().currentScreen != null;
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrixStack, FlowingText.wrap(new TranslatableText(message.getKey() + ".tooltip"), 160), mx, my);
        });
    }
}
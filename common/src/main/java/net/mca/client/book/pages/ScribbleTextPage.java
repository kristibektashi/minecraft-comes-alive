package net.mca.client.book.pages;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mca.client.gui.ExtendedBookScreen;
import net.mca.util.compat.RenderSystemCompat;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ScribbleTextPage extends TextPage {
    final Identifier scribble;

    public ScribbleTextPage(Identifier scribble, String name, int page) {
        super(name, page);
        this.scribble = scribble;
    }

    public ScribbleTextPage(Identifier scribble, String text) {
        super(text);
        this.scribble = scribble;
    }

    public void render(ExtendedBookScreen screen, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // scribble
        int i = (screen.width - 192) / 2;
        RenderSystem.enableBlend();
        RenderSystemCompat.setShaderTexture(0, scribble);
        DrawableHelper.drawTexture(matrices, i + 28, 32, 0, 0, 128, 128, 128, 128);
        RenderSystem.disableBlend();

        super.render(screen, matrices, mouseX, mouseY, delta);
    }
}

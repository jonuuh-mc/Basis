package io.jonuuh.basis.lib.gui.element;

import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

/**
 * A plain, un-interactable texture display element.
 *
 * @see io.jonuuh.basis.lib.gui.element.button.GuiTexturedButton
 */
public class GuiTexture extends GuiElement
{
    protected ResourceLocation texture;
    protected Color texColor;
    protected boolean smoothing;

    public GuiTexture(Builder builder)
    {
        super(builder);
        this.texture = builder.texture;
        this.texColor = builder.texColor;
        this.smoothing = builder.smoothing;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (shouldDrawBackground())
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(),
                    getCornerRadius(), 1, getBackgroundColor(), getBorderColor());
        }

        if (texture != null && !texture.getResourcePath().isEmpty())
        {
            RenderUtils.drawTexturedRect(texture, getInnerLeftBound(), getInnerTopBound(), getZLevel(),
                    getInnerWidth(), getInnerHeight(), 0, smoothing, texColor);
        }
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiTexture>
    {
        protected ResourceLocation texture = null;
        protected Color texColor = Color.WHITE;
        protected boolean smoothing = false;

        public Builder(String elementName)
        {
            super(elementName);
            // Override GuiElement's default of drawing the background
            drawBackground(false);
        }

        public Builder texture(ResourceLocation texture)
        {
            this.texture = texture;
            return self();
        }

        public Builder texColor(Color texColor)
        {
            this.texColor = texColor;
            return self();
        }

        public Builder smoothing(boolean smoothing)
        {
            this.smoothing = smoothing;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiTexture build()
        {
            return new GuiTexture(this);
        }
    }
}

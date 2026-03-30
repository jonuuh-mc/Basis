package io.jonuuh.basis.lib.gui.element.button;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

/**
 * An interactable texture display element.
 * <p>
 * This is more or less identical in display functionality to {@link io.jonuuh.basis.lib.gui.element.GuiTexture GuiTexture},
 * however this one also extends {@link GuiButton} and so can be interacted with.
 */
public class GuiTexturedButton extends GuiButton
{
    protected ResourceLocation texture;
    protected Color texColor;
    protected boolean smoothing;
    protected boolean drawBackground;

    public GuiTexturedButton(Builder builder)
    {
        super(builder);
        this.texture = builder.texture;
        this.texColor = builder.texColor;
        this.smoothing = builder.smoothing;
        this.drawBackground = builder.drawBackground;
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }

    public void setTexture(ResourceLocation texture)
    {
        this.texture = texture;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (drawBackground)
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(),
                    1, getColor(GuiColorType.BACKGROUND), getColor(GuiColorType.BORDER));
        }

        if (texture != null && !texture.getResourcePath().isEmpty())
        {
            RenderUtils.drawTexturedRect(texture, getInnerLeftBound(), getInnerTopBound(), getZLevel(),
                    getInnerWidth(), getInnerHeight(), 0, smoothing, texColor);
        }
    }

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiTexturedButton>
    {
        protected ResourceLocation texture = null;
        protected Color texColor = Color.WHITE;
        protected boolean smoothing = false;
        protected boolean drawBackground = true;

        public Builder(String elementName)
        {
            super(elementName);
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

        public Builder drawBackground(boolean drawBackground)
        {
            this.drawBackground = drawBackground;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiTexturedButton build()
        {
            return new GuiTexturedButton(this);
        }
    }
}

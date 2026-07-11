package io.jonuuh.basis.lib.gui.element.button;

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

    public GuiTexturedButton(Builder builder)
    {
        super(builder);
        this.texture = builder.texture;
        this.texColor = builder.texColor;
        this.smoothing = builder.smoothing;
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

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiTexturedButton>
    {
        protected ResourceLocation texture = null;
        protected Color texColor = Color.WHITE;
        protected boolean smoothing = false;

        public Builder(String elementName)
        {
            super(elementName);
            // Override GuiElement's default of drawing the background
            // TODO: Also it doesn't even make sense for a GuiTexturedButton to be able to
            //  draw a solid background given that this element's entire width/height is
            //  covered by the texture.
            //  When implementing design in future to draw solid
            //  vs textured mode for every element, it would make sense to make a new, more
            //  generic GuiButton that can be either solid or textured (and also optionally
            //  labeled too, maybe? combine all buttons into one)
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
        public GuiTexturedButton build()
        {
            return new GuiTexturedButton(this);
        }
    }
}

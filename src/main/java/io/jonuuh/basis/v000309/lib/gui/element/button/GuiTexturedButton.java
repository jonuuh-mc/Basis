package io.jonuuh.basis.v000309.lib.gui.element.button;

import io.jonuuh.basis.v000309.lib.gui.element.ElementUtils;
import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton
{
    private ResourceLocation texture;
    private boolean smoothing;

    public GuiTexturedButton(Builder builder)
    {
        super(builder);
        this.texture = builder.texture;
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

        RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), 1, getColor(GuiColorType.ACCENT1), getColor(GuiColorType.BORDER));

        if (texture != null)
        {
            RenderUtils.drawTexturedRect(texture, ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
                    getZLevel(), ElementUtils.getInnerWidth(this), ElementUtils.getInnerHeight(this),
                    0, smoothing, getColor(GuiColorType.BASE));
        }
    }

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiTexturedButton>
    {
        protected ResourceLocation texture = null;
        protected boolean smoothing = false;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder texture(ResourceLocation texture)
        {
            this.texture = texture;
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

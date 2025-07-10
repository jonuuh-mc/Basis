package io.jonuuh.basis.lib.gui.element.button;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiTexturedButton extends GuiButton
{
    private ResourceLocation texture;

    public GuiTexturedButton(Builder builder)
    {
        super(builder);
        this.texture = builder.texture;
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
            RenderUtils.drawTexturedRect(texture, worldXPos(), worldYPos(), getZLevel(), getWidth(), getHeight(), true, getColor(GuiColorType.BASE));
        }
    }

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiTexturedButton>
    {
        protected ResourceLocation texture = null;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder texture(ResourceLocation texture)
        {
            this.texture = texture;
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

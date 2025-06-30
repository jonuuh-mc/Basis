package io.jonuuh.core.lib.gui.element.button;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.RenderUtils;
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

        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), 5, getColor(GuiColorType.ACCENT1));

        if (texture != null)
        {
            RenderUtils.drawTexturedRect(texture, worldXPos(), worldYPos(), getZLevel(), getWidth(), getHeight(), getColor(GuiColorType.BASE));
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

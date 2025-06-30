package io.jonuuh.core.lib.gui.element.toggles;

import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiCheckbox extends GuiToggle
{
    protected static final ResourceLocation checkmarkResource = new ResourceLocation("core:textures/check.png");

    public GuiCheckbox(Builder builder)
    {
        super(builder);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

//        Color boxColor = isChecked ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT1);

        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), 5, new Color());

        if (isToggled())
        {
            RenderUtils.drawTexturedRect(checkmarkResource, worldXPos(), worldYPos(), /*0*/getZLevel(), getWidth(), getHeight(), getColor(GuiColorType.BASE));
        }
    }

    public static class Builder extends GuiToggle.AbstractBuilder<Builder, GuiCheckbox>
    {
        public Builder(String elementName)
        {
            super(elementName);
            this.width = this.height = DEFAULT_HEIGHT;
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiCheckbox build()
        {
            return new GuiCheckbox(this);
        }
    }
}

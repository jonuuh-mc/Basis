package io.jonuuh.basis.lib.gui.element.toggles;

import io.jonuuh.basis.lib.gui.element.ElementUtils;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiCheckbox extends GuiToggle
{
    protected static final ResourceLocation checkmarkResource = new ResourceLocation("basis:textures/check.png");

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
            RenderUtils.drawTexturedRect(checkmarkResource,
                    ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
                    /*0*/getZLevel(),
                    ElementUtils.getInnerWidth(this), ElementUtils.getInnerHeight(this),
                    getColor(GuiColorType.BASE));
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

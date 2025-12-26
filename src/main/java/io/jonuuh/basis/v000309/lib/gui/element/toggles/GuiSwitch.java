package io.jonuuh.basis.v000309.lib.gui.element.toggles;

import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.util.Color;
import io.jonuuh.basis.v000309.lib.util.RenderUtils;

public class GuiSwitch extends GuiToggle
{
    // TODO: make vertical option
    public GuiSwitch(Builder builder)
    {
        super(builder);
    }

    public float getPointerSize()
    {
        return getHeight() - 2F;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }

        super.onScreenDraw(mouseX, mouseY, partialTicks);

        float padding = ((getHeight() - getPointerSize()) / 2F);
        float pointerX = isToggled() ? (worldXPos() + getWidth() - getPointerSize() - padding) : worldXPos() + padding;
        Color trackColor = isToggled() ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT2);

        // Track
        RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), 1, trackColor, getColor(GuiColorType.BORDER));
        // Pointer
        RenderUtils.drawRoundedRectWithBorder(pointerX, worldYPos() + padding, getPointerSize(), getPointerSize(), getCornerRadius(), 1, getColor(GuiColorType.ACCENT1), getColor(GuiColorType.BORDER));
    }

    public static class Builder extends GuiToggle.AbstractBuilder<Builder, GuiSwitch>
    {
        public Builder(String elementName)
        {
            super(elementName);
            this.width = DEFAULT_HEIGHT * 2;
            this.height = DEFAULT_HEIGHT;
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiSwitch build()
        {
            return new GuiSwitch(this);
        }
    }
}


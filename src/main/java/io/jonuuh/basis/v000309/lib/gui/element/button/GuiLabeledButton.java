package io.jonuuh.basis.v000309.lib.gui.element.button;

import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.util.RenderUtils;

public class GuiLabeledButton extends GuiButton
{
    private String label;

    public GuiLabeledButton(Builder builder)
    {
        super(builder);
        this.label = builder.label;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));

        String buttonText = RenderUtils.trimStringToWidthWithEllipsis(label, (int) getWidth());

        mc.fontRendererObj.drawString(buttonText, worldXPos() + (getWidth() / 2) - ((float) mc.fontRendererObj.getStringWidth(buttonText) / 2),
                worldYPos() + (getHeight() / 2) - ((float) mc.fontRendererObj.FONT_HEIGHT / 2), getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiLabeledButton>
    {
        protected String label = "";

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder label(String label)
        {
            this.label = label;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiLabeledButton build()
        {
            return new GuiLabeledButton(this);
        }
    }
}

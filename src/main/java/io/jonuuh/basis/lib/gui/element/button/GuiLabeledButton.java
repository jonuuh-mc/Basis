package io.jonuuh.basis.lib.gui.element.button;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class GuiLabeledButton extends GuiButton
{
    protected final FontRenderer fontRenderer;
    protected String label;
    protected float textScale;
    protected boolean doShadow;

    public GuiLabeledButton(Builder builder)
    {
        super(builder);
        this.fontRenderer = mc.fontRendererObj;
        this.label = builder.label;
        this.textScale = builder.textScale;
        this.doShadow = builder.doShadow;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;

        // Resize width and height of the button given the new label
        float hPad = getPadding().left() + getPadding().right();
        float vPad = getPadding().top() + getPadding().bottom();

        float strW = (fontRenderer.getStringWidth(label) - 1) * textScale;
        float strH = (fontRenderer.FONT_HEIGHT - 1) * textScale;

        this.setWidth(strW + hPad);
        this.setHeight(strH + vPad);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        // Draw button background
        if (shouldDrawBackground())
        {
            RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(),
                    getCornerRadius(), 1, getBackgroundColor(), getBorderColor());
        }

        // See comment in GuiLabel regarding more or less identical functionality
        String trimmedText = RenderUtils.trimStringToWidthWithEllipsis(label, (int) (getInnerWidth() * (1 / textScale)));

        if (textScale != 1F)
        {
            GL11.glPushMatrix();

            // Scale matrix around top left corner of element's drawable area (inner bound)
            RenderUtils.scaleCurrentMatrixAroundPoint(
                    getInnerLeftBound(), getInnerTopBound(),
                    textScale, textScale
            );
        }

        fontRenderer.drawString(trimmedText,
                getInnerLeftBound(), getInnerTopBound(),
                getColor(GuiColorType.ACCENT1).toPackedARGB(), doShadow);

        if (textScale != 1F)
        {
            GL11.glPopMatrix();
        }
    }

    public static class Builder extends GuiButton.AbstractBuilder<Builder, GuiLabeledButton>
    {
        protected String label = "";
        protected float textScale = 1F;
        protected boolean doShadow = true;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder label(String label)
        {
            this.label = label;
            return self();
        }

        public Builder textScale(float textScale)
        {
            this.textScale = textScale;
            return self();
        }

        public Builder doShadow(boolean doShadow)
        {
            this.doShadow = doShadow;
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
            float hPad = padding.left() + padding.right();
            float vPad = padding.top() + padding.bottom();

            float strW = (mc.fontRendererObj.getStringWidth(label) /*- 1*/) * textScale;
            float strH = (mc.fontRendererObj.FONT_HEIGHT - 1) * textScale;

            this.width = strW + hPad;
            this.height = strH + vPad;

            return new GuiLabeledButton(this);
        }
    }
}

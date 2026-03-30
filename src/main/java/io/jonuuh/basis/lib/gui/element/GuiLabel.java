package io.jonuuh.basis.lib.gui.element;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class GuiLabel extends GuiElement
{
    protected final FontRenderer fontRenderer;
    protected String text;
    protected float textScale;
    protected boolean doShadow;

    public GuiLabel(Builder builder)
    {
        super(builder);
        this.fontRenderer = mc.fontRendererObj;
        this.text = builder.text;
        this.textScale = builder.textScale;
        this.doShadow = builder.doShadow;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;

        // Resize width and height of the label given the new text
        float hPad = getPadding().left() + getPadding().right();
        float vPad = getPadding().top() + getPadding().bottom();

        float strW = (fontRenderer.getStringWidth(text) - 1) * textScale;
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

        // A label is designed to change its width and height to exactly fit the text plus any padding around it.
        // That on its own would render trimming the text unnecessary, however in combination with a FlexBehavior
        // independently changing the size of elements, this trimming will actually happen very frequently
        //
        // To account for text scale here, apply reverse scaling to the given width that should be trimmed to.
        // Note that trying to somehow apply scaling to other param (text) would not work because text scaling
        // would also need to be applied to the "..." string used within the called function
        String trimmedText = RenderUtils.trimStringToWidthWithEllipsis(text, (int) (ElementUtils.getInnerWidth(this) * (1 / textScale)));

        if (textScale != 1F)
        {
            GL11.glPushMatrix();

            // Scale matrix around top left corner of element's drawable area (inner bound)
            RenderUtils.scaleCurrentMatrixAroundPoint(
                    ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
                    textScale, textScale
            );
        }

        fontRenderer.drawString(trimmedText,
                ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
                getColor(GuiColorType.ACCENT1).toPackedARGB(), doShadow);

        if (textScale != 1F)
        {
            GL11.glPopMatrix();
        }
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiLabel>
    {
        protected String text = "";
        protected float textScale = 1F;
        protected boolean doShadow = true;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder text(String text)
        {
            this.text = text;
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
        public GuiLabel build()
        {
            float hPad = padding.left() + padding.right();
            float vPad = padding.top() + padding.bottom();

            float strW = (mc.fontRendererObj.getStringWidth(text) - 1) * textScale;
            float strH = (mc.fontRendererObj.FONT_HEIGHT - 1) * textScale;

            this.width = strW + hPad;
            this.height = strH + vPad;

            return new GuiLabel(this);
        }
    }
}

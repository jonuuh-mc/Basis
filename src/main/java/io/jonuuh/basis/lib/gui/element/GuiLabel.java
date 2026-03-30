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

    public GuiLabel setText(String text)
    {
        this.text = text;
        return this;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        String trimmedText = RenderUtils.trimStringToWidthWithEllipsis(text, (int) ElementUtils.getInnerWidth(this));

        if (textScale != 1F)
        {
            int textWidth = fontRenderer.getStringWidth(trimmedText);
            GL11.glPushMatrix();

            RenderUtils.scaleCurrentMatrixAroundObject(
                    ElementUtils.getInnerLeftBound(this) + (textWidth / 2F),
                    ElementUtils.getInnerTopBound(this) + (fontRenderer.FONT_HEIGHT / 2F),
                    textScale,
                    textScale
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
            this.width = mc.fontRendererObj.getStringWidth(text) + padding.left() + padding.right();
            this.height = (mc.fontRendererObj.FONT_HEIGHT - 1) + padding.top() + padding.bottom();
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
            return new GuiLabel(this);
        }
    }
}

package io.jonuuh.basis.lib.gui.element;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;

public class GuiLabel extends GuiElement
{
    protected final FontRenderer fontRenderer;
    protected String text;

    public GuiLabel(Builder builder)
    {
        super(builder);
        this.fontRenderer = mc.fontRendererObj;
        this.text = builder.text;
        setHeight((mc.fontRendererObj.FONT_HEIGHT - 1) + getPadding().top() + getPadding().bottom());
        setWidth(mc.fontRendererObj.getStringWidth(text) + getPadding().left() + getPadding().right());
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

        fontRenderer.drawString(trimmedText, ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
                getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiLabel>
    {
        protected String text;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder text(String text)
        {
            this.text = text;
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

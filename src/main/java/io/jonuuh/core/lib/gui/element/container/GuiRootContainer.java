package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexJustify;
import io.jonuuh.core.lib.gui.event.lifecycle.InitGuiEvent;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;

import java.util.Map;

public class GuiRootContainer extends GuiFlexContainer
{
    /** The GuiScreen containing this GuiRootContainer, should be a 1:1 relationship */
    public final AbstractGuiScreen guiScreen;

    public GuiRootContainer(AbstractGuiScreen guiScreen, Map<GuiColorType, Color> colorMap)
    {
        super("ROOT", 0, 0, 0, 0, colorMap);
        this.guiScreen = guiScreen;
        this.setJustifyContent(FlexJustify.CENTER);
        this.setAlignItems(FlexAlign.CENTER);
    }

    public GuiRootContainer(AbstractGuiScreen guiScreen)
    {
        this(guiScreen, null);
    }

    @Override
    public void setParent(GuiContainer parent)
    {
        throw new UnsupportedOperationException("Cannot set the parent of a GuiRootContainer.");
    }

    @Override
    public void onInitGui(InitGuiEvent event)
    {
        setWidth(event.sr.getScaledWidth());
        setHeight(event.sr.getScaledHeight());
        super.onInitGui(event);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), 3, new Color("#FFFFFF", 0.2F));
    }

    public static class Builder extends GuiFlexContainer.AbstractBuilder<Builder, GuiRootContainer>
    {
        protected final AbstractGuiScreen guiScreen;

        public Builder(AbstractGuiScreen guiScreen)
        {
            super("ROOT");
            this.guiScreen = guiScreen;
            this.justify = FlexJustify.CENTER;
            this.align = FlexAlign.CENTER;
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiRootContainer build()
        {
            return new GuiRootContainer(this);
        }
    }
}

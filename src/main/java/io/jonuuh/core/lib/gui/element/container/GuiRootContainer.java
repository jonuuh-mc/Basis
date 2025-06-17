package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexJustify;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Map;

public class GuiRootContainer extends GuiFlexContainer
{
    /** The GuiScreen containing this GuiRootContainer, should be a 1:1 relationship */
    public final AbstractGuiScreen guiScreen;

    public GuiRootContainer(AbstractGuiScreen guiScreen, ScaledResolution sr, Map<GuiColorType, Color> colorMap)
    {
        super("ROOT", 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), colorMap);
        this.guiScreen = guiScreen;
        this.setJustifyContent(FlexJustify.CENTER);
        this.setAlignItems(FlexAlign.CENTER);
    }

    public GuiRootContainer(AbstractGuiScreen guiScreen, ScaledResolution sr)
    {
        this(guiScreen, sr, null);
    }

    @Override
    public void setParent(GuiContainer parent)
    {
        throw new IllegalArgumentException();
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
//        System.out.printf("%s: (%s,%s) -> %s%n", elementName, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaleFactor());
        setWidth(scaledResolution.getScaledWidth());
        setHeight(scaledResolution.getScaledHeight());

        super.onInitGui(scaledResolution);
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), 3, new Color("#FFFFFF", 0.2F));
        super.onScreenDraw(mouseX, mouseY, partialTicks);
    }
}

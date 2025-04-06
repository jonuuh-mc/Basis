package io.jonuuh.core.lib.gui.element.container;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.flex.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.FlexJustify;
import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.Map;

// TODO: rename to GuiRootContainer? window is confusing
public final class GuiWindow extends GuiFlexContainer
{
    /** The GuiScreen containing this GuiWindow, should be a 1:1 relationship */
    public final AbstractGuiScreen guiScreen;

    public GuiWindow(AbstractGuiScreen guiScreen, ScaledResolution sr, float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
    {
        super("ROOT", 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), outerRadius, innerRadius, colorMap);
        this.guiScreen = guiScreen;
        this.setJustifyContent(FlexJustify.CENTER);
        this.setAlignItems(FlexAlign.CENTER);
    }

    public GuiWindow(AbstractGuiScreen guiScreen, ScaledResolution sr, float outerRadius, float innerRadius)
    {
        this(guiScreen, sr, outerRadius, innerRadius, null);
    }

    @Override
    public void setParent(GuiContainer parent)
    {
        throw new IllegalArgumentException();
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
        int screenWidth = scaledResolution.getScaledWidth();
        int screenHeight = scaledResolution.getScaledHeight();

        System.out.printf("%s: (%s,%s) -> %s%n", elementName, screenWidth, screenHeight, scaledResolution.getScaleFactor());

        setWidth(screenWidth);
        setHeight(screenHeight);

        super.onInitGui(scaledResolution);
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), innerRadius, new Color("#FFFFFF", 0.2F), true);
    }
}

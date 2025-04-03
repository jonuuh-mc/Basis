//package io.jonuuh.core.lib.gui.element.container;
//
//import io.jonuuh.core.lib.gui.GuiColorType;
//import io.jonuuh.core.lib.gui.element.container.flex.GuiFlexContainer;
//import io.jonuuh.core.lib.util.Color;
//import io.jonuuh.core.lib.util.RenderUtils;
//import net.minecraft.client.gui.ScaledResolution;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Map;
//
//public final class GuiWindow extends GuiFlexContainer
//{
//    public GuiWindow(float outerRadius, float innerRadius, Map<GuiColorType, Color> colorMap)
//    {
//        super("ROOT", 0, 0, 100, 100, outerRadius, innerRadius, colorMap);
//    }
//
//    public GuiWindow(float outerRadius, float innerRadius)
//    {
//        super("ROOT", 0, 0, 100, 100, outerRadius, innerRadius);
//    }
//
//    @Override
//    public void setParent(GuiContainer parent)
//    {
//    }
//
//    @Override
//    public void setInheritedXPos(float inheritedXPos)
//    {
//    }
//
//    @Override
//    public void setInheritedYPos(float inheritedYPos)
//    {
//    }
//
//    @Override
//    protected void onInitGui(ScaledResolution scaledResolution)
//    {
//        int screenWidth = scaledResolution.getScaledWidth();
//        int screenHeight = scaledResolution.getScaledHeight();
//
////        int xPadding = screenWidth / 10;
////        int yPadding = screenHeight / 10;
//
//        System.out.printf("%s: (%s,%s) -> %s%n", elementName, screenWidth, screenHeight, scaledResolution.getScaleFactor());
//
////            if (scaledResolution.getScaleFactor() == 1)
////            {
////                screenWidth = screenWidth * 2;
////                screenHeight = screenHeight * 2;
////            }
//
//        setLocalXPos(0);
//        setLocalYPos(0);
//
//        setWidth(Math.min(screenWidth - getLocalXPos(), getMaxWidth()));
//        setHeight(Math.min(screenHeight - getLocalYPos(), getMaxHeight()));
//
//        super.onInitGui(scaledResolution);
//    }
//
//    @Override
//    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
//    {
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), innerRadius, new Color("#FFFFFF", 0.5F), true);
//    }
//}

//package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;
//
//import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
//import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
//import io.jonuuh.core.lib.util.RenderUtils;
//import net.minecraft.client.Minecraft;
//import org.lwjgl.opengl.GL11;
//
//public class GuiVerticalSlider extends GuiSlider
//{
//    public GuiVerticalSlider(GuiContainer parent, int xPos, int yPos, int width, int height, double min, double max, double[] startValues)
//    {
//        super(parent, xPos, yPos, width, height, min, max, startValues);
//    }
//
//    public GuiVerticalSlider(GuiContainer parent, int xPos, int yPos, int width, int height, double min, double max, double startValue)
//    {
//        super(parent, xPos, yPos, width, height, min, max, startValue);
//    }
//
//    public GuiVerticalSlider(GuiContainer parent, int xPos, int yPos, double min, double max, double[] startValues)
//    {
//        super(parent, xPos, yPos, min, max, startValues);
//    }
//
//    public GuiVerticalSlider(GuiContainer parent, int xPos, int yPos, double min, double max, double startValue)
//    {
//        super(parent, xPos, yPos, min, max, startValue);
//    }
//
////    @Override
////    protected void drawSlider()
////    {
////        float trackWidth = (width / 3F);
////
////        // Draw track(s)
////        for (int i = 0; i < normalPointerValues.length; i++)
////        {
////            float currPointerCenter = getPointerCenter(i);
////
////            float trackY = i != 0 ? getPointerCenter(i - 1) : yPos;
////            float trackHeight = i != 0 ? currPointerCenter - getPointerCenter(i - 1) : currPointerCenter - yPos;
////
////            // Left side track for each pointer
//////            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, trackX, yPos + trackHeight, trackWidth, trackHeight, parent.getOuterRadius(), colors.get(i), true);
////            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, trackY, trackWidth, trackHeight, parent.getOuterRadius(), colors.get(i).setA(0.8F), true);
////
////            if (i == normalPointerValues.length - 1)
////            {
////                // Right side track for last pointer
////                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, currPointerCenter, trackWidth, height - (currPointerCenter - yPos), parent.getOuterRadius(), parent.getColorMap().get("ACCENT").setA(0.8F), true);
////            }
////        }
////
////        // Draw pointer(s)
////        for (int i = 0; i < normalPointerValues.length; i++)
////        {
////            if (i == lastHeldPointer && mouseDown)
////            {
////                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos - 2, getPointerCenter(i) - (pointerSize / 2F) - 2, pointerSize + 4, pointerSize + 4, parent.getInnerRadius(), colors.get(i).setA(0.8F), true);
////                continue;
////            }
////            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, getPointerCenter(i) - (pointerSize / 2F), pointerSize, pointerSize, parent.getInnerRadius(), colors.get(i).setA(0.8F), true);
////        }
////    }
////
////    @Override
////    protected void claimTooltipForPointer(int pointerIndex)
////    {
////        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));
////
////        GuiTooltip.claim(this);
////
////        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
////        GuiTooltip.getInstance().posX = xPos - 7 - strWidth;
////        GuiTooltip.getInstance().posY = getPointerCenter(pointerIndex) /*- (strWidth / 2F)*/;
////        GuiTooltip.getInstance().color = colors.get(pointerIndex);
////    }
//}

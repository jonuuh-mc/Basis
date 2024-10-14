//package io.jonuuh.core.module.config.gui.elements.interactable.sliders;
//
//import io.jonuuh.core.module.config.gui.ISettingsGui;
//
//import java.util.Collections;
//
//public class GuiSingleSlider extends GuiSlider
//{
//    public GuiSingleSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, double min, double max, double startValue)
//    {
//        super(parent, xPos, yPos, width, height, min, max, Collections.singletonList(startValue));
//    }
//
//    public GuiSingleSlider(ISettingsGui parent, int xPos, int yPos, double min, double max, double startValue)
//    {
//        super(parent, xPos, yPos, min, max, Collections.singletonList(startValue));
//    }
//
////    public double getValue()
////    {
////        return denormalize(getNormalizedValue());
////    }
////
////    public void setValue(double value)
////    {
////        setNormalizedValue(normalize(value));
////    }
////
////    public double getNormalizedValue()
////    {
////        return normalPointerValues[0];
////    }
////
////    public void setNormalizedValue(double normalValue)
////    {
////        normalPointerValues[0] = clamp(normalValue);
////    }
//
////    @Override
////    public boolean onScreenDraw(Minecraft mc, int mouseX, int mouseY)
////    {
////        boolean wasDrawn = super.onScreenDraw(mc, mouseX, mouseY);
////
////        if (wasDrawn)
////        {
////            drawSlider();
////
////            if (mouseDown)
////            {
////                setNormalizedValue(getSliderValueAtMouseX(mouseX));
////
////                claimTooltip();
////
////                sendChangeToParent();
////            }
////        }
////
////        return wasDrawn;
////    }
//
////    @Override
////    public void onMousePress(int mouseX, int mouseY)
////    {
////        mouseDown = true;
////        lastHeldPointer = getClosestPointerToMouse(mouseX);
////    }
//
////    @Override
////    public void onMouseRelease(int mouseX, int mouseY)
////    {
////        mouseDown = false;
////    }
//
////    protected void drawSlider()
////    {
////        float trackHeight = (height / 3F);
////        float pointerXCenter = getPointerXCenter(0);
////
////        // Left side track
////        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos + trackHeight, pointerXCenter - xPos, trackHeight, parent.getOuterRadius(), colorMap.get("BASE"), true);
////
////        // Right side track
////        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, pointerXCenter, yPos + trackHeight, width - (pointerXCenter - xPos), trackHeight, parent.getOuterRadius(), colorMap.get("ACCENT"), true);
////
////        // Draw pointer
////        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, pointerXCenter - (pointerSize / 2F), yPos, pointerSize, pointerSize, parent.getInnerRadius(), colorMap.get("BASE"), true);
////    }
//
////    public float getPointerXCenter()
////    {
////        float x = (float) (xPos + (getNormalizedValue() * width));
////        GuiUtils.drawRectangle(GL11.GL_LINE_LOOP, x, yPos, 3, 3, new Color());
////
////        // prevents sliders from going half off the start/end of the track
////        x = (float) clamp(x, xPos + (pointerSize / 2D), xPos + width - (pointerSize / 2D));
////
////        return x;
////    }
//
////    public int getClosestPointerToMouse(int mouseX)
////    {
////        return 0;
////    }
//
////    // mouse position on the slider
////    protected double getSliderValueAtMouseX(int mouseX)
////    {
////        return (mouseX - xPos) / (double) width;
////    }
////
////    protected double normalize(double value)
////    {
////        return (value - min) / (max - min);
////    }
////
////    protected double denormalize(double normalizedValue)
////    {
////        return normalizedValue * (max - min) + min;
////    }
//
////    protected double clamp(double value)
////    {
////        // Clamp value (0, 1)
////        return clamp(value, 0.0, 1.0);
////    }
//
////    protected double clamp(double value, double min, double max)
////    {
////        // Clamp value (min, max)
////        return Math.min((Math.max(value, min)), max);
////    }
//
////    protected void claimTooltip()
////    {
////        this.tooltipStr = decimalFormat.format(getValue());
////
////        GuiTooltip.claim(this);
////
////        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
////        GuiTooltip.getInstance().posX = getPointerXCenter() - (strWidth / 2F);
////        GuiTooltip.getInstance().color = colorMap.get("BASE");
////    }
////
////    public void claimHoverTooltip(int mouseX, int mouseY)
////    {
////        claimTooltip();
////    }
//}

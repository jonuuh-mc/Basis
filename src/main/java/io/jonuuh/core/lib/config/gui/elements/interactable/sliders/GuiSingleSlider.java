package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiSettingElement;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiSingleSlider extends GuiSettingElement
{
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected double normalPointerValue;

    protected DecimalFormat decimalFormat;
    protected int pointerSize;

    public GuiSingleSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, double startValue, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;
        this.pointerSize = isVertical ? width : height;

        this.decimalFormat = new DecimalFormat("#.###");

        setValue(startValue);
    }

    public GuiSingleSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, double startValue)
    {
        this(parent, elementName, xPos, yPos, 200, 16, min, max, startValue, false);
    }

    public double getValue()
    {
        return MathUtils.denormalize(getNormalizedValue(), min, max);
    }

    public void setValue(double value)
    {
        setNormalizedValue(MathUtils.normalize(value, min, max));
    }

    public double getNormalizedValue()
    {
        return normalPointerValue;
    }

    public void setNormalizedValue(double normalValue)
    {
        normalPointerValue = MathUtils.clamp(normalValue, 0, 1);
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return isVertical
                ? (mouseY - yPos) / (double) height
                : (mouseX - xPos) / (double) width;
    }

    // x/y screen position of the center of a pointer
    protected float getPointerScreenPos()
    {
//        if (isVertical)
//        {
//            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, center, 3, 3, new Color("#00ff00"));
//        }
//        else
//        {
//            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, center, yPos, 3, 3, new Color("#00ff00"));
//        }
        return isVertical
                ? (float) (yPos + (getNormalizedValue() * height))
                : (float) (xPos + (getNormalizedValue() * width));
    }

    @Override
    protected void updateSetting()
    {

    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        if (isVertical)
        {
//            drawVerticalSlider();
        }
        else
        {
            drawHorizontalSlider();
        }

        if (mouseDown)
        {
            setNormalizedValue(getSliderValueAtMousePos(mouseX, mouseY));

            // TODO: don't check for pre-claim? assumes no slider overlap (drag two sliders once)
//            claimTooltipForPointer(lastHeldPointer);

            sendChangeToSetting();
        }
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (height / 3F);
        float trackY = yPos + trackHeight;
        float pointerScreenPos = getPointerScreenPos();

        // left
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, trackY, (pointerScreenPos - xPos), trackHeight, parent.getOuterRadius(), parent.getBaseColor(), true);
        // right
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, pointerScreenPos, trackY, width - (pointerScreenPos - xPos), trackHeight, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);

        drawPointer();
    }

//    protected void drawVerticalSlider()
//    {
//        float trackWidth = (width / 3F);
//
//        // Draw track(s)
//        for (int i = 0; i < normalPointerValues.length; i++)
//        {
//            float currPointerCenter = getPointerScreenPos(i);
//
//            // Right side track for last pointer
//            if (i == normalPointerValues.length - 1)
//            {
//                float y = currPointerCenter;
//                float h = height - (currPointerCenter - yPos);
//
//                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, y, trackWidth, h, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
//            }
//
//            // Left side track for all but first pointer
//            float y = (i == 0) ? yPos : getPointerScreenPos(i - 1);
//            float h = (i == 0) ? (currPointerCenter - yPos) : (currPointerCenter - getPointerScreenPos(i - 1));
//
//            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + trackWidth, y, trackWidth, h, parent.getOuterRadius(), colors.get(i), true);
//        }
//
//        drawPointer(true);
//        drawPointer(false);
//    }

    protected void drawPointer()
    {
        float x = isVertical ? xPos : getPointerScreenPos() - (pointerSize / 2F);
        float y = isVertical ? getPointerScreenPos() - (pointerSize / 2F) : yPos;

        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, y, pointerSize, pointerSize, parent.getInnerRadius(), parent.getBaseColor().copy().setA(0.5F), true);
    }

//    protected void claimTooltipForPointer(boolean isLeftPointer)
//    {
//        this.tooltipStr = decimalFormat.format(getValue(isLeftPointer));
//        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
//
//        if (isVertical)
//        {
//            this.tooltip.posX = xPos - 7 - strWidth;
//            this.tooltip.posY = getPointerScreenPos(isLeftPointer) /*- (strWidth / 2F)*/;
//        }
//        else
//        {
//            this.tooltip.posX = getPointerScreenPos(isLeftPointer) - (strWidth / 2F);
//        }
//
////        GuiTooltip.getInstance().color = colors.get(pointerIndex);
//
////        this.tooltipStr = decimalFormat.format(getValue(pointerIndex));
////        GuiTooltip.claim(this);
////
////        int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tooltipStr) - 1;
////
////        if (isVertical)
////        {
////            GuiTooltip.getInstance().posX = xPos - 7 - strWidth;
////            GuiTooltip.getInstance().posY = getPointerScreenPos(pointerIndex) /*- (strWidth / 2F)*/;
////        }
////        else
////        {
////            GuiTooltip.getInstance().posX = getPointerScreenPos(pointerIndex) - (strWidth / 2F);
////        }
////
////        GuiTooltip.getInstance().color = colors.get(pointerIndex);
//    }

//    @Override
//    public void claimHoverTooltip(int mouseX, int mouseY)
//    {
//        claimTooltipForPointer(getClosestPointerToMouse(mouseX, mouseY));
//    }
}

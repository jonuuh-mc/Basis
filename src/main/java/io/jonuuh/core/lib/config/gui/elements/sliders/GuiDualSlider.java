package io.jonuuh.core.lib.config.gui.elements.sliders;

import io.jonuuh.core.lib.config.gui.GuiColorType;
import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiSettingElement;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiDualSlider extends GuiSettingElement
{
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected double normalPointerValueLeft;
    protected double normalPointerValueRight;

    protected DecimalFormat decimalFormat;
    protected boolean isLastHeldPointerLeft;
    protected float pointerSize;

    public GuiDualSlider(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, double startValueLeft, double startValueRight, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;
        this.pointerSize = isVertical ? width : height;

        this.decimalFormat = new DecimalFormat("#.###");

        setValues(startValueLeft, startValueRight);
    }

    public GuiDualSlider(GuiContainer parent, String elementName, int xPos, int yPos, double min, double max, double startValueLeft, double startValueRight)
    {
        this(parent, elementName, xPos, yPos, 200, 16, min, max, startValueLeft, startValueRight, false);
    }

    public double getValue(boolean isLeftPointer)
    {
        return MathUtils.denormalize(getNormalizedValue(isLeftPointer), min, max);
    }

    public void setValue(boolean isLeftPointer, double value)
    {
        setNormalizedValue(isLeftPointer, MathUtils.normalize(value, min, max));
    }

    public double[] getValues()
    {
        return new double[]{getValue(true), getValue(false)};
    }

    public void setValues(double startValueLeft, double startValueRight)
    {
        startValueLeft = Math.min(startValueLeft, startValueRight);

        normalPointerValueLeft = MathUtils.clamp(MathUtils.normalize(startValueLeft, min, max));
        normalPointerValueRight = MathUtils.clamp(MathUtils.normalize(startValueRight, min, max));
    }

//    public void setValues(double[] values)
//    {
//        normalPointerValueLeft = MathUtils.clamp(MathUtils.normalize(values[0], min, max));
//        normalPointerValueRight = MathUtils.clamp(MathUtils.normalize(values[1], min, max));
//    }

    public double getNormalizedValue(boolean isLeftPointer)
    {
        return isLeftPointer ? normalPointerValueLeft : normalPointerValueRight;
    }

    public void setNormalizedValue(boolean isLeftPointer, double normalValue)
    {
        if (isLeftPointer)
        {
            normalPointerValueLeft = clampBetweenAdjacents(true, normalValue);
        }
        else
        {
            normalPointerValueRight = clampBetweenAdjacents(false, normalValue);
        }
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    // what prevents sliders from passing each other
    protected double clampBetweenAdjacents(boolean isLeftPointer, double normalValue)
    {
        double minValue = !isLeftPointer ? (normalPointerValueLeft) : 0;
        double maxValue = isLeftPointer ? (normalPointerValueRight) : 1;

        return MathUtils.clamp(normalValue, minValue, maxValue);
    }

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return isVertical
                ? (mouseY - yPos) / (double) height
                : (mouseX - xPos) / (double) width;
    }

    // x/y screen position of the center of a pointer
    protected float getPointerScreenPos(boolean isLeftPointer)
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
                ? (float) (yPos + (getNormalizedValue(isLeftPointer) * height))
                : (float) (xPos + (getNormalizedValue(isLeftPointer) * width));
    }

    @Override
    protected void updateSetting()
    {
    }

    @Override
    protected void onInitGui(int guiScreenWidth, int guiScreenHeight)
    {
        pointerSize = isVertical ? width : height;
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        isLastHeldPointerLeft = getClosestPointerToMouse(mouseX, mouseY);
    }

    @Override
    public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        // TODO: move stuff from onScreenDraw here
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
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
//                System.out.println(Arrays.toString(normalPointerValues));
            setNormalizedValue(isLastHeldPointerLeft, getSliderValueAtMousePos(mouseX, mouseY));

            // TODO: don't check for pre-claim? assumes no slider overlap (drag two sliders once)
//            claimTooltipForPointer(lastHeldPointer);

            trySendChangeToSetting();
        }
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (height / 3F);

        float leftPointerScreenPos = getPointerScreenPos(true);
        float rightPointerScreenPos = getPointerScreenPos(false);

        // Left side track for all but first pointer
//        float x = /*(i == 0) ?*/ xPos /*: getPointerScreenPos(i - 1)*/;
//        float w = /*(i == 0) ? */ (leftPointerScreenPos - xPos) /*: (currPointerCenter - getPointerScreenPos(i - 1))*/;

        float pOffset = pointerSize / 2F;

        // far left
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, (yPos + trackHeight), (leftPointerScreenPos - xPos) /*- pOffset*/, trackHeight, parent.getOuterRadius(), getColor(GuiColorType.ACCENT1), true);

        // middle
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, leftPointerScreenPos /*- pOffset*/, (yPos + trackHeight), (rightPointerScreenPos - leftPointerScreenPos) /*+ (pOffset * 2)*/, trackHeight, parent.getOuterRadius(), getColor(GuiColorType.BASE), true);

        // far right
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, rightPointerScreenPos /*+ pOffset*/, (yPos + trackHeight), width - (rightPointerScreenPos - xPos) /*- pOffset*/, trackHeight, parent.getOuterRadius(), getColor(GuiColorType.ACCENT1), true);


//        // Draw track(s)
//        for (int i = 0; i < normalPointerValues.length; i++)
//        {
//            float currPointerCenter = getPointerScreenPos(i);
//
//            // Right side track for last pointer
//            if (i == normalPointerValues.length - 1)
//            {
//                float x = currPointerCenter;
//                float w = width - (currPointerCenter - xPos);
//
//                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, yPos + trackHeight, w, trackHeight, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
//            }
//
//            // Left side track for all but first pointer
//            float x = (i == 0) ? xPos : getPointerScreenPos(i - 1);
//            float w = (i == 0) ? (currPointerCenter - xPos) : (currPointerCenter - getPointerScreenPos(i - 1));
//
//            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, yPos + trackHeight, w, trackHeight, parent.getOuterRadius(), parent.getBaseColor(), true);
//        }

        drawPointer(true);
        drawPointer(false);
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

    protected void drawPointer(boolean isLeftPointer)
    {
        float x = isVertical ? xPos : getPointerScreenPos(isLeftPointer) - (pointerSize / 2F);
        float y = isVertical ? getPointerScreenPos(isLeftPointer) - (pointerSize / 2F) : yPos;

        if (isLeftPointer)
        {
            RenderUtils.drawTriangle(GL11.GL_POLYGON, x /*- (pointerSize / 2F)*/, y, pointerSize, pointerSize, getColor(GuiColorType.BASE).copy().setA(0.5F), -90);
        }
        else
        {
            RenderUtils.drawTriangle(GL11.GL_POLYGON, x /*+ (pointerSize / 2F)*/, y, pointerSize, pointerSize, getColor(GuiColorType.BASE).copy().setA(0.5F), 90);
        }
    }

    // return true = isLeftPointer
    protected boolean getClosestPointerToMouse(int mouseX, int mouseY)
    {
        double mousePosSliderVal = getSliderValueAtMousePos(mouseX, mouseY);

        double leftDistance = Math.abs(normalPointerValueLeft - mousePosSliderVal);
        double rightDistance = Math.abs(normalPointerValueRight - mousePosSliderVal);

        if (leftDistance != rightDistance)
        {
            return rightDistance > leftDistance;
        }

        return mousePosSliderVal < normalPointerValueLeft;
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

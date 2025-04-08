package io.jonuuh.core.lib.gui.element.sliders;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.Dimensions;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;

public class GuiSingleSlider extends GuiElement
{
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected final boolean isInteger;
    protected double normalPointerValue;

    protected DecimalFormat decimalFormat;
    protected int isMovingTimer;
    protected float pointerSize;

    public GuiSingleSlider(String elementName, float xPos, float yPos, float width, float height, double min, double max, double startValue, boolean isVertical, boolean isInteger)
    {
        super(elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;
        this.isInteger = isInteger;

        this.decimalFormat = new DecimalFormat("#.###");

        setValue(startValue);
    }

    public GuiSingleSlider(String elementName, float xPos, float yPos, double min, double max, double startValue)
    {
        // TODO: ???
        this(elementName, xPos, yPos, new Dimensions().width, new Dimensions().height, min, max, startValue, false, false);
    }

    public DecimalFormat getDecimalFormat()
    {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat)
    {
        this.decimalFormat = decimalFormat;
    }

    public double getValue()
    {
        return MathUtils.denormalize(getNormalizedValue(), min, max);
    }

    public int getValueInt()
    {
        return Math.round((float) getValue());
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
        double value = MathUtils.clamp(normalValue, 0, 1);
        // If an integer slider, round proportional to the max value
        normalPointerValue = isInteger ? (Math.round(value * max) / max) : value;
    }

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return isVertical
                ? (mouseY - worldYPos()) / getHeight()
                : (mouseX - worldXPos()) / getWidth();
    }

    // x/y screen position of the center of a pointer
    protected float getPointerScreenPos()
    {
        return isVertical
                ? (float) (worldYPos() + (getNormalizedValue() * getHeight()))
                : (float) (worldXPos() + (getNormalizedValue() * getWidth()));
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
        pointerSize = isVertical ? getWidth() : getHeight();
    }

    @Override
    protected void onScreenTick()
    {
        if (isMovingTimer > 0)
        {
            isMovingTimer--;
        }
    }

    @Override
    protected void onKeyTyped(char typedChar, int keyCode)
    {
        double moveAmt = MathUtils.normalize(1, min, max);

        if ((isVertical && (keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_W))
                || (!isVertical && (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_A)))
        {
            isMovingTimer = 13;
            setNormalizedValue(getNormalizedValue() - moveAmt);
        }
        else if ((isVertical && (keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_S))
                || (!isVertical && (keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_D)))
        {
            isMovingTimer = 13;
            setNormalizedValue(getNormalizedValue() + moveAmt);
        }
    }

    @Override
    protected void onMouseScroll(int wheelDelta)
    {
        isMovingTimer = 10;
        setNormalizedValue(getNormalizedValue() + MathUtils.normalize(wheelDelta, min, max));
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (isVertical)
        {
            drawVerticalSlider();
        }
        else
        {
            drawHorizontalSlider();
        }

        if (hovered || mouseDown)
        {
            isMovingTimer = 5;
        }

        // can't really be in onMouseDrag (seems to look laggy; doesn't update fast enough?)
        if (mouseDown)
        {
            setNormalizedValue(getSliderValueAtMousePos(mouseX, mouseY));
        }
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (getHeight() / 3F);
        float trackY = worldYPos() + trackHeight;
        float pointerScreenPos = getPointerScreenPos();

        // left
        RenderUtils.drawRoundedRect(worldXPos(), trackY, (pointerScreenPos - worldXPos()), trackHeight, getCornerRadius(), getColor(GuiColorType.BASE));
        // right
        RenderUtils.drawRoundedRect(pointerScreenPos, trackY, getWidth() - (pointerScreenPos - worldXPos()), trackHeight, getCornerRadius(), getColor(GuiColorType.ACCENT1));

        drawPointer();
    }

    protected void drawVerticalSlider()
    {
        float trackWidth = (getWidth() / 3F);
        float trackX = worldXPos() + trackWidth;
        float pointerScreenPos = getPointerScreenPos();

        // left
        RenderUtils.drawRoundedRect(trackX, worldYPos(), trackWidth, (pointerScreenPos - worldYPos()), getCornerRadius(), getColor(GuiColorType.BASE));
        // right
        RenderUtils.drawRoundedRect(trackX, pointerScreenPos, trackWidth, getHeight() - (pointerScreenPos - worldYPos()), getCornerRadius(), getColor(GuiColorType.ACCENT1));

        drawPointer();
    }

    protected void drawPointer()
    {
        float offset = pointerSize / 4;
        float size = isMovingTimer > 0 ? pointerSize + offset : pointerSize;

        float x = isVertical ? (isMovingTimer > 0 ? worldXPos() - (offset / 2) : worldXPos()) : getPointerScreenPos() - (size / 2);
        float y = isVertical ? getPointerScreenPos() - (size / 2) : (isMovingTimer > 0 ? worldYPos() - (offset / 2) : worldYPos());

        RenderUtils.drawRoundedRect(x, y, size, size, getCornerRadius(), getColor(GuiColorType.BASE));
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

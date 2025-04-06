package io.jonuuh.core.lib.gui.element.sliders;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.Dimensions;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiSingleSlider extends GuiElement
{
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected double normalPointerValue;

    protected DecimalFormat decimalFormat;
    protected int isMovingTimer;
    protected float pointerSize;

    public GuiSingleSlider(String elementName, float xPos, float yPos, float width, float height, double min, double max, double startValue, boolean isVertical)
    {
        super(elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;

        this.decimalFormat = new DecimalFormat("#.###");

        setValue(startValue);
    }

    public GuiSingleSlider(String elementName, float xPos, float yPos, double min, double max, double startValue)
    {
        // TODO: ???
        this(elementName, xPos, yPos, new Dimensions().width, new Dimensions().height, min, max, startValue, false);
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
                ? (mouseY - worldYPos()) / (double) getHeight()
                : (mouseX - worldXPos()) / (double) getWidth();
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
//            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, (float) (xPos + (getNormalizedValue() * getWidth())), yPos, 3, 3, new Color("#00ff00"));
//        }
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
        if (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_A)
        {
            isMovingTimer = 13;
            move(getNormalizedValue() - 0.005F);
        }
        else if (keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_D)
        {
            isMovingTimer = 13;
            move(getNormalizedValue() + 0.005F);
        }
    }

    @Override
    protected void onMouseScroll(int wheelDelta)
    {
        isMovingTimer = 10;
        move(getNormalizedValue() + (wheelDelta / 100F));
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

        if (hovered || mouseDown)
        {
            isMovingTimer = 5;
        }

        // can't really be in onMouseDrag (seems to look laggy; doesn't update fast enough?)
        if (mouseDown)
        {
            move(getSliderValueAtMousePos(mouseX, mouseY));
        }
    }

    protected void move(double newNormalValue)
    {
//        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("core:tick"), 1.0F));
        setNormalizedValue(newNormalValue);
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (getHeight() / 3F);
        float trackY = worldYPos() + trackHeight;
        float pointerScreenPos = getPointerScreenPos();

        // left
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), trackY, (pointerScreenPos - worldXPos()), trackHeight, parent.getOuterRadius(), getColor(GuiColorType.BASE), true);
        // right
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, pointerScreenPos, trackY, getWidth() - (pointerScreenPos - worldXPos()), trackHeight, parent.getOuterRadius(), getColor(GuiColorType.ACCENT1), true);

        drawPointer();
    }

//    protected void drawVerticalSlider()
//    {
//        float trackWidth = (getWidth() / 3F);
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
        float offset = pointerSize / 4;
        float size = isMovingTimer > 0 ? pointerSize + offset : pointerSize;

        float x = isVertical ? worldXPos() : getPointerScreenPos() - (size / 2);
        float y = isVertical ? getPointerScreenPos() - (size / 2) : (isMovingTimer > 0 ? worldYPos() - (offset / 2) : worldYPos());

        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, y, size, size, parent.getInnerRadius(), getColor(GuiColorType.BASE), true);
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

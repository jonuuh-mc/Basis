package io.jonuuh.core.lib.gui.element.sliders;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiDualSlider extends GuiElement
{
    protected static final ResourceLocation pointerResource = new ResourceLocation("core:textures/pointer.png");
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected double normalPointerValueLeft;
    protected double normalPointerValueRight;

    protected DecimalFormat decimalFormat;
    protected boolean isLastHeldPointerLeft;
    protected float pointerSize;

    public GuiDualSlider(String elementName, float xPos, float yPos, float width, float height, double min, double max, double startValueLeft, double startValueRight, boolean isVertical)
    {
        super(elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;

        this.isVertical = isVertical;

        this.decimalFormat = new DecimalFormat("#.###");

        setValues(startValueLeft, startValueRight);
    }

    public GuiDualSlider(String elementName, float xPos, float yPos, double min, double max, double startValueLeft, double startValueRight)
    {
        this(elementName, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, min, max, startValueLeft, startValueRight, false);
    }

    public double getValue(boolean isLeftPointer)
    {
        return MathUtils.denormalize(getNormalizedValue(isLeftPointer), min, max);
    }

    public void setValue(boolean isLeftPointer, double value)
    {
        setNormalizedValue(isLeftPointer, MathUtils.clamp(MathUtils.normalize(value, min, max)));
    }

    public double[] getValues()
    {
        return new double[]{getValue(true), getValue(false)};
    }

    public void setValues(double startValueLeft, double startValueRight)
    {
        startValueLeft = Math.min(startValueLeft, startValueRight);

        setValue(true, startValueLeft);
        setValue(true, startValueRight);
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
        if (handlePreCustomEvent())
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
        handlePostCustomEvent();
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
                ? (mouseY - worldYPos()) / getHeight()
                : (mouseX - worldXPos()) / getWidth();
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
                ? (float) (worldYPos() + (getNormalizedValue(isLeftPointer) * getHeight()))
                : (float) (worldXPos() + (getNormalizedValue(isLeftPointer) * getWidth()));
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
        pointerSize = isVertical ? getWidth() : getHeight();
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
        }
    }

    protected void drawHorizontalSlider()
    {
        float trackHeight = (getHeight() / 3F);

        float leftPointerScreenPos = getPointerScreenPos(true);
        float rightPointerScreenPos = getPointerScreenPos(false);

        // Left side track for all but first pointer
//        float x = /*(i == 0) ?*/ xPos /*: getPointerScreenPos(i - 1)*/;
//        float w = /*(i == 0) ? */ (leftPointerScreenPos - xPos) /*: (currPointerCenter - getPointerScreenPos(i - 1))*/;

//        float pOffset = pointerSize / 2F;

        // far left
        RenderUtils.drawRoundedRect(worldXPos(), (worldYPos() + trackHeight), (leftPointerScreenPos - worldXPos()) /*- pOffset*/, trackHeight, getCornerRadius(), getColor(GuiColorType.ACCENT1));

        // middle
        RenderUtils.drawRoundedRect(leftPointerScreenPos /*- pOffset*/, (worldYPos() + trackHeight), (rightPointerScreenPos - leftPointerScreenPos) /*+ (pOffset * 2)*/, trackHeight, getCornerRadius(), getColor(GuiColorType.BASE));

        // far right
        RenderUtils.drawRoundedRect(rightPointerScreenPos /*+ pOffset*/, (worldYPos() + trackHeight), getWidth() - (rightPointerScreenPos - worldXPos()) /*- pOffset*/, trackHeight, getCornerRadius(), getColor(GuiColorType.ACCENT1));


//        // Draw track(s)
//        for (int i = 0; i < normalPointerValues.length; i++)
//        {
//            float currPointerCenter = getPointerScreenPos(i);
//
//            // Right side track for last pointer
//            if (i == normalPointerValues.length - 1)
//            {
//                float x = currPointerCenter;
//                float w = getWidth() - (currPointerCenter - xPos);
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

    protected void drawPointer(boolean isLeftPointer)
    {
        float x = isVertical ? worldXPos() : getPointerScreenPos(isLeftPointer) - (pointerSize / 2F);
        float y = isVertical ? getPointerScreenPos(isLeftPointer) - (pointerSize / 2F) : worldYPos();

        if (isLeftPointer)
        {
            RenderUtils.drawTexturedRect(pointerResource, x, y, zLevel, pointerSize, pointerSize, getColor(GuiColorType.BASE));
        }
        else
        {
            GL11.glPushMatrix();
            RenderUtils.rotateCurrentMatrixAroundObject(x + (pointerSize / 2), y + (pointerSize / 2),
                    180, 0, 0, 1);
            RenderUtils.drawTexturedRect(pointerResource, x, y, zLevel, pointerSize, pointerSize, getColor(GuiColorType.BASE));
            GL11.glPopMatrix();
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
}

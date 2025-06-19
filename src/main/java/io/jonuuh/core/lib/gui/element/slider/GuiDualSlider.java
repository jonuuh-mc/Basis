package io.jonuuh.core.lib.gui.element.slider;

import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiDualSlider extends GuiSlider
{
    protected static final ResourceLocation pointerResource = new ResourceLocation("core:textures/pointer.png");
    protected float normalValueRight;

    protected boolean isLastHeldPointerLeft;

    public GuiDualSlider(String elementName, float xPos, float yPos, float width, float height, float min, float max, float startValueLeft, float startValueRight, boolean isVertical, boolean isInteger)
    {
        super(elementName, xPos, yPos, width, height, min, max, startValueLeft, isVertical, isInteger);
        setRightValue(startValueRight);
    }

    public GuiDualSlider(String elementName, float xPos, float yPos, float min, float max, float startValueLeft, float startValueRight)
    {
        this(elementName, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, min, max, startValueLeft, startValueRight, false, false);
    }

    public float getRightValue()
    {
        return (float) MathUtils.denormalize(getRightNormalizedValue(), min, max);
    }

    public void setRightValue(float value)
    {
        setRightNormalizedValue((float) MathUtils.clamp(MathUtils.normalize(value, min, max)));
    }

    public float getRightNormalizedValue()
    {
        return normalValueRight;
    }

//    public float getLeftNormalizedValue()
//    {
//        return getNormalizedValue();
//    }

    public void setRightNormalizedValue(float normalValue)
    {
        float value = clampBetweenAdjacents(false, normalValue);
        // If an integer slider, round proportional to the max value
        normalValueRight = isInteger ? (Math.round(value * max) / max) : value;
        // handlePostCustomEvent();
    }

    @Override
    public void setNormalizedValue(float normalValue)
    {
        super.setNormalizedValue(clampBetweenAdjacents(true, normalValue));
        // handlePostCustomEvent();
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        super.onMouseDown(event);
        isLastHeldPointerLeft = getClosestPointerToMouse(event.mouseX, event.mouseY);
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event)
    {
        isMovingTimer = 10;
        float offset = (float) MathUtils.normalize(event.wheelDelta, min, max);

        if (isLastHeldPointerLeft)
        {
            setNormalizedValue(getNormalizedValue() + offset);
        }
        else
        {
            setRightNormalizedValue(getRightNormalizedValue() + offset);
        }
    }

//    @Override
//    public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
//    {
//        // TODO: move stuff from onScreenDraw here?
//    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (isMouseDown())
        {
            float value = getNormalValueAtScreenPos(mouseX, mouseY);

            if (isLastHeldPointerLeft)
            {
                setNormalizedValue(value);
            }
            else
            {
                setRightNormalizedValue(value);
            }
        }
    }

    @Override
    protected void drawHorizontalSlider()
    {
        float trackHeight = (getHeight() / 3F);

        float leftPointerScreenPos = getScreenPosAtNormalValue(getNormalizedValue());
        float rightPointerScreenPos = getScreenPosAtNormalValue(getRightNormalizedValue());

        // Left side track for all but first pointer
//        float x = /*(i == 0) ?*/ xPos /*: getPointerScreenPos(i - 1)*/;
//        float w = /*(i == 0) ? */ (leftPointerScreenPos - xPos) /*: (currPointerCenter - getPointerScreenPos(i - 1))*/;
//        float pOffset = pointerSize / 2F;

        // far left
        RenderUtils.drawRectangle(worldXPos(), (worldYPos() + trackHeight), (leftPointerScreenPos - worldXPos()) /*- pOffset*/, trackHeight, getColor(GuiColorType.ACCENT1));

        // middle
        RenderUtils.drawRectangle(leftPointerScreenPos /*- pOffset*/, (worldYPos() + trackHeight), (rightPointerScreenPos - leftPointerScreenPos) /*+ (pOffset * 2)*/, trackHeight, getColor(GuiColorType.BASE));

        // far right
        RenderUtils.drawRectangle(rightPointerScreenPos /*+ pOffset*/, (worldYPos() + trackHeight), getWidth() - (rightPointerScreenPos - worldXPos()) /*- pOffset*/, trackHeight, getColor(GuiColorType.ACCENT1));

        drawPointer(true);
        drawPointer(false);
    }

    // TODO:
    protected void drawVerticalSlider()
    {
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
    }

    protected void drawPointer(boolean isLeftPointer)
    {
        float screenPos = getScreenPosAtNormalValue(isLeftPointer ? getNormalizedValue() : getRightNormalizedValue());
        float x = isVertical ? worldXPos() : screenPos - (getPointerSize() / 2F);
        float y = isVertical ? screenPos - (getPointerSize() / 2F) : worldYPos();

        if (isLeftPointer)
        {
            RenderUtils.drawTexturedRect(pointerResource, x, y, getZLevel(), getPointerSize(), getPointerSize(), getColor(GuiColorType.BASE));
        }
        else
        {
            GL11.glPushMatrix();
            RenderUtils.rotateCurrentMatrixAroundObject(x + (getPointerSize() / 2), y + (getPointerSize() / 2),
                    180, 0, 0, 1);
            RenderUtils.drawTexturedRect(pointerResource, x, y, getZLevel(), getPointerSize(), getPointerSize(), getColor(GuiColorType.BASE));
            GL11.glPopMatrix();
        }
    }

    // what prevents sliders from passing each other
    protected float clampBetweenAdjacents(boolean isLeftPointer, double normalValue)
    {
        double minValue = !isLeftPointer ? (getNormalizedValue()) : 0;
        double maxValue = isLeftPointer ? (getRightNormalizedValue()) : 1;

        return (float) MathUtils.clamp(normalValue, minValue, maxValue);
    }

    // return true = isLeftPointer
    protected boolean getClosestPointerToMouse(int mouseX, int mouseY)
    {
        double mousePosSliderVal = getNormalValueAtScreenPos(mouseX, mouseY);

        double leftDistance = Math.abs(getNormalizedValue() - mousePosSliderVal);
        double rightDistance = Math.abs(getRightNormalizedValue() - mousePosSliderVal);

        if (leftDistance != rightDistance)
        {
            return rightDistance > leftDistance;
        }

        return mousePosSliderVal < getNormalizedValue();
    }

    public static class Builder extends GuiSlider.AbstractBuilder<Builder, GuiDualSlider>
    {
        protected float startValueRight;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder startValueRight(float startValueRight)
        {
            this.startValueRight = startValueRight;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiDualSlider build()
        {
            return new GuiDualSlider(this);
        }
    }
}

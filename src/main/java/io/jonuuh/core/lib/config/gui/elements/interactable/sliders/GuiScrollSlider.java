package io.jonuuh.core.lib.config.gui.elements.interactable.sliders;

import io.jonuuh.core.lib.config.gui.elements.GuiScrollContainer;
import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class GuiScrollSlider extends GuiInteractableElement
{
    protected final double min;
    protected final double max;
    protected final boolean isVertical;
    protected final double slidingWindowWidth;
    protected double slidingWindowTop;
    protected double grabPosValue;
    protected double lastSliderValue = -1;
    protected double startSliderValue;

    public GuiScrollSlider(GuiScrollContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, double startValue, boolean isVertical)
    {
        super(parent, elementName, xPos, yPos, width, height);
        this.min = min;
        this.max = max;
        this.isVertical = isVertical;

        setValue(startValue);
        this.slidingWindowWidth = (float) (height / max);
    }

    public GuiScrollSlider(GuiScrollContainer parent, String elementName, int xPos, int yPos, double min, double max, double startValue)
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

    public int getValueInt()
    {
        return (int) getValue();
    }

    public double getNormalizedValue()
    {
        return slidingWindowTop;
    }

    public void setNormalizedValue(double normalValue)
    {
        slidingWindowTop = MathUtils.clamp(normalValue, 0, 1 - slidingWindowWidth);
    }

    // mouse position on the slider
    protected double getSliderValueAtMousePos(int mouseX, int mouseY)
    {
        return isVertical
                ? (mouseY - yPos) / (double) height
                : (mouseX - xPos) / (double) width;
    }

    // x/y screen position of normal slider value
    protected float getScreenPosAtNormalValue(double normalValue)
    {
        return isVertical
                ? (float) (yPos + (normalValue * height))
                : (float) (xPos + (normalValue * width));
    }

    public void onMouseScroll(int wheelDelta)
    {
//        System.out.println(wheelDelta);
        setNormalizedValue(slidingWindowTop + MathUtils.normalize(wheelDelta * 6, min, max));
    }

    public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        double valueAtMouse = getSliderValueAtMousePos(mouseX, mouseY);

        // TODO: fix mouseDown logic (track recursively in container?)
        if (mouseDown && lastSliderValue != valueAtMouse)
        {
            double distance = valueAtMouse - grabPosValue;

//            System.out.println(startSliderValue + " " + grabPosValue + " " + distance);
            setNormalizedValue(startSliderValue + distance);

            lastSliderValue = valueAtMouse;
        }
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        grabPosValue = getSliderValueAtMousePos(mouseX, mouseY);
        startSliderValue = slidingWindowTop;
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        drawSlider();

//        if (mouseDown)
//        {
//            double valueAtMouse = getSliderValueAtMousePos(mouseX, mouseY);
//
//            if (lastSliderValue != valueAtMouse)
//            {
//                double distance = valueAtMouse - grabPosValue;
//
//                System.out.println(startSliderValue + " " + grabPosValue + " " + distance);
//                setNormalizedValue(startSliderValue + distance);
//
//                lastSliderValue = valueAtMouse;
//            }
//                System.out.println("lastSliderValue: " + lastSliderValue);

//            double valueAtMouse = getSliderValueAtMousePos(mouseX, mouseY);
//            double slidingWindowBottom = slidingWindowTop + slidingWindowWidth;
//
//            System.out.println(valueAtMouse + " / " + slidingWindowWidth + " " + slidingWindowBottom);
//            System.out.println(valueAtMouse - slidingWindowWidth);
//            System.out.println(MathUtils.denormalize(getNormalizedValue(), min, max) + " " + MathUtils.denormalize(slidingWindowBottom, min, max));
//
//            if (valueAtMouse < slidingWindowTop /*+ (slidingWindowWidth / 2F)*/)
//            {
//                setNormalizedValue(valueAtMouse);
//            }
//            else if (valueAtMouse >= slidingWindowBottom)
//            {
//                if (slidingWindowBottom < 1.0)
//                {
//                    setNormalizedValue(valueAtMouse - slidingWindowWidth);
//                }
//            }
//            }
//        }
    }

    protected void drawSlider()
    {
        if (!isVertical)
        {
            float trackHeight = (height / 3F);
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, (yPos + trackHeight), width, trackHeight, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
        }
        else
        {
            float trackWidth = (width / 3F);
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, (xPos + trackWidth), yPos, trackWidth, height, parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
        }

        drawPointer();
    }

    protected void drawPointer()
    {
        float screenPosWindowTop = getScreenPosAtNormalValue(slidingWindowTop);
        float screenPosWindowBottom = getScreenPosAtNormalValue(slidingWindowTop + slidingWindowWidth);

        float x = isVertical ? xPos : screenPosWindowTop /*- (pointerSize / 2F)*/;
        float y = isVertical ? screenPosWindowTop /*- (pointerSize / 2F)*/ : yPos;
        float w = isVertical ? width : screenPosWindowBottom - screenPosWindowTop;
        float h = isVertical ? screenPosWindowBottom - screenPosWindowTop : height;

        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, x, y, w, h, parent.getInnerRadius(), parent.getBaseColor().copy().setA(0.5F), true);
    }
}

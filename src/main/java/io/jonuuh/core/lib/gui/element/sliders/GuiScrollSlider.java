//package io.jonuuh.core.lib.gui.elements.sliders;
//
//import io.jonuuh.core.lib.gui.elements.GuiScrollContainer;
//import io.jonuuh.core.lib.gui.elements.interactable.GuiInteractableElement;
//import io.jonuuh.core.lib.util.MathUtils;
//import io.jonuuh.core.lib.util.RenderUtils;
//import org.lwjgl.opengl.GL11;
//
//public class GuiScrollSlider extends GuiInteractableElement
//{
//    protected final double min;
//    protected final double max;
//    protected final double slidingWindowWidth;
//    protected double slidingWindowTop;
//    protected double grabPosValue;
//    protected double lastSliderValue = -1;
//    protected double startSliderValue;
//
//    public GuiScrollSlider(GuiScrollContainer parent, String elementName, int xPos, int yPos, int width, int height, double min, double max, double startValue)
//    {
//        super(parent, elementName, xPos, yPos, width, height);
//        this.min = min;
//        this.max = max;
//
//        setValue(startValue);
//        this.slidingWindowWidth = (float) (height / max);
//    }
//
//    public GuiScrollSlider(GuiScrollContainer parent, String elementName, int xPos, int yPos, double min, double max, double startValue)
//    {
//        this(parent, elementName, xPos, yPos, 200, 16, min, max, startValue);
//    }
//
//    public double getValue()
//    {
//        return MathUtils.denormalize(getNormalizedValue(), min, max);
//    }
//
//    public void setValue(double value)
//    {
//        setNormalizedValue(MathUtils.normalize(value, min, max));
//    }
//
//    public int getValueInt()
//    {
//        return (int) getValue();
//    }
//
//    public double getNormalizedValue()
//    {
//        return slidingWindowTop;
//    }
//
//    public void setNormalizedValue(double normalValue)
//    {
//        slidingWindowTop = MathUtils.clamp(normalValue, 0, 1 - slidingWindowWidth);
//    }
//
//    // mouse position on the slider
//    protected double getSliderValueAtMousePos(int mouseY)
//    {
//        return (mouseY - yPos) / (double) height;
//    }
//
//    // x/y screen position of normal slider value
//    protected float getScreenPosAtNormalValue(double normalValue)
//    {
//        return (float) (yPos + (normalValue * height));
//    }
//
//    public void onMouseScroll(int wheelDelta)
//    {
////        System.out.println(wheelDelta);
//        setNormalizedValue(getNormalizedValue() + MathUtils.normalize(wheelDelta * 6, min, max));
//    }
//
//    public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
//    {
//        double valueAtMouse = getSliderValueAtMousePos(mouseY);
//
////        System.out.println(lastSliderValue + " " + valueAtMouse);
//
//        // TODO: fix mouseDown logic (track recursively in container?)
//        if (mouseDown && lastSliderValue != valueAtMouse)
//        {
//            double distance = valueAtMouse - grabPosValue;
//
////            System.out.println(startSliderValue + " " + grabPosValue + " " + distance);
//            setNormalizedValue(startSliderValue + distance);
//
//            lastSliderValue = valueAtMouse;
//        }
//    }
//
//    @Override
//    public void onMousePress(int mouseX, int mouseY)
//    {
//        super.onMousePress(mouseX, mouseY);
//        grabPosValue = getSliderValueAtMousePos(mouseY);
//
////        System.out.println("PRESS: " + grabPosValue + " " + startSliderValue + " " + getNormalizedValue());
//
//        if(grabPosValue < getNormalizedValue() || grabPosValue > getNormalizedValue() + slidingWindowWidth)
//        {
//            setNormalizedValue(grabPosValue - (slidingWindowWidth / 2F));
//        }
//
//        startSliderValue = getNormalizedValue();
////        System.out.println(getNormalizedValue());
//    }
//
//    @Override
//    protected void drawElement(int mouseX, int mouseY, float partialTicks)
//    {
//        drawSlider();
//    }
//
//    protected void drawSlider()
//    {
//        float trackWidth = (width / 3F);
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, (xPos + trackWidth), yPos, trackWidth, height,
//                parent.getOuterRadius(), parent.getColorMap().get("ACCENT"), true);
//
//        drawPointer();
//    }
//
//    protected void drawPointer()
//    {
//        float screenPosWindowTop = getScreenPosAtNormalValue(getNormalizedValue());
//        float screenPosWindowBottom = getScreenPosAtNormalValue(getNormalizedValue() + slidingWindowWidth);
//
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, screenPosWindowTop, width, screenPosWindowBottom - screenPosWindowTop,
//                parent.getInnerRadius(), parent.getBaseColor().copy().setA(0.5F), true);
//    }
//}

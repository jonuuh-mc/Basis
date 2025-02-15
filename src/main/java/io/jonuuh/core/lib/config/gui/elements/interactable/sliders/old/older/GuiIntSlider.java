//package io.jonuuh.configlib.gui.elements;
//
//import io.jonuuh.configlib.gui.ISettingsGui;
//
//public class GuiIntSlider extends GuiSlider
//{
//    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int width, int height, float radius, int min, int max, int start)
//    {
//        super(parent, xPos, yPos, width, height, radius, min, max, start);
//    }
//
//    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int min, int max, int start)
//    {
//        super(parent, xPos, yPos, min, max, start);
//    }
//
//    public GuiIntSlider(ISettingsGui parent, int xPos, int yPos, int min, int max)
//    {
//        super(parent, xPos, yPos, min, max);
//    }
//
//    // De-normalize
//    @Override
//    public double getValue()
//    {
//        return Math.round(normalizedVal * (max - min) + min);
//    }
//
//    public int getValueInt()
//    {
//        return (int) getValue();
//    }
//
//    // Normalize TODO: is this necessary? diff from super?
//    public void setValue(int value)
//    {
//        super.setValue(value);
//    }
//
//    @Override
//    protected void update(int mouseX)
//    {
//        normalizedVal = (mouseX - xPos) / (float) width;
//        normalizedVal = Math.round(normalizedVal * max) / max; // round (up?) to multiple (ex: 20 -> 0.05, 10 -> 0.1)
//        clamp();
//
//        updateTooltip();
//
//        sendChangeToParent();
//    }
//}

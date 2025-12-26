package io.jonuuh.basis.v000309.lib.gui.element.slider;

import io.jonuuh.basis.v000309.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.v000309.lib.util.Color;
import io.jonuuh.basis.v000309.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class GuiSingleSlider extends GuiSlider
{
    public GuiSingleSlider(Builder builder)
    {
        super(builder);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (isMouseDown())
        {
            setNormalizedValue(getNormalValueAtScreenPos(mouseX, mouseY));
        }
    }

    @Override
    protected void drawHorizontalSlider()
    {
        float x = worldXPos();
        float y = worldYPos();

        float trackThickness = getTrackThickness();
        float trackPos = y + trackThickness;
        float pointerPos = getScreenPosAtNormalValue(getNormalizedValue());

        // Left track
        RenderUtils.drawRoundedRectWithBorder(x, trackPos, pointerPos - x, trackThickness, getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));
        // Right track
        RenderUtils.drawRoundedRectWithBorder(pointerPos, trackPos, getWidth() - (pointerPos - x), trackThickness, getCornerRadius(), 1, getColor(GuiColorType.ACCENT1), getColor(GuiColorType.BORDER));

        drawPointer();
    }

    @Override
    protected void drawVerticalSlider()
    {
        float x = worldXPos();
        float y = worldYPos();

        float trackThickness = getTrackThickness();
        float trackPos = x + trackThickness;
        float pointerPos = getScreenPosAtNormalValue(getNormalizedValue());

        // Top track
        RenderUtils.drawRoundedRectWithBorder(trackPos, y, trackThickness, pointerPos - y, getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));
        // Bottom track
        RenderUtils.drawRoundedRectWithBorder(trackPos, pointerPos, trackThickness, getHeight() - (pointerPos - y), getCornerRadius(), 1, getColor(GuiColorType.ACCENT1), getColor(GuiColorType.BORDER));

        drawPointer();
    }

    protected void drawPointer()
    {
        boolean shouldScale = movingTimer > 0 || hoveredTimer > 0;

        float size = getPointerSize();
        float movingOffset = size / 4;

        if (shouldScale)
        {
            size += movingOffset;
        }

        float trackPos = (isVertical ? worldXPos() : worldYPos()) + getTrackThickness();
        float pointerPos = getScreenPosAtNormalValue(getNormalizedValue());

        float x = isVertical ? trackPos - (Math.abs(getTrackThickness() - size) / 2) : pointerPos - (size / 2);
        float y = isVertical ? pointerPos - (size / 2) : trackPos - (Math.abs(getTrackThickness() - size) / 2);

        RenderUtils.drawRoundedRectWithBorder(x, y, size, size, getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));

        if (debug)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, x, y, size, size, isFocused() ? new Color("#00ff00") : new Color("#ff55ff"));
        }
    }

    public static class Builder extends GuiSlider.AbstractBuilder<Builder, GuiSingleSlider>
    {
        public Builder(String elementName)
        {
            super(elementName);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiSingleSlider build()
        {
            return new GuiSingleSlider(this);
        }
    }
}

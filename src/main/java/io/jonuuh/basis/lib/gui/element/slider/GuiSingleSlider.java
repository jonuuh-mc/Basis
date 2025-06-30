package io.jonuuh.basis.lib.gui.element.slider;

import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class GuiSingleSlider extends GuiSlider
{
//    protected static final ResourceLocation pointerResource = new ResourceLocation("basis:textures/bar.png");

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
        RenderUtils.drawRoundedRect(x, trackPos, pointerPos - x, trackThickness, 5, getColor(GuiColorType.BASE));
        // Right track
        RenderUtils.drawRoundedRect(pointerPos, trackPos, getWidth() - (pointerPos - x), trackThickness, 5, getColor(GuiColorType.ACCENT1));

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
        RenderUtils.drawRoundedRect(trackPos, y, trackThickness, pointerPos - y, 5, getColor(GuiColorType.BASE));
        // Bottom track
        RenderUtils.drawRoundedRect(trackPos, pointerPos, trackThickness, getHeight() - (pointerPos - y), 5, getColor(GuiColorType.ACCENT1));

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

        RenderUtils.drawRoundedRect(x, y, size, size, 5, getColor(GuiColorType.BASE));

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

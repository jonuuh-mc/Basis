package io.jonuuh.core.lib.gui.element.slider;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSingleSlider extends GuiSlider
{
    protected static final ResourceLocation pointerResource = new ResourceLocation("core:textures/bar.png");

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
        RenderUtils.drawNineSliceTexturedRect(trackResource, x, trackPos, getZLevel(), pointerPos - x, trackThickness,
                32, 32, 5, 3, getColor(GuiColorType.BASE));
        // Right track
        RenderUtils.drawNineSliceTexturedRect(trackResource, pointerPos, trackPos, getZLevel(), getWidth() - (pointerPos - x), trackThickness,
                32, 32, 5, 3, getColor(GuiColorType.ACCENT1));

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
        RenderUtils.drawNineSliceTexturedRect(trackResource, trackPos, y, getZLevel(), trackThickness, pointerPos - y,
                32, 32, 5, 3, getColor(GuiColorType.BASE));
        // Bottom track
        RenderUtils.drawNineSliceTexturedRect(trackResource, trackPos, pointerPos, getZLevel(), trackThickness, getHeight() - (pointerPos - y),
                32, 32, 5, 3, getColor(GuiColorType.ACCENT1));

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

        RenderUtils.drawNineSliceTexturedRect(trackResource, x, y, getZLevel(), size, size, 32, 32, 5, 3, getColor(GuiColorType.ACCENT1));

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

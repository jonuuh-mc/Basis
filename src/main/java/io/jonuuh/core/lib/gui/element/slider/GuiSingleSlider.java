package io.jonuuh.core.lib.gui.element.slider;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

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
        float trackHeight = (getHeight() / 3F);
        float trackY = worldYPos() + trackHeight;
        float pointerScreenPos = getScreenPosAtNormalValue(getNormalizedValue());

        // left
        RenderUtils.drawRectangle(worldXPos(), trackY, (pointerScreenPos - worldXPos()), trackHeight, getColor(GuiColorType.BASE));
        // right
        RenderUtils.drawRectangle(pointerScreenPos, trackY, getWidth() - (pointerScreenPos - worldXPos()), trackHeight, getColor(GuiColorType.ACCENT1));

        drawPointer();
    }

    @Override
    protected void drawVerticalSlider()
    {
        float trackWidth = (getWidth() / 3F);
        float trackX = worldXPos() + trackWidth;
        float pointerScreenPos = getScreenPosAtNormalValue(getNormalizedValue());

        // left
        RenderUtils.drawRectangle(trackX, worldYPos(), trackWidth, (pointerScreenPos - worldYPos()), getColor(GuiColorType.BASE));
        // right
        RenderUtils.drawRectangle(trackX, pointerScreenPos, trackWidth, getHeight() - (pointerScreenPos - worldYPos()), getColor(GuiColorType.ACCENT1));

        drawPointer();
    }

    protected void drawPointer()
    {
        // TODO: figure this out
//        pointerSize = 14;
        float movingOffset = getPointerSize() / 4;
        float size = isMovingTimer > 0 ? getPointerSize() + movingOffset : getPointerSize();
        float yOffset = (getPointerSize() - getHeight()) / 2;

        float x = /*isVertical ? (isMovingTimer > 0 ? worldXPos() - (offset / 2) : worldXPos()) :*/ getScreenPosAtNormalValue(getNormalizedValue()) - (size / 2);
        float y = /*isVertical ? getPointerScreenPos() - (size / 2) :*/ (isMovingTimer > 0 ? worldYPos() - (movingOffset / 2) - yOffset : worldYPos() - yOffset);

        RenderUtils.drawTexturedRect(pointerResource, x, y, getZLevel(), size, size, getColor(GuiColorType.BASE));
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

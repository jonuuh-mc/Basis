package io.jonuuh.basis.lib.gui.element.slider;

import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseScrollEvent;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.MathUtils;
import io.jonuuh.basis.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class GuiDualSlider extends GuiSlider
{
    //    protected static final ResourceLocation pointerResource = new ResourceLocation("basis:textures/pointer.png");
    protected float normalValueEnd;
    /**
     * MouseDown and MouseScroll events will update the normal value corresponding to this pointer.
     * <p>
     * (Pointer.START={@link GuiSlider#normalValue}, Pointer.END={@link GuiDualSlider#normalValueEnd})
     */
    protected Pointer lastHeldPointer;
    /**
     * Used for tracking which of the two pointers should currently be 'highlighted' through scaling in drawPointer().
     * <p>
     * Will also get cached in {@link GuiDualSlider#lastHeldPointer} on MouseDown events.
     */
    protected Pointer closestPointerToMouse;

    public GuiDualSlider(Builder builder)
    {
        super(builder);
        // This is the same as `setValue(builder.endPointerValue, Pointer.END);`, but calling setValue() from constructor
        // is a bad idea as it also tries to apply the event behavior in setNormalizedValue()
        double value = MathUtils.normalize(builder.endPointerValue, min, max);
        float clampAgainstStart = (float) MathUtils.clamp(value, getNormalizedValue(Pointer.START), 1);
        this.normalValueEnd = isInteger ? roundAgainstSliderRange(clampAgainstStart) : clampAgainstStart;
    }

    public float getValue(Pointer pointer)
    {
        return (float) MathUtils.denormalize(getNormalizedValue(pointer), min, max);
    }

    public void setValue(float value, Pointer pointer)
    {
        setNormalizedValue((float) MathUtils.normalize(value, min, max), pointer);
    }

    public float getNormalizedValue(Pointer pointer)
    {
        return Pointer.START.equals(pointer) ? getNormalizedValue() : normalValueEnd;
    }

    public void setNormalizedValue(float normalValue, Pointer pointer)
    {
        if (Pointer.START.equals(pointer))
        {
            // Prevent the start slider pointer from passing the end
            float clampAgainstEnd = (float) MathUtils.clamp(normalValue, 0, getNormalizedValue(Pointer.END));
            super.setNormalizedValue(clampAgainstEnd);
        }
        else
        {
            // Prevent the end slider pointer from passing the start
            float clampAgainstStart = (float) MathUtils.clamp(normalValue, getNormalizedValue(Pointer.START), 1);
            // If an integer slider, round proportional to the max value
            normalValueEnd = isInteger ? roundAgainstSliderRange(clampAgainstStart) : clampAgainstStart;
            tryApplyPostEventBehavior(MouseDownEvent.class);
        }
    }

    @Override
    protected float getPointerSizeRatio()
    {
        return 1.5F;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        if (isMouseDown())
        {
            float value = getNormalValueAtScreenPos(mouseX, mouseY);
            setNormalizedValue(value, lastHeldPointer);
        }
        // The check against movingTimer lets onMouseScroll always 'claim' the closest pointer to mouse so that it shows
        // as highlighted while scrolling without being immediately overridden by the standard isHovered() check. This
        // extra check only matters if the last held pointer and the closest pointer are not equal during
        // a MouseScroll, letting the highlight 'bounce' back and forth
        else if (isHovered() && movingTimer == 0)
        {
            closestPointerToMouse = getClosestPointerToMouse(mouseX, mouseY);
        }
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        super.onMouseDown(event);
        lastHeldPointer = closestPointerToMouse;
    }

    @Override
    public void onMouseScroll(MouseScrollEvent event)
    {
        movingTimer = 10;
        setNormalizedValue(getNormalizedValue(lastHeldPointer) + scaleWheelDelta(event.wheelDelta), lastHeldPointer);
        closestPointerToMouse = lastHeldPointer;
    }

    @Override
    protected void drawHorizontalSlider()
    {
        float trackHeight = getTrackThickness();
        float trackY = worldYPos() + trackHeight;

        float leftPointerScreenPos = getScreenPosAtNormalValue(getNormalizedValue(Pointer.START));
        float rightPointerScreenPos = getScreenPosAtNormalValue(getNormalizedValue(Pointer.END));

        // Start
        RenderUtils.drawRoundedRect(worldXPos(), trackY, (leftPointerScreenPos - worldXPos()), trackHeight, getCornerRadius(), getColor(GuiColorType.ACCENT1));
        // Middle
        RenderUtils.drawRoundedRect(leftPointerScreenPos, trackY, (rightPointerScreenPos - leftPointerScreenPos), trackHeight, getCornerRadius(), getColor(GuiColorType.BASE));
        // End
        RenderUtils.drawRoundedRect(rightPointerScreenPos, trackY, getWidth() - (rightPointerScreenPos - worldXPos()), trackHeight, getCornerRadius(), getColor(GuiColorType.ACCENT1));

        drawPointer(Pointer.START);
        drawPointer(Pointer.END);
    }

    @Override
    protected void drawVerticalSlider()
    {
        float trackWidth = getTrackThickness();
        float trackX = worldXPos() + trackWidth;

        float topPointerPos = getScreenPosAtNormalValue(getNormalizedValue(Pointer.START));
        float bottomPointerPos = getScreenPosAtNormalValue(getNormalizedValue(Pointer.END));

        // Start
        RenderUtils.drawRoundedRect(trackX, worldYPos(), trackWidth, (topPointerPos - worldYPos()), getCornerRadius(), getColor(GuiColorType.ACCENT1));
        // Middle
        RenderUtils.drawRoundedRect(trackX, topPointerPos, trackWidth, (bottomPointerPos - topPointerPos), getCornerRadius(), getColor(GuiColorType.BASE));
        // End
        RenderUtils.drawRoundedRect(trackX, bottomPointerPos, trackWidth, getHeight() - (bottomPointerPos - worldYPos()), getCornerRadius(), getColor(GuiColorType.ACCENT1));

        drawPointer(Pointer.START);
        drawPointer(Pointer.END);
    }

    protected void drawPointer(Pointer pointer)
    {
        boolean isStartPointer = Pointer.START.equals(pointer);
        boolean shouldScale = (movingTimer > 0 || hoveredTimer > 0) && closestPointerToMouse.equals(pointer);

        float size = getPointerSize();
        float movingOffset = size / 4;

        if (shouldScale)
        {
            size += movingOffset;
        }

        float trackPos = (isVertical ? worldXPos() : worldYPos()) + getTrackThickness();
        float pointerPos = getScreenPosAtNormalValue(isStartPointer ? getNormalizedValue(Pointer.START) : getNormalizedValue(Pointer.END));

        float x = isVertical ? trackPos - (Math.abs(getTrackThickness() - size) / 2) : pointerPos - (size / 2);
        float y = isVertical ? pointerPos - (size / 2) : trackPos - (Math.abs(getTrackThickness() - size) / 2);

        float rotateX = x + (getPointerSize() / 2);
        float rotateY = y + (getPointerSize() / 2);

        if (shouldScale)
        {
            rotateX += (movingOffset / 2);
            rotateY += (movingOffset / 2);
        }

        if (isVertical)
        {
            GL11.glPushMatrix();
            RenderUtils.rotateCurrentMatrixAroundObject(rotateX, rotateY, (isStartPointer ? 90 : -90), 0, 0, 1);
            RenderUtils.drawRoundedRect(x, y, size, size, getCornerRadius(), getColor(GuiColorType.ACCENT1));
            GL11.glPopMatrix();
        }
        else
        {
            if (isStartPointer)
            {
                RenderUtils.drawRoundedRect(x, y, size, size, getCornerRadius(), getColor(GuiColorType.ACCENT1));
            }
            else
            {
                GL11.glPushMatrix();
                RenderUtils.rotateCurrentMatrixAroundObject(rotateX, rotateY, 180, 0, 0, 1);
                RenderUtils.drawRoundedRect(x, y, size, size, getCornerRadius(), getColor(GuiColorType.ACCENT1));
                GL11.glPopMatrix();
            }
        }

        if (debug)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, x, y, size, size, isFocused() ? new Color("#00ff00") : new Color("#ff55ff"));
        }
    }

    protected Pointer getClosestPointerToMouse(int mouseX, int mouseY)
    {
        double normalMousePos = getNormalValueAtScreenPos(mouseX, mouseY);

        double distanceFromMouseToStartPointer = Math.abs(getNormalizedValue(Pointer.START) - normalMousePos);
        double distanceFromMouseToEndPointer = Math.abs(getNormalizedValue(Pointer.END) - normalMousePos);

        // If the pointers don't perfectly overlap:
        // Return the pointer with the smaller distance to the mouse
        if (distanceFromMouseToStartPointer != distanceFromMouseToEndPointer)
        {
            return distanceFromMouseToStartPointer < distanceFromMouseToEndPointer ? Pointer.START : Pointer.END;
        }

        // This extra calculation is necessary to resolve perfectly overlapping pointers.
        // If the pointers are perfectly overlapping we can't figure out which is closer to the mouse by comparing them
        // to each other, so we have to compare them to the mouse instead.
        // Also, we can't just use this as the only condition either because in that case this would always return END
        // whenever the mouse is to the right/bottom of the start pointer
        return normalMousePos < getNormalizedValue(Pointer.START) ? Pointer.START : Pointer.END;
    }

    public enum Pointer
    {
        START,
        END
    }

    public static class Builder extends GuiSlider.AbstractBuilder<Builder, GuiDualSlider>
    {
        protected float endPointerValue;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder endPointerValue(float endPointerValue)
        {
            this.endPointerValue = endPointerValue;
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

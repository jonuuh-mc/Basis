package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class GuiSwitch extends GuiElement
{
    protected float pointerSize;
    protected boolean switchState;

    // TODO: make vertical option? subclass?
    public GuiSwitch(String elementName, float xPos, float yPos, float width, float height, boolean switchState)
    {
        super(elementName, xPos, yPos, Math.max(height, width), Math.min(Math.max(height, width), height)); // height should never be > width, width should never be < height
        this.switchState = switchState;
    }

    public GuiSwitch(String elementName, float xPos, float yPos, boolean switchState)
    {
        this(elementName, xPos, yPos, DEFAULT_HEIGHT * 2, DEFAULT_HEIGHT, switchState);
    }

    public GuiSwitch(String elementName, float xPos, float yPos)
    {
        this(elementName, xPos, yPos, false);
    }

    public boolean getSwitchState()
    {
        return switchState;
    }

    public void setSwitchState(boolean switchState)
    {
        this.switchState = switchState;
    }

    public void flip()
    {
        setSwitchState(!switchState);
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
        pointerSize = getHeight() - 2F;
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        float padding = ((getHeight() - pointerSize) / 2F);
        float pointerX = switchState ? (worldXPos() + getWidth() - pointerSize - padding) : worldXPos() + padding;
        Color trackColor = switchState ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT2);

        // Track
        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), trackColor);
        // Pointer
        RenderUtils.drawRoundedRect(pointerX, worldYPos() + padding, pointerSize, pointerSize, getCornerRadius() - 1, getColor(GuiColorType.ACCENT1));
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        flip();
    }

    @Override
    protected void onKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == Keyboard.KEY_RETURN)
        {
            flip();
            playClickSound(mc.getSoundHandler());
        }
    }
}


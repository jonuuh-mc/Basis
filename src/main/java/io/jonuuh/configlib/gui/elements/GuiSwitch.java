package io.jonuuh.configlib.gui.elements;

import io.jonuuh.configlib.util.Color;
import io.jonuuh.configlib.gui.GuiUtils;
import io.jonuuh.configlib.gui.ISettingsGui;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiInteractableElement
{
    protected final float pointerSize;
    protected final float radius;
    protected boolean switchState;

    public GuiSwitch(ISettingsGui parent, int xPos, int yPos, int width, int height, float radius, String tooltip, boolean switchState)
    {
        super(parent, xPos, yPos, Math.max(height, width), Math.min(Math.max(height, width), height), tooltip); // height should never be > width, width should never be < height
        this.radius = Math.min(radius, height / 2F); // radius should never be > half height
        this.pointerSize = height - 2F; // assoc w/ <radius - 1> in pointer draw?
        this.switchState = switchState;
    }

    public GuiSwitch(ISettingsGui parent, int xPos, int yPos, String tooltip, boolean switchState)
    {
        this(parent, xPos, yPos, 16, 8, 4, tooltip, switchState);
    }

    public GuiSwitch(ISettingsGui parent, int xPos, int yPos, boolean switchState)
    {
        this(parent, xPos, yPos, "", switchState);
    }

    public GuiSwitch(ISettingsGui parent, int xPos, int yPos)
    {
        this(parent, xPos, yPos, false);
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
        switchState = !switchState;
        sendChangeToParent();
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        super.drawButton(mc, mouseX, mouseY);

        if (visible)
        {
            float padding = ((height - pointerSize) / 2F);
            float pointerX = switchState ? (xPosition + width - pointerSize - padding) : xPosition + padding;
            Color trackColor = switchState ? baseColor : disabledColor;

            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, xPosition, yPosition, width, height, radius, trackColor, true);
            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, pointerX, yPosition + padding, pointerSize, pointerSize, radius - 1, accentColor, true);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);

        if (pressed)
        {
            flip();
        }
        return pressed;
    }
}


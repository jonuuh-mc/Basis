package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.ISettingsGui;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiInteractableElement
{
    protected final float pointerSize;
    //    protected final float radius;
    protected boolean switchState;

    // TODO: make vertical option? subclass?
    public GuiSwitch(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltipStr, boolean switchState)
    {
        super(parent, xPos, yPos, Math.max(height, width), Math.min(Math.max(height, width), height), tooltipStr); // height should never be > width, width should never be < height
//        this.radius = Math.min(radius, height / 2F); // radius should never be > half height
        this.pointerSize = height - 2F; // assoc w/ <radius - 1> in pointer draw?
        this.switchState = switchState;
    }

    public GuiSwitch(ISettingsGui parent, int xPos, int yPos, String tooltipStr, boolean switchState)
    {
        this(parent, xPos, yPos, 32, 16, tooltipStr, switchState);
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
        setSwitchState(!switchState);
        sendChangeToParent();
    }

    @Override
    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        super.onScreenDraw(mc, mouseX, mouseY);

        float padding = ((height - pointerSize) / 2F);
        float pointerX = switchState ? (xPos + width - pointerSize - padding) : xPos + padding;
        Color trackColor = switchState ? colorMap.get("BASE") : colorMap.get("DISABLED");

        // Track
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, parent.getInnerRadius(), trackColor, true);
        // Pointer
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, pointerX, yPos + padding, pointerSize, pointerSize, parent.getInnerRadius() - 1, colorMap.get("ACCENT"), true);
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        flip();
    }
}


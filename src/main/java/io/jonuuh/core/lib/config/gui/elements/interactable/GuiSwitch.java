package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiSettingElement
{
    protected final float pointerSize;
    protected boolean switchState;

    // TODO: make vertical option? subclass?
    public GuiSwitch(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr, boolean switchState)
    {
        super(parent, elementName, xPos, yPos, Math.max(height, width), Math.min(Math.max(height, width), height), tooltipStr); // height should never be > width, width should never be < height
        this.pointerSize = height - 2F;
        this.switchState = switchState;
    }

    public GuiSwitch(GuiContainer parent, String elementName, int xPos, int yPos, String tooltipStr, boolean switchState)
    {
        this(parent, elementName, xPos, yPos, 32, 16, tooltipStr, switchState);
    }

    public GuiSwitch(GuiContainer parent, String elementName, int xPos, int yPos, boolean switchState)
    {
        this(parent, elementName, xPos, yPos, "", switchState);
    }

    public GuiSwitch(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        this(parent, elementName, xPos, yPos, false);
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
        sendChangeToSetting();
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        float padding = ((height - pointerSize) / 2F);
        float pointerX = switchState ? (xPos + width - pointerSize - padding) : xPos + padding;
        Color trackColor = switchState ? baseColor : parent.getColorMap().get("DISABLED");

        // Track
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, parent.getInnerRadius(), trackColor, true);
        // Pointer
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, pointerX, yPos + padding, pointerSize, pointerSize, parent.getInnerRadius() - 1, parent.getColorMap().get("ACCENT"), true);
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        flip();
    }

    @Override
    protected void updateSetting()
    {
        ((BoolSetting) associatedSetting).setValue(switchState);
    }
}


package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiSettingElement
{
    protected float pointerSize;
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
        trySendChangeToSetting();
    }

//    @Override
//    protected void onInitGui(int guiScreenWidth, int guiScreenHeight)
//    {
//        pointerSize = height - 2F;
//    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        float padding = ((height - pointerSize) / 2F);
        float pointerX = switchState ? (worldXPos() + width - pointerSize - padding) : worldXPos() + padding;
        Color trackColor = switchState ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT2);

        // Track
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), width, height, parent.getInnerRadius(), trackColor, true);
        // Pointer
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, pointerX, worldYPos() + padding, pointerSize, pointerSize, parent.getInnerRadius() - 1, getColor(GuiColorType.ACCENT1), true);
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        flip();
    }

    @Override
    protected void onKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == Keyboard.KEY_RETURN /*|| keyCode == Keyboard.KEY_SPACE*/)
        {
            flip();
            playClickSound(mc.getSoundHandler());
        }
    }

    @Override
    protected void updateSetting()
    {
        ((BoolSetting) associatedSetting).setCurrentValue(switchState);
    }
}


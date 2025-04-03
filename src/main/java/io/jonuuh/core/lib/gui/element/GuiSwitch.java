package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiSwitch extends GuiSettingElement
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
        this(elementName, xPos, yPos, 20, 10, switchState);
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
        trySendChangeToSetting();
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
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), parent.getInnerRadius(), trackColor, true);
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


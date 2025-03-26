package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.elements.container.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.Setting;

public abstract class GuiSettingElement extends GuiElement
{
    protected Setting<?> associatedSetting;

    protected GuiSettingElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, tooltipStr);
    }

    protected GuiSettingElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height)
    {
        super(parent, elementName, xPos, yPos, width, height);
    }

    protected GuiSettingElement(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        super(parent, elementName, xPos, yPos);
    }

    public void associateSetting(Setting<?> associatedSetting)
    {
        this.associatedSetting = associatedSetting;
    }

    public void disassociateSetting()
    {
        associatedSetting = null;
    }

    public boolean hasSettingAssociation()
    {
        return associatedSetting != null;
    }

    protected void trySendChangeToSetting()
    {
        if (hasSettingAssociation())
        {
            updateSetting();
        }
    }

    protected abstract void updateSetting();
}

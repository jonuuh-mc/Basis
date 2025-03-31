package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.config.setting.types.Setting;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;

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

    // TODO: enforce concrete type by overriding in subclasses?
    //  also add feature to auto init element "value" with setting value?
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

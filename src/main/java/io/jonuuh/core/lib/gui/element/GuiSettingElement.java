package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.config.setting.types.Setting;

public abstract class GuiSettingElement extends GuiElement
{
    protected Setting<?> associatedSetting;

    protected GuiSettingElement(String elementName, float xPos, float yPos, float width, float height)
    {
        super(elementName, xPos, yPos, width, height);
    }

    protected GuiSettingElement(String elementName, float xPos, float yPos)
    {
        super(elementName, xPos, yPos);
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

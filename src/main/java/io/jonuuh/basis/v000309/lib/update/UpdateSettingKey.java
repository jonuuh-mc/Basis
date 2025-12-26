package io.jonuuh.basis.v000309.lib.update;

public enum UpdateSettingKey
{
    LAST_LATEST_VERSION(),
    REPEAT_NOTIFY("repeatNotify");

    private String friendlyName;

    UpdateSettingKey()
    {
    }

    UpdateSettingKey(String friendlyName)
    {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString()
    {
        return this.friendlyName.isEmpty() ? super.toString() : friendlyName;
    }
}

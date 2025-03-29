package io.jonuuh.core.lib.update;

public enum UpdateSettingsData
{
    CATEGORY("update"),
    LAST_LATEST_VERSION,
    REPEAT_NOTIFY("repeatNotify");

    private String friendlyName;

    UpdateSettingsData()
    {
    }

    UpdateSettingsData(String friendlyName)
    {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString()
    {
        return this.friendlyName.isEmpty() ? super.toString() : friendlyName;
    }
}

package io.jonuuh.core.lib.config.command;

import io.jonuuh.core.lib.config.SettingsConfigurationAdapter;
import io.jonuuh.core.lib.config.setting.Settings;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.update.UpdateSettingsData;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public final class CommandCore extends CommandBase
{
    private final String commandName;
    private final Settings updateSettings;

    public CommandCore(String modID)
    {
        this.commandName = modID + "$core";
        this.updateSettings = SettingsConfigurationAdapter.INSTANCE.getSettings(UpdateSettingsData.CATEGORY.toString());
    }

    @Override
    public String getCommandName()
    {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP) || args.length == 0)
        {
            return;
        }

        if (updateSettings != null && args.length == 2 && args[0].equals(UpdateSettingsData.REPEAT_NOTIFY.toString()))
        {
            BoolSetting boolSetting = (BoolSetting) updateSettings.get(UpdateSettingsData.REPEAT_NOTIFY.name());

            if (boolSetting == null || (!args[1].equals("true") && !args[1].equals("false")))
            {
                return;
            }

            switch (args[1])
            {
                case "true":
                    boolSetting.setCurrentValue(true);
                    break;
                case "false":
                    boolSetting.setCurrentValue(false);
                    break;
            }

            boolSetting.setDefaultValue(false);

            updateSettings.saveCurrentValues();
        }
    }
}

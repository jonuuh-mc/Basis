package io.jonuuh.basis.v000309.local;

import io.jonuuh.basis.v000309.lib.gui.BaseGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class CommandOpenSettingsGui extends CommandBase
{
    protected final String commandName;
    protected final BaseGuiScreen gui;

    public CommandOpenSettingsGui(String commandName, BaseGuiScreen settingsGui)
    {
        this.commandName = commandName;
        this.gui = settingsGui;
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
        if (!(sender.getCommandSenderEntity() instanceof EntityPlayerSP))
        {
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Need to wait until the start of the next tick to display GUI TODO: why is this necessary again
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        Minecraft.getMinecraft().displayGuiScreen(gui);
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}

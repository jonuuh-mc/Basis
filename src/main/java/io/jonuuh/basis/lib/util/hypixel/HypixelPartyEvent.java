package io.jonuuh.basis.lib.util.hypixel;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Arrays;

public class HypixelPartyEvent extends Event
{
    public final Type type;
    public final String[] playerNames;

    public HypixelPartyEvent(Type type, String[] playerNames)
    {
        this.type = type;
        this.playerNames = playerNames;
    }

    public HypixelPartyEvent(Type type, String playerName)
    {
        this(type, new String[]{playerName});
    }

    public HypixelPartyEvent(Type type)
    {
        this(type, "");
    }

    public enum Type
    {
        CREATE, DISBAND, JOIN, LEAVE
    }

    @Override
    public String toString()
    {
        return "PartyEvent{" + "type=" + type + ", playerNames=" + Arrays.toString(playerNames) + '}';
    }
}

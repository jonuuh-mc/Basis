package io.jonuuh.core.lib.util.hypixel;

import io.jonuuh.core.lib.util.ChatLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class HypixelPartyEventPoster
{
    private final Minecraft mc;
    //    private final Pattern playerSentChatPattern;
    private boolean isHypixel;
    private boolean justConnected;
    private boolean waitingForPartyListChat;
    private final List<String> partyMembersBuffer;
    private boolean waitingForPartyingWithChat;
    private final Pattern otherJoinedPartyPattern = Pattern.compile("\\w{3,16} joined the party.");
    private final Pattern otherLeftPartyPattern = Pattern.compile("\\w{3,16} has left the party.");
    private final Pattern manualDisbandPartyPattern = Pattern.compile("\\w{3,16} has disbanded the party!");

//    private final String playerNameRegex = "(\\[[A-Za-z+]+] )?(\\w{3,16})";
//    private final Pattern joinPattern = Pattern.compile("You have joined " + playerNameRegex + "'s party!");
//    private final Pattern partyWithPattern = Pattern.compile("You'll be partying with: (" + playerNameRegex + "(, )?)+");
//    private final Pattern joinedPattern = Pattern.compile(playerNameRegex + " joined the party.");

    public HypixelPartyEventPoster()
    {
        this.mc = Minecraft.getMinecraft();
        this.partyMembersBuffer = new ArrayList<>();
//        this.playerSentChatPattern = Pattern.compile(".* ?\\w{3,16}: .*"); // TODO: who the hell knows how many edge cases this has
    }

    @SubscribeEvent
    public void onClientConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("hypixel"))
        {
            isHypixel = true;
            justConnected = true;
        }
    }

    @SubscribeEvent
    public void onClientDisconnectFromServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        if (isHypixel)
        {
            isHypixel = false;
            MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.DISBAND));
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity == mc.thePlayer && justConnected)
        {
            justConnected = false;
            mc.thePlayer.sendChatMessage("/pl");
            waitingForPartyListChat = true;
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if (!isHypixel)
        {
            return;
        }

        String msg = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());

//        // normal player-sent chat message e.g. `[MVP+] jonuuh: hello`
//        // horrible idea (big surprise there) (e.g. `Party Leader: [VIP] gaehgahjwegaheag ?` matches)
//        if (playerSentChatPattern.matcher(msg).matches())
//        {
//            return;
//        }

//        System.out.println(msg);
//        ChatLogger.addLog(msg);
        msg = trimRanks(msg).trim();
//        ChatLogger.addLog(msg);
//        System.out.println(msg);

        /*
            [23:48:17] [Client thread/INFO]: [CHAT] -----------------------------------------------------
            [23:48:17] [Client thread/INFO]: [CHAT] Party Members (2)
            [23:48:17] [Client thread/INFO]: [CHAT]
            [23:48:17] [Client thread/INFO]: [CHAT] Party Leader: [MVP++] jonuuh ?
            [23:48:17] [Client thread/INFO]: [CHAT] Party Members: [MVP++] PlayerName ?
            [23:48:17] [Client thread/INFO]: [CHAT] -----------------------------------------------------

            `Party Moderators: [MVP++] jonuuh ?`
         */

        /*
            [20:49:28] [Client thread/INFO]: [CHAT] -----------------------------------------------------
            [20:49:28] [Client thread/INFO]: [CHAT] §6§l[§8§lBNT§6§l] PartyEvent{type=CREATE, playerNames=[oLeafy, jonuuh]}
            [20:49:28] [Client thread/INFO]: [CHAT] §6§l[§8§lBNT§6§l] [oLeafy, jonuuh]
            [20:49:28] [Client thread/INFO]: [CHAT] You have joined [MVP++] oLeafy's party!
            [20:49:28] [Client thread/INFO]: [CHAT]
            [20:49:28] [Client thread/INFO]: [CHAT] You'll be partying with: [MVP++] Cluelesss
            [20:49:28] [Client thread/INFO]: [CHAT] -----------------------------------------------------
         */

        // TODO: parse party list chat
        if (waitingForPartyListChat && msg.equals("You are not currently in a party."))
        {
            waitingForPartyListChat = false;
        }
        else if (waitingForPartyListChat)
        {
            if (msg.startsWith("Party Leader: "))
            {
                String playerName = msg.substring("Party Leader: ".length(), msg.lastIndexOf(' '));
                partyMembersBuffer.add(playerName);
            }
            else if (msg.startsWith("Party Moderators: ")) // TODO:
            {

            }
            else if (msg.startsWith("Party Members: "))
            {
//                char[] arr = msg.toCharArray();
//                for (char c : arr)
//                {
//                    System.out.println(c + ": " + (int) c);
//                }

                String playerNamesStr = msg.substring("Party Members: ".length());
                String[] playerNames = playerNamesStr.contains(" \u25cf") ? playerNamesStr.split(" \u25cf") : new String[]{playerNamesStr};
                playerNames = Arrays.stream(playerNames).map(String::trim).toArray(String[]::new);

                ChatLogger.INSTANCE.addLog(playerNamesStr + " /// " + Arrays.toString(playerNames));

                partyMembersBuffer.addAll(Arrays.asList(playerNames));
                waitingForPartyListChat = false;
                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.CREATE, partyMembersBuffer.toArray(new String[0])));
                partyMembersBuffer.clear();
            }
        }

        try
        {
            // `[MVP++] jonuuh invited [VIP] PlayerName to the party! They have 60 seconds to accept.`
            // `jonuuh invited PlayerName to the party! They have 60 seconds to accept.`
            // Self invite someone else (also need to check for already existing party?)
            if (msg.startsWith(mc.thePlayer.getName()) && msg.endsWith(" to the party! They have 60 seconds to accept."))
            {
                if (msg.substring(0, msg.indexOf(" invited")).equals(mc.thePlayer.getName()))
                {
                    MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.CREATE, mc.thePlayer.getName()));
                }
                else
                {
//                    throw new PartyEventException("Unknown problem matching pattern: `[MVP++] Player invited [VIP] PlayerName to the party! They have 60 seconds to accept.`");
                }
            }

            // `You have joined [MVP+] PlayerName's party!`
            else if (msg.startsWith("You have joined ") && msg.endsWith("'s party!"))
            {
                String partyLeaderName = msg.substring("You have joined ".length(), msg.indexOf("'s party!"));
                partyMembersBuffer.add(mc.thePlayer.getName());
                partyMembersBuffer.add(partyLeaderName);
                waitingForPartyingWithChat = true;
            }
            // `You'll be partying with: [MVP+] PlayerName1, [VIP] PlayerName2` / `You'll be partying with: [MVP+] PlayerName1`
            else if (waitingForPartyingWithChat && msg.startsWith("You'll be partying with: "))
            {
                String playerNamesStr = msg.substring("You'll be partying with: ".length());
                String[] playerNames = playerNamesStr.contains(", ") ? playerNamesStr.split(", ") : new String[]{playerNamesStr};
                ChatLogger.INSTANCE.addLog("partying with: " + Arrays.toString(playerNames));
//                if (playerNamesStr.contains(", "))
//                {
//                    playerNames = playerNamesStr.split(", ");
//
//                    for (int i = 0; i < playerNames.length; i++)
//                    {
//                        playerNames[i] = trimPlayerRank(playerNames[i]);
//                    }
//                }
//                else
//                {
//                    playerNames = new String[]{playerNamesStr};
//                }
                partyMembersBuffer.addAll(Arrays.asList(playerNames));

                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.CREATE, partyMembersBuffer.toArray(new String[0])));
//                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.JOIN, playerNames));
            }

            // `[VIP] PlayerName joined the party.`
            else if (otherJoinedPartyPattern.matcher(msg).matches())
            {
                String playerName = msg.substring(0, msg.indexOf(" joined the party."));
                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.JOIN, playerName));
            }
            // `[VIP] PlayerName has left the party.`
            else if (otherLeftPartyPattern.matcher(msg).matches())
            {
                String playerName = msg.substring(0, msg.indexOf(" has left the party."));
                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.LEAVE, playerName));
            }

            // `You left the party.`
            // `[MVP++] PlayerName has disbanded the party!`
            // `The party was disbanded because all invites expired and the party was empty.`
            else if (msg.equals("You left the party.") || manualDisbandPartyPattern.matcher(msg).matches()
                    || msg.equals("The party was disbanded because all invites expired and the party was empty."))
            {
                MinecraftForge.EVENT_BUS.post(new HypixelPartyEvent(HypixelPartyEvent.Type.DISBAND));
            }
        }
        catch (Exception e)
        {
            ChatLogger.INSTANCE.addLog("EXCEPTION ON CHAT RECEIVED: " + e.toString());
            e.printStackTrace();
        }
    }

//    // Assumes format: '[MVP+] PlayerName1', '[VIP] PlayerName2', 'PlayerName3', etc
//    private String trimPlayerRank(String playerNameWithRank)
//    {
//        if (playerNameWithRank.contains("[") && playerNameWithRank.contains("]"))
//        {
//            return playerNameWithRank.substring(playerNameWithRank.indexOf(']') + 1).trim();
//        }
//        return playerNameWithRank;
//    }

    // TODO: scuffed design but not really sure of a better way atm
    //       surely not error prone right
    private String trimRanks(String msg)
    {
        msg = trimRankIfExists(msg, "[VIP]");
        msg = trimRankIfExists(msg, "[VIP+]");

        msg = trimRankIfExists(msg, "[MVP]");
        msg = trimRankIfExists(msg, "[MVP+]");
        msg = trimRankIfExists(msg, "[MVP++]");

        msg = trimRankIfExists(msg, "[YOUTUBE]");
        msg = trimRankIfExists(msg, "[MOJANG]");
        msg = trimRankIfExists(msg, "[EVENTS]");
        msg = trimRankIfExists(msg, "[MCP]");
        msg = trimRankIfExists(msg, "[INNIT]");
        msg = trimRankIfExists(msg, "[PIG+++]");

        msg = trimRankIfExists(msg, "[GM]");
        msg = trimRankIfExists(msg, "[ADMIN]");
        msg = trimRankIfExists(msg, "[OWNER]");

        return msg;
    }

    private String trimRankIfExists(String msg, String rank)
    {
        return msg.contains(rank + " ") ? msg.replace(rank + " ", "") : msg;
    }
}

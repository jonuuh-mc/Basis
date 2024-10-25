package io.jonuuh.core.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;

public abstract class Util
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static RenderManager getRenderManager()
    {
        return mc.getRenderManager();
    }

    public static FontRenderer getFontRenderer()
    {
        return mc.fontRendererObj;
    }

    public static int getStringWidth(String str)
    {
        return getFontRenderer().getStringWidth(str) - 1; // getStringWidth seems to always be over by 1 (for loop end condition error?)
    }

    public static NetworkPlayerInfo getPlayerInfo(EntityPlayer player)
    {
        return mc.getNetHandler().getPlayerInfo(player.getUniqueID());
    }

//        // from GuiPlayerTabOverlay & ScorePlayerTeam
//        public static String getFormattedPlayerName(NetworkPlayerInfo playerInfo, boolean doPrefix, boolean doSuffix)
//        {
//            return (playerInfo.getDisplayName() != null) ? playerInfo.getDisplayName().getFormattedText()
//                    : formatPlayerName(playerInfo.getPlayerTeam(), playerInfo.getGameProfile().getName(), doPrefix, doSuffix);
//        }
//
//        private static String formatPlayerName(ScorePlayerTeam team, String name, boolean doPrefix, boolean doSuffix)
//        {
//            return team == null ? name : formatString(team, name, doPrefix, doSuffix);
//        }
//
//        private static String formatString(ScorePlayerTeam team, String str, boolean doPrefix, boolean doSuffix)
//        {
//            if (!doPrefix)
//            {
//                str = "\u00a7" + getFirstColorCode(team.getColorPrefix() + str + team.getColorSuffix()) + str;
//            }
//
//            return (doPrefix ? team.getColorPrefix() : "") + str + (doSuffix ? team.getColorSuffix() : "");
//        }

    public static char getFormattingCode(EnumChatFormatting formatting)
    {
        return formatting.toString().charAt(1);
    }

    public static boolean isFormattingColorCode(char code)
    {
        // EnumChatFormatting color codes: [0-9A-F]
        return Character.digit(code, 16) != -1;
    }

    public static char getFirstColorCode(String s)
    {
        for (int i = s.indexOf("§"); i < s.length(); i = s.indexOf("§", i + 1))
        {
            if (i == -1 || i == s.length() - 1) // no section sign or last char in string
            {
                break;
            }

            char code = s.charAt(i + 1);

            if (isFormattingColorCode(code))
            {
                return code;
            }
        }
        return 'f'; // default (white)
    }

    public static char getNearestColorCode(String str, String targetStr)
    {
        for (int i = str.lastIndexOf("§", str.indexOf(targetStr)); i >= 0; i = str.lastIndexOf("§", i - 1))
        {
            char code = str.charAt(i + 1);

            if (isFormattingColorCode(code))
            {
                return code;
            }
        }
        return 'f'; // default (white)
    }

//    public static EnumChatFormatting getHealthColor(float healthPercent)
//    {
//        return healthPercent >= 75 ? EnumChatFormatting.DARK_GREEN
//                : healthPercent >= 50 ? EnumChatFormatting.GREEN
//                : healthPercent >= 25 ? EnumChatFormatting.RED
//                : EnumChatFormatting.DARK_RED;
//    }

    public static float halfRound(float num)
    {
        return Math.round(num * 2) / 2.0F; // round to nearest 0.5
    }

    public static Vec3 multiplyVec(Vec3 vec, double d)
    {
        return new Vec3(vec.xCoord * d, vec.yCoord * d, vec.zCoord * d);
    }

//    public static String getScoreboardHeader(Scoreboard sb)
//    {
//        if (sb == null || sb.getObjectiveInDisplaySlot(1) == null)
//        {
//            return "";
//        }
//
//        String header = sb.getObjectiveInDisplaySlot(1).getDisplayName();
//        return header != null ? EnumChatFormatting.getTextWithoutFormattingCodes(header) : "";
//    }
//
//    public static String getScoreboardScoreAtIndex(Scoreboard sb, boolean removeFormatting, int index)
//    {
//        List<String> cleanScores = getScoreboardScores(sb, removeFormatting);
//        return (index < cleanScores.size()) ? cleanScores.get(index) : null;
//    }
//
//    public static List<String> getScoreboardScores(Scoreboard sb, boolean removeFormatting)
//    {
//        if (sb == null || sb.getObjectiveInDisplaySlot(1) == null)
//        {
//            return Collections.emptyList();
//        }
//
//        return new ArrayList<>(sb.getScores()).stream()
//                .sorted(Comparator.comparingInt(score -> -score.getScorePoints())) // fix sidebar scores stored in reverse order
//                .filter(score -> score.getObjective().getName().equals(sb.getObjectiveInDisplaySlot(1).getName())) // sidebar score name = sidebar header name?
//                .map(score -> getScoreText(sb, score, removeFormatting)) // get text from each score
//                .collect(Collectors.toList());
//    }
//
//    private static String getScoreText(Scoreboard sb, Score score, boolean removeFormatting)
//    {
//        ScorePlayerTeam spt = sb.getPlayersTeam(score.getPlayerName()); // getPlayerName: hexadecimal string? (hypixel specific)
//
//        if (spt == null)
//        {
//            return "";
//        }
//
//        // If score text is <16 char, text stored in getColorPrefix; possible overflow (>16) stored in getColorSuffix (hypixel specific)
//        String scoreText = spt.formatString("");
//        return removeFormatting ? EnumChatFormatting.getTextWithoutFormattingCodes(scoreText) : scoreText;
//    }

////    World.java -> checkBlockCollision(AxisAlignedBB bb)
//    public static BlockPos isBlockCollision(WorldClient world, AxisAlignedBB bb)
//    {
//        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
//
//        for (int x = MathHelper.floor_double(bb.minX); x <= MathHelper.floor_double(bb.maxX); ++x)
//        {
//            for (int y = MathHelper.floor_double(bb.minY); y <= MathHelper.floor_double(bb.maxY); ++y)
//            {
//                for (int z = MathHelper.floor_double(bb.minZ); z <= MathHelper.floor_double(bb.maxZ); ++z)
//                {
//                    Block block = world.getBlockState(mutableBlockPos.set(x, y, z)).getBlock();
//                    BlockPos blockPos = new BlockPos(x, y, z);
//
//                    if (!block.isAir(world, blockPos) && block.getSelectedBoundingBox(world, blockPos).intersectsWith(bb))
//                    {
//                        return blockPos;
//                    }
//                }
//            }
//        }
//        return null;
//    }
}

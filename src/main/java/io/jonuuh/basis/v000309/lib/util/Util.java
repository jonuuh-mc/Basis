package io.jonuuh.basis.v000309.lib.util;

import net.minecraft.util.EnumChatFormatting;

public final class Util
{
    /** Prevents instantiation */
    private Util()
    {
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
}

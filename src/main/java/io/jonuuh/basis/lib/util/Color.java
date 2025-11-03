package io.jonuuh.basis.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a color with alpha, red, green, and blue components.
 * <p>
 * The components are 'literally' stored as signed bytes [-128, 127], but should be 'interpreted' as unsigned bytes [0, 255] (e.g. use glColor4ub, not glColor4b)
 * <p>
 * This class provides various constructors and methods for creating, manipulating, and converting colors to and from different formats
 * <p>
 * Various equivalent formats example:
 * <pre>
 * {@code
 *       A         R         G         B
 * -----------------------------------------
 * |   -99,  |   -57,  |   -81,  |   -28   | Byte values of color components [-128, 127]
 * |   157,  |   199,  |   175,  |   228   | Bytes mapped to unsigned ints [0-255]
 * |    9d,  |    c7,  |    af,  |    e4   | Paired hex color components
 * |  9   d  |  c   7  |  a   f  |  e   4  | Separated hex color components
 * |1001 1101|1100 0111|1010 1111|1110 0100| Binary value of packed ARGB int
 * -----------------------------------------
 * |            -1,647,857,692             | Packed ARGB as int
 * |             2,647,109,604             | Packed ARGB as long
 * |               #9dc7afe4               | Hex string
 * -----------------------------------------
 * }
 * </pre>
 */
public class Color
{
    // EnumChatFormatting Colors
    public static final Color BLACK = new Color("#000000");
    public static final Color DARK_BLUE = new Color("#0000AA");
    public static final Color DARK_GREEN = new Color("#00AA00");
    public static final Color DARK_AQUA = new Color("#00AAAA");
    public static final Color DARK_RED = new Color("#AA0000");
    public static final Color DARK_PURPLE = new Color("#AA00AA");
    public static final Color GOLD = new Color("#FFAA00");
    public static final Color GRAY = new Color("#AAAAAA");
    public static final Color DARK_GRAY = new Color("#555555");
    public static final Color BLUE = new Color("#5555FF");
    public static final Color GREEN = new Color("#55FF55");
    public static final Color AQUA = new Color("#55FFFF");
    public static final Color RED = new Color("#FF5555");
    public static final Color LIGHT_PURPLE = new Color("#FF55FF");
    public static final Color YELLOW = new Color("#FFFF55");
    public static final Color WHITE = new Color("#FFFFFF");

    public static final Color TRANSPARENT = new Color("#FFFFFF", 0.0F);

    /** The max value a color component can hold as an int (255) */
    public static final int MAX_COMPONENT_INT = 0xFF;
    /** The max value a color component can hold as a byte (-1) */
    public static final byte MAX_COMPONENT_BYTE = (byte) MAX_COMPONENT_INT;

    /** A byte (signed [-128, 127]) representing this color's alpha (opacity) value */
    public final byte a;
    /** A byte (signed [-128, 127]) representing this color's red value */
    public final byte r;
    /** A byte (signed [-128, 127]) representing this color's green value */
    public final byte g;
    /** A byte (signed [-128, 127]) representing this color's blue value */
    public final byte b;

    /**
     * Creates a color with the given red, green, blue, and alpha values.
     *
     * @param a {@link Color#a}
     * @param r {@link Color#r}
     * @param g {@link Color#g}
     * @param b {@link Color#b}
     */
    public Color(byte a, byte r, byte g, byte b)
    {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Creates a color with the given red, green, blue, and alpha values.
     *
     * @param a An alpha value in the range [0, 255]
     * @param r A red value in the range [0, 255]
     * @param g A green value in the range [0, 255]
     * @param b A blue value in the range [0, 255]
     */
    public Color(int a, int r, int g, int b)
    {
        this.a = (byte) a;
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
    }

    /**
     * Creates a full opacity color with the given red, green, and blue values.
     *
     * @param r A red value in the range [0, 255]
     * @param g A green value in the range [0, 255]
     * @param b A blue value in the range [0, 255]
     */
    public Color(int r, int g, int b)
    {
        this(MAX_COMPONENT_INT, r, g, b);
    }

    /**
     * Creates a color with the given red, green, blue, and alpha values.
     *
     * @param a An alpha value normalized in the range [0.0, 1.0]
     * @param r A red value normalized in the range [0.0, 1.0]
     * @param g A green value normalized in the range [0.0, 1.0]
     * @param b A blue value normalized in the range [0.0, 1.0]
     */
    public Color(float a, float r, float g, float b)
    {
        this((byte) (MAX_COMPONENT_INT * a), (byte) (MAX_COMPONENT_INT * r), (byte) (MAX_COMPONENT_INT * g), (byte) (MAX_COMPONENT_INT * b));
    }

    /**
     * Creates a full opacity color with the given red, green, and blue values.
     *
     * @param r A red value normalized in the range [0.0, 1.0]
     * @param g A green value normalized in the range [0.0, 1.0]
     * @param b A blue value normalized in the range [0.0, 1.0]
     */
    public Color(float r, float g, float b)
    {
        this(1.0F, r, g, b);
    }

    /**
     * Creates a color from a packed ARGB integer.
     *
     * @param argb A packed ARGB integer
     */
    public Color(int argb)
    {
        this.a = (byte) ((argb >> 24) & 0xFF);
        this.r = (byte) ((argb >> 16) & 0xFF);
        this.g = (byte) ((argb >> 8) & 0xFF);
        this.b = (byte) (argb & 0xFF);
    }

    /**
     * Creates a color from a hexadecimal string, where pairs of characters each represent a component of a color.
     * <p>
     * The string is case-insensitive and may be prefixed by '#' or "0x"
     * <p>
     * While the hexStr length is less than 8, 'f' will be prepended, shifting any given chars down.
     *
     * @param hexStr An RGB or ARGB hex color string.
     */
    public Color(String hexStr)
    {
        hexStr = trimPrefixes(hexStr);

        // Prepend a default char until expected length
        StringBuilder sb = new StringBuilder(hexStr);
        while (sb.length() < 8)
        {
            sb.insert(0, 'f');
        }
        hexStr = sb.toString();

        int[] argb = new int[4];
        for (int i = 0; i < 4; i++)
        {
            argb[i] = Integer.valueOf(hexStr.substring(i * 2, (i * 2) + 2), 16);
        }

        this.a = (byte) argb[0];
        this.r = (byte) argb[1];
        this.g = (byte) argb[2];
        this.b = (byte) argb[3];
    }

    /**
     * Creates a color from an RGB hexadecimal string and an opacity, where pairs of characters in the string represent components of a color
     * <p>
     * The string is case-insensitive and may be prefixed by '#' or "0x"
     *
     * @param rgbHexStr An RGB hex color string
     * @param a An alpha value normalized in the range [0.0, 1.0]
     */
    public Color(String rgbHexStr, float a)
    {
        this(Integer.toHexString((int) (MAX_COMPONENT_INT * a)) + trimPrefixes(rgbHexStr));
    }

    /**
     * Creates a color from an {@link EnumChatFormatting} color.
     *
     * @param chatFormatting An EnumChatFormatting color
     */
    public Color(EnumChatFormatting chatFormatting)
    {
        char colorChar = chatFormatting.isColor() ? chatFormatting.toString().charAt(1) : 'f';
        int packedRGB = Minecraft.getMinecraft().fontRendererObj.getColorCode(colorChar);

        // Unpack
        this.a = MAX_COMPONENT_BYTE;
        this.r = (byte) ((packedRGB >> 16) & 0xFF);
        this.g = (byte) ((packedRGB >> 8) & 0xFF);
        this.b = (byte) (packedRGB & 0xFF);
    }

    /**
     * Creates a default color (full opacity white).
     */
    public Color()
    {
        this(MAX_COMPONENT_BYTE, MAX_COMPONENT_BYTE, MAX_COMPONENT_BYTE, MAX_COMPONENT_BYTE);
    }

    public Color addA(float a)
    {
        float alpha = (float) MathUtils.clamp(getAAsFloat() + a);
        return new Color(alpha, getRAsFloat(), getGAsFloat(), getBAsFloat());
    }

    public Color addR(float r)
    {
        float red = (float) MathUtils.clamp(getRAsFloat() + r);
        return new Color(getAAsFloat(), red, getGAsFloat(), getBAsFloat());
    }

    public Color addG(float g)
    {
        float green = (float) MathUtils.clamp(getGAsFloat() + g);
        return new Color(getAAsFloat(), getRAsFloat(), green, getBAsFloat());
    }

    public Color addB(float b)
    {
        float blue = (float) MathUtils.clamp(getBAsFloat() + b);
        return new Color(getAAsFloat(), getRAsFloat(), getGAsFloat(), blue);
    }

    public float getAAsFloat()
    {
        return toFloat(a);
    }

    public float getRAsFloat()
    {
        return toFloat(r);
    }

    public float getGAsFloat()
    {
        return toFloat(g);
    }

    public float getBAsFloat()
    {
        return toFloat(b);
    }

    /**
     * Gets this color's red component as an unsigned int
     *
     * @see Byte#toUnsignedInt(byte)
     */
    public int getRAsInt()
    {
        return toInt(r);
    }

    /**
     * Gets this color's green component as an unsigned int
     *
     * @see Byte#toUnsignedInt(byte)
     */
    public int getGAsInt()
    {
        return toInt(g);
    }

    /**
     * Gets this color's blue component as an unsigned int
     *
     * @see Byte#toUnsignedInt(byte)
     */
    public int getBAsInt()
    {
        return toInt(b);
    }

    /**
     * Gets this color's alpha component as an unsigned int
     *
     * @see Byte#toUnsignedInt(byte)
     */
    public int getAAsInt()
    {
        return toInt(a);
    }

    /**
     * Get the value of this color as a packed ARGB integer
     * <p>
     * Packed int layout: {@code [Alpha byte][Red byte][Green byte][Blue byte]}
     */
    public int toPackedARGB()
    {
        return (getAAsInt() << 24) | (getRAsInt() << 16) | (getGAsInt() << 8) | (getBAsInt());
    }

    /**
     * Get the value of this color as an RGB hex string
     */
    public String toRGBHex()
    {
        return byteToHexStr(r) + byteToHexStr(g) + byteToHexStr(b);
    }

    /**
     * Get the value of this color as an ARGB hex string
     */
    public String toARGBHex()
    {
        return byteToHexStr(a) + byteToHexStr(r) + byteToHexStr(g) + byteToHexStr(b);
    }

    private String byteToHexStr(byte component)
    {
        return String.format("%02X", toInt(component));
    }

    /**
     * Performs {@code ((int) component) & 0xff} internally.
     * <p>
     * Also, IIRC casting to (int) is actually unnecessary but just clearer to what's happening in the conversion
     */
    private int toInt(byte colorComponentByte)
    {
        return Byte.toUnsignedInt(colorComponentByte);
    }

    private float toFloat(byte colorComponentByte)
    {
        return (colorComponentByte & MAX_COMPONENT_INT) / 255.0F;
    }

    /**
     * Trim '#' or "0x" hex string prefixes, if they exist
     */
    private static String trimPrefixes(String hexStr)
    {
        // TODO: only reason this is static is so it can be called within a this() constructor call
        return hexStr.charAt(0) == '#' ? hexStr.substring(1)
                : hexStr.startsWith("0x") ? hexStr.substring(2) : hexStr;
    }

    /**
     * Create and return a new color whose RGB values are copied from this color and scaled by a given percentage
     *
     * @param percentChange A percent change, e.g. -50 = 50% darker, 0 = no change, 100 = 100% brighter
     */
    public Color adjustBrightness(float percentChange)
    {
        float factor = 1 + (percentChange / 100);

        int scaledR = (int) MathUtils.clamp(getRAsInt() * factor, 0, 255);
        int scaledG = (int) MathUtils.clamp(getGAsInt() * factor, 0, 255);
        int scaledB = (int) MathUtils.clamp(getBAsInt() * factor, 0, 255);

        return new Color(a, scaledR, scaledG, scaledB);
    }

    /**
     * Creates and returns a color with random alpha, red, green, and blue values.
     */
    public static Color getRandomARGB()
    {
        long max = 4294967295L; // 0xFFFFFFFF
        long randomLong = ThreadLocalRandom.current().nextLong(max + 1);
        return new Color(Long.toHexString(randomLong));
    }

    /**
     * Creates and returns a full opacity color with random red, green, and blue values,
     */
    public static Color getRandomRGB()
    {
        return getRandomRGBWithOpacity(1.0F);
    }

    /**
     * Creates and returns a color with the given opacity and random red, green, and blue values,
     *
     * @param a An alpha value normalized in the range [0.0, 1.0]
     */
    public static Color getRandomRGBWithOpacity(float a)
    {
        String alphaStr = Integer.toHexString((int) (MAX_COMPONENT_INT * a));
        int randomInt = ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
        return new Color(alphaStr + Integer.toHexString(randomInt));
    }

    @Override
    public String toString()
    {
        return "#" + toARGBHex();
    }
}

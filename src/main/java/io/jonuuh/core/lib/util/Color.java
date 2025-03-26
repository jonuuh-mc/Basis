package io.jonuuh.core.lib.util;

import net.minecraft.util.EnumChatFormatting;

import java.util.concurrent.ThreadLocalRandom;

public class Color
{
    private float r;
    private float g;
    private float b;
    private float a;

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b)
    {
        this(r, g, b, 1.0F);
    }

    public Color()
    {
        this(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Color copy()
    {
        return new Color(this.r, this.g, this.b, this.a);
    }

    public Color(String hexColor)
    {
        float[] c = hexToColor(hexColor);

        this.r = c[0];
        this.g = c[1];
        this.b = c[2];
        this.a = 1.0F;
    }

    public Color(int decimalARGB)
    {
        String argbHex = Integer.toHexString(decimalARGB);

        float[] c = hexToColor(argbHex.substring(2));

        this.r = c[0];
        this.g = c[1];
        this.b = c[2];
        this.a = Integer.valueOf(argbHex.substring(0, 2), 16) / 255F;
    }

    public Color(EnumChatFormatting chatFormatting)
    {
        int packedColor = chatFormattingToPackedInt(chatFormatting);

        // unpack
        int r = (packedColor >> 16) & 0xFF;
        int g = (packedColor >> 8) & 0xFF;
        int b = packedColor & 0xFF;

        // normalize
        this.r = r / 255.0F;
        this.g = g / 255.0F;
        this.b = b / 255.0F;
        this.a = 1.0F;
    }

    public float getR()
    {
        return r;
    }

    public float getG()
    {
        return g;
    }

    public float getB()
    {
        return b;
    }

    public float getA()
    {
        return a;
    }

    public Color setR(float r)
    {
        this.r = r;
        return this;
    }

    public Color setG(float g)
    {
        this.g = g;
        return this;
    }

    public Color setB(float b)
    {
        this.b = b;
        return this;
    }

    public Color setA(float a)
    {
        this.a = a;
        return this;
    }

    // definitely not the right way to do this (relative luminance?) but should be good enough
    public Color adjustBrightness(float percentChange)
    {
        r = r + MathUtils.percentOf(r, percentChange);
        g = g + MathUtils.percentOf(g, percentChange);
        b = b + MathUtils.percentOf(b, percentChange);
        return this;
    }

    public int toDecimalARGB()
    {
        String s = Integer.toHexString((int) (a * 255)) +
                Integer.toHexString((int) (r * 255)) +
                Integer.toHexString((int) (g * 255)) +
                Integer.toHexString((int) (b * 255));

        // TODO: why is this using parseLong instead of parseInt again?
        return (int) Long.parseLong(s, 16);
    }

    public String toHex()
    {
        return Integer.toHexString((int) (r * 255)) +
                Integer.toHexString((int) (g * 255)) +
                Integer.toHexString((int) (b * 255));
    }

    private float[] hexToColor(String hexColor)
    {
        int[] rgb = new int[]{255, 255, 255};

        if (hexColor.charAt(0) == '#')
        {
            hexColor = hexColor.substring(1);
        }

        if (hexColor.length() != 6)
        {
            return normalizeRGB(rgb);
        }

        for (int i = 0; i < 3; i++)
        {
            rgb[i] = Integer.valueOf(hexColor.substring(i * 2, (i * 2) + 2), 16);
        }

        return normalizeRGB(rgb);
    }

    private float[] normalizeRGB(int[] rgb)
    {
        return new float[]{rgb[0] / 255.0F, rgb[1] / 255.0F, rgb[2] / 255.0F};
    }

    public static Color getRandom(float a)
    {
        int randomHexInt = ThreadLocalRandom.current().nextInt(0xFFFFFF + 1);
        Color color = new Color(Integer.toHexString(randomHexInt));
        return a == 1.0F ? color : color.setA(a);
    }

    public static Color getRandom()
    {
        return getRandom(1.0F);
    }

    public static int chatFormattingToPackedInt(EnumChatFormatting chatFormatting)
    {
        return colorCodes[Character.digit(chatFormatting.toString().charAt(1), 16)];
    }

    public int[] getChatFormattingColorCodes()
    {
        return colorCodes.clone();
    }

    /**
     * Array of packed RGB triplet integers defining 16 standard chat colors <p>
     * ex: 0xFF00AA -> FF (R), 00 (G), AA (B) -> packed as decimals from 0-255 into each section of 8 bits <p>
     * 32 bit int -> [empty 8 bits][R 8 bits][G 8 bits][B 8 bits]
     *
     * @see net.minecraft.client.gui.FontRenderer#FontRenderer(net.minecraft.client.settings.GameSettings, net.minecraft.util.ResourceLocation, net.minecraft.client.renderer.texture.TextureManager, boolean)
     */
    private static final int[] colorCodes = new int[16];

    // init color code packed integers for 16 default EnumChatFormatting colors
    static
    {
        for (int i = 0; i < 16; ++i)
        {
            // magic
            int oneThirdBrightness = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + oneThirdBrightness;
            int green = (i >> 1 & 1) * 170 + oneThirdBrightness;
            int blue = (i & 1) * 170 + oneThirdBrightness;

            // edge case for gold color
            if (i == 6)
            {
                red += 85;
            }

            // combine RGB components
            colorCodes[i] = (red & 255) << 16 | (green & 255) << 8 | (blue & 255);
        }
    }

    @Override
    public String toString()
    {
        return "Color{" + toHex() + "}";
    }

//    @Override
//    public String toString()
//    {
//        return "Color{" + "r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
//    }
}

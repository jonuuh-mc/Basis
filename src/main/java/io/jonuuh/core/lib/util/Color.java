package io.jonuuh.core.lib.util;

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

    public Color()
    {
        this(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Color(float rgb, float a)
    {
        this(rgb, rgb, rgb, a);
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

//        this(Integer.toHexString(argbDecimal).substring(2));
//        this.a = Integer.valueOf(Integer.toHexString(argbDecimal).substring(0, 2), 16) / 255F;
    }

    public long toDecimalARGB()
    {
        String s = "";

        s = s + Integer.toHexString((int) (a * 255));
        s = s + Integer.toHexString((int) (r * 255));
        s = s + Integer.toHexString((int) (g * 255));
        s = s + Integer.toHexString((int) (b * 255));

        return (int) Long.parseLong(s, 16);
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

    @Override
    public String toString()
    {
        return "Color{" + "r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
    }
}

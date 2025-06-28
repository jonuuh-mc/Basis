package io.jonuuh.core.lib.util;

import net.minecraft.util.Vec3;

public final class MathUtils
{
    /** Prevents instantiation */
    private MathUtils()
    {
    }

    public static float halfRound(float num)
    {
        return Math.round(num * 2) / 2.0F; // round to nearest 0.5
    }

    public static float percentOf(float f, float percent)
    {
        return (f / 100) * percent;
    }

    public static Vec3 multiplyVec(Vec3 vec, double d)
    {
        return new Vec3(vec.xCoord * d, vec.yCoord * d, vec.zCoord * d);
    }

    public static double normalize(double value, double min, double max)
    {
        return (value - min) / (max - min);
    }

    public static double denormalize(double normalizedValue, double min, double max)
    {
        return normalizedValue * (max - min) + min;
    }

    public static double clamp(double value)
    {
        // Clamp value (0, 1)
        return clamp(value, 0.0, 1.0);
    }

    public static double clamp(double value, double min, double max)
    {
        // Clamp value (min, max)
        return Math.min((Math.max(value, min)), max);
    }

    public static int randomIntInclusiveRange(int min, int max)
    {
        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }
}

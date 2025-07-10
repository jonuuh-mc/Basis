package io.jonuuh.basis.lib.util;

import net.minecraft.util.Vec3;

public final class MathUtils
{
    /** Prevents instantiation */
    private MathUtils()
    {
    }

    /**
     * Rounds the given float to the nearest 0.5
     *
     * @param num The float
     * @return The rounded float
     */
    public static float halfRound(float num)
    {
        return Math.round(num * 2) / 2.0F;
    }

    /**
     * Calculates "What is {@code percent}% of {@code f}"
     *
     * @param f A number
     * @param percent The percentage of {@code f} to return
     * @return {@code percent}% of {@code f}
     */
    public static float percentOf(float f, float percent)
    {
        return (f / 100) * percent;
    }

    /**
     * Multiplies all components of the vector {@code vec} by {@code d}, returning a new vector
     *
     * @param vec The vector
     * @param d The multiplication factor
     * @return A multiplied vector
     */
    public static Vec3 multiplyVec(Vec3 vec, double d)
    {
        return new Vec3(vec.xCoord * d, vec.yCoord * d, vec.zCoord * d);
    }

    /**
     * Normalize a value to the inclusive range of [min, max].
     * <ul>
     * <li>If the value is equal to min, the returned number is 0.0
     * <li>If the value is equal to max, the returned number is 1.0
     * <li>If the value is between min and max, the returned number is between 0.0 and 1.0, relative to it's position in the range
     * </ul>
     *
     * @param value The value to be normalized
     * @param min The minimum value for the range
     * @param max The maximum value for the range
     * @return The normalized value [0.0 - 1.0]
     */
    public static double normalize(double value, double min, double max)
    {
        return (value - min) / (max - min);
    }

    /**
     * Denormalize a value to the inclusive range of [min, max].
     * <ul>
     * <li>If the value is equal to 0.0, the returned number is min
     * <li>If the value is equal to 1.0, the returned number is max
     * <li>If the value is between 0.0 and 1.0, the returned number is between min and max, relative to it's position in the range
     * </ul>
     *
     * @param normalizedValue A value normalized in the inclusive range of [0.0 - 1.0]
     * @param min The minimum value for the range
     * @param max The maximum value for the range
     * @return The denormalized value [min - max]
     */
    public static double denormalize(double normalizedValue, double min, double max)
    {
        return normalizedValue * (max - min) + min;
    }

    /**
     * Clamps a value between 0.0 and 1.0, inclusive
     *
     * @param value The value to be clamped
     * @return The clamped value
     */
    public static double clamp(double value)
    {
        return clamp(value, 0.0, 1.0);
    }

    /**
     * Clamps a value between min and max, inclusive
     *
     * @param value The value to be clamped
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static double clamp(double value, double min, double max)
    {
        return Math.min((Math.max(value, min)), max);
    }

    /**
     * Generate a random integer within or equal to the bounds given by [min, max].
     *
     * @param min The minimum value the integer can be, inclusive
     * @param max The maximum value the integer can be, inclusive
     * @return The random integer
     */
    public static int randomIntInclusiveRange(int min, int max)
    {
        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }
}

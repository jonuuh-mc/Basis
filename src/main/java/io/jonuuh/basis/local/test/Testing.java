package io.jonuuh.basis.local.test;

import io.jonuuh.basis.lib.util.Color;

public class Testing
{
    public static void main(String[] args)
    {
        Color c = new Color("FFAA00");
        System.out.println(c + " " + c.toRGBHex());

        byte maxByte = -1;
        byte zeroByte = 0;
        System.out.println(Integer.toHexString(toInt(maxByte)));
        System.out.println(Integer.toHexString(toInt(zeroByte)));
    }

    private static int toInt(byte colorComponentByte)
    {
        return Byte.toUnsignedInt(colorComponentByte);
    }
}

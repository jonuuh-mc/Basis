package io.jonuuh.basis.v000309.local;

import io.jonuuh.basis.v000309.lib.util.Color;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class Events
{
    private final KeyBinding keyBinding;

    public Events(KeyBinding keyBinding)
    {
        this.keyBinding = keyBinding;
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event)
    {
        if (keyBinding.isPressed())
        {
            System.out.println("TEST");

            Color darkRed = new Color(EnumChatFormatting.DARK_RED);
            System.out.println(darkRed + " " + darkRed.toRGBHex() + " " + darkRed.addR(-0.5F));
        }
    }
}

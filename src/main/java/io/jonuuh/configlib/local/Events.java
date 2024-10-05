package io.jonuuh.configlib.local;

import net.minecraft.client.settings.KeyBinding;
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
            //
        }
    }

//    @SubscribeEvent
//    public void onClientChatReceived(ClientChatReceivedEvent event)
//    {
//        String msg = event.message.getUnformattedText();
//    }
//
//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.START)
//        {
//            return;
//        }
//
//        //
//    }
}

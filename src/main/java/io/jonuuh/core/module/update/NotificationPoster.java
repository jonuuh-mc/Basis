package io.jonuuh.core.module.update;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class NotificationPoster
{
    private static NotificationPoster instance;

    public static NotificationPoster createInstance()
    {
        if (instance == null)
        {
            instance = new NotificationPoster();
            return instance;
        }
        else
        {
            throw new IllegalStateException("NotificationPoster instance has already been created");
        }
    }

    private NotificationPoster()
    {
        System.out.println("Elapsed: " + TimeUnit.MILLISECONDS.toDays((System.currentTimeMillis() - 1727323753160L)));
    }

    private String readResourceAsStr(String path) throws IOException
    {
        try
        {
            URL url = this.getClass().getClassLoader().getResource(path); // "/assets/last-notification-post.txt"
            byte[] fileBytes = Files.readAllBytes(Paths.get(url.toString()));
            return new String(fileBytes, StandardCharsets.UTF_8);
        }
        catch (IOException | NullPointerException e) 
        {
            e.printStackTrace();
            return "";
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity != Minecraft.getMinecraft().thePlayer)
        {
            return;
        }

//        Calendar today = Calendar.getInstance();
//        today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds
//
//        System.out.println(today.toInstant());

//        int days = Duration.between(calendar1.toInstant(), calendar2.toInstant()).toDays();


        System.out.println("HELLO FROM onEntityJoinWorld");
        MinecraftForge.EVENT_BUS.unregister(instance);
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("First world join of session"));
    }
}


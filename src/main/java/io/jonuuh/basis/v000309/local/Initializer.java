package io.jonuuh.basis.v000309.local;

import io.jonuuh.basis.v000309.lib.util.StaticAssetRequestFinishedEvent;
import io.jonuuh.basis.v000309.lib.util.StaticAssetRequester;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Initializer
{
    private final String modID;
    private final String version;
    private boolean started;

    public Initializer(String modID, String version)
    {
        this.modID = modID;
        this.version = version;
    }

    // TODO: proably none of this really needs to be multithreaded;
    //  maybe set a timeout limit for api call instead? just take the thread blocking instead of asking for trouble when you dont know how to multithread
    //  test out things with wifi off? assetFinishedEvent asset should just be null due to error catching
    //  also need to test out api response when ratelimit is hit
    public void start()
    {
        if (!started)
        {
            System.out.println("starting asset req");
            new Thread(new StaticAssetRequester(modID + ".json")).start();
            started = true;
        }
    }

    @SubscribeEvent
    public void onAssetReqFinished(StaticAssetRequestFinishedEvent event)
    {
//        String asset = event.asset;
//        System.out.println("asset req finished: " + asset);
//
//        // TODO: pass asset in here
////        UpdateHandler.createInstance(modID, version);
////        UpdateHandler.INSTANCE.start();
//
//        // TODO: is it okay for all this to be in here, dependant on github api call finish?
//        //  try registering a keybind on some other key press for example (arbitrary time) - does it show up in controls menu?
//        //  same for something like creating the gui and command to open the gui?
//        KeyBinding keyBinding = new KeyBinding("<description>", Keyboard.KEY_NONE, modID);
//        ClientRegistry.registerKeyBinding(keyBinding);
//        MinecraftForge.EVENT_BUS.register(new Events(keyBinding));
//
//        BaseGuiScreen guiScreen = new SettingsGuiScreen(SettingsConfigurationAdapter.INSTANCE.getDefaultCategorySettings());
//        ClientCommandHandler.instance.registerCommand(new CommandOpenSettingsGui(modID, guiScreen));
    }
}

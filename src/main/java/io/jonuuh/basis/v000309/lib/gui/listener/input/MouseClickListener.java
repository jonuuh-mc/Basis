package io.jonuuh.basis.v000309.lib.gui.listener.input;

import io.jonuuh.basis.v000309.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.v000309.lib.gui.event.input.MouseUpEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

// TODO: could look into splitting this into LeftClickListener, RightClickListener, and MiddleClickListener
//  probably wouldn't be able to make a superclass interface as that would only allow one implementation each interface
//  e.g isMouseDown() for example would need more specific methods like isLeftMouseDown(), onLeftMouseDown(), etc
public interface MouseClickListener extends InputListener
{
    /** A resource pointing to the default mouse down sound */
    ResourceLocation resourceClickSound = new ResourceLocation("basis:click");

    /**
     * @return Whether the mouse is currently pressed down on this element (!= hovered, is set true on mouse down and false on mouse release
     */
    boolean isMouseDown();

    void setMouseDown(boolean mouseDown);

    default void onMouseDown(MouseDownEvent event)
    {
        setMouseDown(true);

        if (event.target == this)
        {
            // TODO: find a better solution than this call to Minecraft.getMinecraft()
            playClickSound(Minecraft.getMinecraft().getSoundHandler());
        }
    }

    default void onMouseUp(MouseUpEvent event)
    {
        setMouseDown(false);
    }

    default void playClickSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.create(resourceClickSound, 2.0F));
    }
}

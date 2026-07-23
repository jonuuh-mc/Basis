package io.jonuuh.basis.lib.gui.element.behavior;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseUpEvent;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class ClickBehavior
{
    private static final ResourceLocation CLICK_SOUND = new ResourceLocation("basis:click");

    private final GuiElement host;
    private boolean mouseDown;

    public ClickBehavior(GuiElement owner)
    {
        this.host = owner;

        host.addEventListener(MouseDownEvent.class, this::onMouseDown);
        host.addEventListener(MouseUpEvent.class, this::onMouseUp);
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    private void onMouseDown(MouseDownEvent event)
    {
        mouseDown = true;

        if (event.target == host)
        {
            GuiElement.mc.getSoundHandler().playSound(PositionedSoundRecord.create(CLICK_SOUND, 2.0F));
        }
    }

    private void onMouseUp(MouseUpEvent event)
    {
        mouseDown = false;
    }
}
package io.jonuuh.core.lib.gui.listener.lifecycle;

import io.jonuuh.core.lib.gui.event.lifecycle.ScreenTickEvent;

public interface ScreenTickListener
{
//    int getHoverTime();
//
//    void setHoverTime(int hoverTime);

    void onScreenTick(ScreenTickEvent event);

//    // TODO: something like this for tooltips?
//    if (hovered && getHoverTime() != 20)
//    {
//        setHoverTime(20)
//    }
//    else if (getHoverTime() > 0)
//    {
//        setHoverTime(getHoverTime() - 1);
//    }
}

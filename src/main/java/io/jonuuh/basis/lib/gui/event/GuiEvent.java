package io.jonuuh.basis.lib.gui.event;

import io.jonuuh.basis.lib.gui.element.GuiElement;

public abstract class GuiEvent
{
    GuiElement currentTarget;
    //    private final boolean cancelable;
    private boolean propagationStopped;
    private boolean immediatePropagationStopped;
//    private boolean defaultPrevented;

    protected GuiEvent(boolean cancelable)
    {
//        this.cancelable = cancelable;
    }

    public GuiElement getCurrentTarget()
    {
        return currentTarget;
    }

    /**
     * Stop propagation from element to element
     */
    public void stopPropagation()
    {
        propagationStopped = true;
    }

    /**
     * Stop propagation within one element, e.g. propagation through an element's listeners for one event
     */
    public void stopImmediatePropagation()
    {
        immediatePropagationStopped = true;
        propagationStopped = true;
    }

    public boolean isPropagationStopped()
    {
        return propagationStopped;
    }

    boolean isImmediatePropagationStopped()
    {
        return immediatePropagationStopped;
    }

//    public void preventDefault()
//    {
//        if (cancelable)
//        {
//            defaultPrevented = true;
//        }
//    }
//
//    public boolean isDefaultPrevented()
//    {
//        return defaultPrevented;
//    }
}
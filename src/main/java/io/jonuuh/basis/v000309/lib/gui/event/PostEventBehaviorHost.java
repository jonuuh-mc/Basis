package io.jonuuh.basis.v000309.lib.gui.event;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;

import java.util.Map;
import java.util.function.Consumer;

public interface PostEventBehaviorHost
{
    Map<Class<? extends GuiEvent>, Consumer<GuiElement>> getPostEventBehaviors();

    default void assignPostEventBehavior(Class<? extends GuiEvent> eventType, Consumer<GuiElement> behavior)
    {
        getPostEventBehaviors().put(eventType, behavior);
    }

    default void tryApplyPostEventBehavior(Class<? extends GuiEvent> eventType)
    {
        Consumer<GuiElement> behavior = getPostEventBehaviors().get(eventType);
        if (behavior != null)
        {
            // TODO: unsafe cast but should be okay assuming this interface is never implemented on something other than a GuiElement
            behavior.accept((GuiElement) this);
        }
    }
}


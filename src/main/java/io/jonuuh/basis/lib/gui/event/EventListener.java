package io.jonuuh.basis.lib.gui.event;

@FunctionalInterface
public interface EventListener<T extends GuiEvent>
{
    void onEvent(T event);
}

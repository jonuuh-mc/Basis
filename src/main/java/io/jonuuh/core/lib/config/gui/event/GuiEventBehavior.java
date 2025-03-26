package io.jonuuh.core.lib.config.gui.event;

import io.jonuuh.core.lib.config.gui.elements.GuiElement;

/**
 * Represents a custom behavior for a gui event
 *
 * <p>This is a functional interface ({@link java.util.function})
 * whose functional method is {@link #apply(GuiElement)}
 *
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface GuiEventBehavior
{
    /**
     * Applies this behavior to the given element
     *
     * @param element the GuiElement
     * @return a boolean: whether to override the default event behavior
     */
    Boolean apply(GuiElement element);
}

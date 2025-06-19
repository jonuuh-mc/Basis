package io.jonuuh.core.lib.gui.element.button;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GuiButton extends GuiElement implements MouseClickListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    private boolean enabled;
    private boolean mouseDown;

    protected GuiButton(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.enabled = builder.enabled;
        this.postBehaviors = new HashMap<>();

        if (builder.mouseDownBehavior != null)
        {
            assignPostEventBehavior(MouseDownEvent.class, builder.mouseDownBehavior);
        }
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isMouseDown()
    {
        return mouseDown;
    }

    @Override
    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    @Override
    public Map<Class<? extends GuiEvent>, Consumer<GuiElement>> getPostEventBehaviors()
    {
        return postBehaviors;
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);
        tryApplyPostEventBehavior(event.getClass());
    }

    protected static abstract class AbstractBuilder<T extends GuiButton.AbstractBuilder<T, R>, R extends GuiButton> extends GuiElement.AbstractBuilder<T, R>
    {
        protected Consumer<GuiElement> mouseDownBehavior = null;
        protected boolean enabled = true;

        protected AbstractBuilder(String elementName)
        {
            super(elementName);
        }

        public T mouseDownBehavior(Consumer<GuiElement> mouseDownBehavior)
        {
            this.mouseDownBehavior = mouseDownBehavior;
            return self();
        }

        public T enabled(boolean enabled)
        {
            this.enabled = enabled;
            return self();
        }
    }
}


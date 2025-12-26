package io.jonuuh.basis.v000309.lib.gui.element.toggles;

import io.jonuuh.basis.v000309.lib.gui.element.GuiElement;
import io.jonuuh.basis.v000309.lib.gui.event.GuiEvent;
import io.jonuuh.basis.v000309.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.basis.v000309.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.v000309.lib.gui.listener.input.MouseClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GuiToggle extends GuiElement implements MouseClickListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    private boolean isToggled;
    private boolean enabled;
    private boolean mouseDown;

    protected GuiToggle(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.isToggled = builder.isToggled;
        this.enabled = builder.enabled;

        this.postBehaviors = new HashMap<>();

        if (builder.mouseDownBehavior != null)
        {
            assignPostEventBehavior(MouseDownEvent.class, builder.mouseDownBehavior);
        }
    }

    public boolean isToggled()
    {
        return isToggled;
    }

    public void setToggled(boolean isToggled)
    {
        this.isToggled = isToggled;
    }

    public void toggle()
    {
        setToggled(!isToggled());
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
        toggle();
        tryApplyPostEventBehavior(event.getClass());
    }

    protected static abstract class AbstractBuilder<T extends GuiToggle.AbstractBuilder<T, R>, R extends GuiToggle> extends GuiElement.AbstractBuilder<T, R>
    {
        protected Consumer<GuiElement> mouseDownBehavior = null;
        protected boolean isToggled = false;
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

        public T toggled(boolean toggled)
        {
            this.isToggled = toggled;
            return self();
        }

        public T enabled(boolean enabled)
        {
            this.enabled = enabled;
            return self();
        }
    }
}

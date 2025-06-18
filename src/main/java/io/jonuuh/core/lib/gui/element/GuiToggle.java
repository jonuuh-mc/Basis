package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GuiToggle extends GuiElement implements MouseClickListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    private boolean enabled;
    private boolean mouseDown;
    private boolean isToggled;

    protected GuiToggle(String elementName, float localXPos, float localYPos, float width, float height, boolean isToggled)
    {
        super(elementName, localXPos, localYPos, width, height);
        this.isToggled = isToggled;
        this.postBehaviors = new HashMap<>();
    }

    protected GuiToggle(String elementName, float localXPos, float localYPos, boolean isToggled)
    {
        this(elementName, localXPos, localYPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, isToggled);
    }

    protected GuiToggle(String elementName, float xPos, float yPos)
    {
        this(elementName, xPos, yPos, false);
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
}

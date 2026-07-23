package io.jonuuh.basis.lib.gui.element.toggles;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.behavior.ClickBehavior;
import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;

public abstract class GuiToggle extends GuiElement
{
    protected ClickBehavior clickBehavior;
    protected boolean isToggled;

    protected GuiToggle(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.clickBehavior = new ClickBehavior(this);
        this.addEventListener(MouseDownEvent.class, e -> toggle());
        this.isToggled = builder.isToggled;
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

    protected static abstract class AbstractBuilder<T extends GuiToggle.AbstractBuilder<T, R>, R extends GuiToggle> extends GuiElement.AbstractBuilder<T, R>
    {
        protected boolean isToggled = false;

        protected AbstractBuilder(String elementName)
        {
            super(elementName);
        }

        public T toggled(boolean toggled)
        {
            this.isToggled = toggled;
            return self();
        }
    }
}

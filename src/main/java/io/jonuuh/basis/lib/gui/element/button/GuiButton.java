package io.jonuuh.basis.lib.gui.element.button;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.behavior.ClickBehavior;

public abstract class GuiButton extends GuiElement
{
    protected ClickBehavior clickBehavior;

    protected GuiButton(AbstractBuilder<?, ?> builder)
    {
        super(builder);

        this.clickBehavior = new ClickBehavior(this);
    }

    public ClickBehavior getClickBehavior()
    {
        return clickBehavior;
    }

    protected static abstract class AbstractBuilder<T extends GuiButton.AbstractBuilder<T, R>, R extends GuiButton> extends GuiElement.AbstractBuilder<T, R>
    {
        protected AbstractBuilder(String elementName)
        {
            super(elementName);
        }
    }
}


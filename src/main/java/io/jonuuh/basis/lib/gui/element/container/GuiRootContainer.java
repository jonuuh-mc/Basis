package io.jonuuh.basis.lib.gui.element.container;

import io.jonuuh.basis.lib.gui.BaseGuiScreen;
import io.jonuuh.basis.lib.gui.event.lifecycle.InitGuiEvent;

public class GuiRootContainer extends GuiContainer
{
    /** The BaseGuiScreen containing this GuiRootContainer, should be a 1:1 relationship */
    public final BaseGuiScreen guiScreen;

    public GuiRootContainer(Builder builder)
    {
        super(builder);
        this.guiScreen = builder.guiScreen;
    }

    @Override
    public void setParent(GuiContainer parent)
    {
        throw new UnsupportedOperationException("Cannot set the parent of a GuiRootContainer.");
    }

    @Override
    protected boolean shouldScissor()
    {
        return false;
    }

    @Override
    public void onInitGui(InitGuiEvent event)
    {
        // This is essentially the furthest point upstream in the cascading
        // resizing and reorganization of elements in a FlexBehavior.
        // Without this, FlexBehaviors would do nothing - the resizing has to start somewhere.
        //
        // Also note that if the root container is not given a FlexBehavior,
        // all flexing containers downstream will have no effect for the same reason.
        setWidth(event.sr.getScaledWidth());
        setHeight(event.sr.getScaledHeight());
        // Now make the call to GuiContainer's onInitGui, which will call this
        // element's flexBehavior#updateItemsLayout(), and thus the cascade begins.
        super.onInitGui(event);
    }

    public static class Builder extends GuiContainer.AbstractBuilder<Builder, GuiRootContainer>
    {
        protected final BaseGuiScreen guiScreen;

        public Builder(BaseGuiScreen guiScreen)
        {
            super("ROOT");
            this.guiScreen = guiScreen;
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiRootContainer build()
        {
            return new GuiRootContainer(this);
        }
    }
}

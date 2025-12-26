package io.jonuuh.basis.v000309.lib.gui.element.container;

public class GuiBasicContainer extends GuiContainer
{
    public GuiBasicContainer(Builder builder)
    {
        super(builder);
    }

//    @Override
//    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
//    {
//        if (!isVisible())
//        {
//            return;
//        }
//        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), getColor(GuiColorType.BACKGROUND));
//    }

    public static class Builder extends GuiContainer.AbstractBuilder<Builder, GuiBasicContainer>
    {
        public Builder(String elementName)
        {
            super(elementName);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiBasicContainer build()
        {
            return new GuiBasicContainer(this);
        }
    }
}

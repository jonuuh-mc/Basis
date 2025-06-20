package io.jonuuh.core.lib.gui.element.container.flex;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.FlexBehavior;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexJustify;
import io.jonuuh.core.lib.gui.event.lifecycle.InitGuiEvent;
import io.jonuuh.core.lib.gui.listener.lifecycle.InitGuiListener;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GuiFlexContainer extends GuiContainer implements InitGuiListener
{
    private final FlexBehavior flexBehavior;

    protected GuiFlexContainer(AbstractBuilder<?, ?> builder)
    {
        super(builder);
        this.flexBehavior = new FlexBehavior(this);
        flexBehavior.setDirection(builder.direction).setJustifyContent(builder.justify).setAlignItems(builder.align);
        flexBehavior.addItems(builder.flexItems);
    }

    public GuiFlexContainer(Builder builder)
    {
        this((AbstractBuilder<?, ?>) builder);
    }

    // TODO: just for debugging and experimenting, probably should not be exposed
    public FlexBehavior getFlexBehavior()
    {
        return flexBehavior;
    }

    public FlexDirection getDirection()
    {
        return flexBehavior.getDirection();
    }

    public GuiFlexContainer setDirection(FlexDirection direction)
    {
        flexBehavior.setDirection(direction);
        return this;
    }

    public FlexJustify getJustifyContent()
    {
        return flexBehavior.getJustifyContent();
    }

    public GuiFlexContainer setJustifyContent(FlexJustify justifyContent)
    {
        flexBehavior.setJustifyContent(justifyContent);
        return this;
    }

    public FlexAlign getAlignItems()
    {
        return flexBehavior.getAlignItems();
    }

    public GuiFlexContainer setAlignItems(FlexAlign alignItems)
    {
        flexBehavior.setAlignItems(alignItems);
        return this;
    }

    /**
     * Retrieve the FlexItem wrapping the given GuiElement in this GuiFlexContainer
     */
    public FlexItem getFlexItem(GuiElement element)
    {
        return flexBehavior.getFlexItem(element);
    }

    public void addItem(FlexItem item)
    {
        flexBehavior.addItem(item);
    }

    public void addItems(Collection<FlexItem> items)
    {
        flexBehavior.addItems(items);
    }

    public void addItems(FlexItem... items)
    {
        flexBehavior.addItems(items);
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }

        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), getColor(GuiColorType.BACKGROUND));
//        RenderUtils.drawNineSliceTexturedRect(resourceGenericBackgroundTex,
//                worldXPos(), worldYPos(), zLevel - 90, getWidth(), getHeight(), 52, 52, 12, 12);

        if (debug)
        {
            drawArrow(getDirection(), new Color("FFD700"), 8);

            FlexDirection alignDir = (getDirection() == FlexDirection.ROW || getDirection() == FlexDirection.ROW_REVERSE) ? FlexDirection.COLUMN : FlexDirection.ROW;
            drawArrow(alignDir, new Color(EnumChatFormatting.WHITE), 5);

            String info = this.elementName;
//        String info = direction.toString().toLowerCase() + ", " + justifyContent.toString().toLowerCase() + ", " + alignItems.toString().toLowerCase();
            float textX = worldXPos() + (getWidth() / 4) - (mc.fontRendererObj.getStringWidth(info) / 2F);
            float textY = worldYPos() + getHeight() - mc.fontRendererObj.FONT_HEIGHT;
            mc.fontRendererObj.drawString(info, textX, textY, getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
        }

        super.onScreenDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onInitGui(InitGuiEvent event)
    {
        flexBehavior.updateItemsLayout();
    }

    // TODO: debug
    private void drawArrow(FlexDirection direction, Color color, float headSize)
    {
        float tailX = 0;
        float tailY = 0;
        float headX = 0;
        float headY = 0;
        float[] headEdge1 = new float[2];
        float[] headEdge2 = new float[2];

        switch (direction)
        {
            case ROW:
                tailY = headY = worldYPos() + (getHeight() / 2);
                tailX = worldXPos();
                headX = tailX + getWidth();
                headEdge1 = new float[]{headX - headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY + headSize};
                break;
            case ROW_REVERSE:
                tailY = headY = worldYPos() + (getHeight() / 2);
                headX = worldXPos();
                tailX = headX + getWidth();
                headEdge1 = new float[]{headX + headSize, headY + headSize};
                headEdge2 = new float[]{headX + headSize, headY - headSize};
                break;
            case COLUMN:
                tailX = headX = worldXPos() + (getWidth() / 2);
                tailY = worldYPos();
                headY = tailY + getHeight();
                headEdge1 = new float[]{headX + headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY - headSize};
                break;
            case COLUMN_REVERSE:
                tailX = headX = worldXPos() + (getWidth() / 2);
                headY = worldYPos();
                tailY = headY + getHeight();
                headEdge1 = new float[]{headX - 10, headY + 10};
                headEdge2 = new float[]{headX + 10, headY + 10};
                break;
        }

//        System.out.printf("{tX:%s, tY:%s}, {hX:%s, hY:%s}%n", arrowTailX, arrowTailY, arrowHeadX, arrowHeadY);
        RenderUtils.drawVertices(GL11.GL_POLYGON, new float[][]{{headX, headY}, headEdge1, headEdge2}, color);
        RenderUtils.drawVertices(GL11.GL_LINE_STRIP, new float[][]{{tailX, tailY}, {headX, headY}}, color);
    }

    protected static abstract class AbstractBuilder<T extends AbstractBuilder<T, R>, R extends GuiFlexContainer> extends GuiContainer.AbstractBuilder<T, R>
    {
        protected final List<FlexItem> flexItems = new ArrayList<>();
        protected FlexDirection direction = FlexDirection.ROW;
        protected FlexJustify justify = FlexJustify.START;
        protected FlexAlign align = FlexAlign.START;

        protected AbstractBuilder(String name)
        {
            super(name);
        }

        public T direction(FlexDirection direction)
        {
            this.direction = direction;
            return self();
        }

        public T justify(FlexJustify justify)
        {
            this.justify = justify;
            return self();
        }

        public T align(FlexAlign align)
        {
            this.align = align;
            return self();
        }

        public T item(FlexItem item)
        {
            this.flexItems.add(item);
            return self();
        }

        public T items(Collection<FlexItem> items)
        {
            this.flexItems.addAll(items);
            return self();
        }

        public T items(FlexItem... items)
        {
            this.flexItems.addAll(Arrays.asList(items));
            return self();
        }
    }

    public static class Builder extends AbstractBuilder<Builder, GuiFlexContainer>
    {
        public Builder(String name)
        {
            super(name);
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiFlexContainer build()
        {
            return new GuiFlexContainer(this);
        }
    }
}

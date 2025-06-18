package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.event.GuiEvent;
import io.jonuuh.core.lib.gui.event.PostEventBehaviorHost;
import io.jonuuh.core.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.core.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.RenderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GuiButton extends GuiElement implements MouseClickListener, PostEventBehaviorHost
{
    private final Map<Class<? extends GuiEvent>, Consumer<GuiElement>> postBehaviors;
    private boolean enabled;
    private boolean mouseDown;
    private String buttonLabel;

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, String buttonLabel, Consumer<GuiElement> mouseDownBehavior)
    {
        super(elementName, xPos, yPos, width, height);
        this.buttonLabel = buttonLabel;
        this.postBehaviors = new HashMap<>();
        assignPostEventBehavior(MouseDownEvent.class, mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, Consumer<GuiElement> mouseDownBehavior)
    {
        this(elementName, xPos, yPos, width, height, "", mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, Consumer<GuiElement> mouseDownBehavior)
    {
        this(elementName, xPos, yPos, DEFAULT_WIDTH, DEFAULT_HEIGHT, "", mouseDownBehavior);
    }

    public String getButtonLabel()
    {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel)
    {
        this.buttonLabel = buttonLabel;
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
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), getColor(GuiColorType.BASE));

        String buttonText = RenderUtils.trimStringToWidthWithEllipsis(buttonLabel, (int) getWidth());

        mc.fontRendererObj.drawString(buttonText, worldXPos() + (getWidth() / 2) - ((float) mc.fontRendererObj.getStringWidth(buttonText) / 2),
                worldYPos() + (getHeight() / 2) - ((float) mc.fontRendererObj.FONT_HEIGHT / 2), getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);
        tryApplyPostEventBehavior(event.getClass());
    }
}

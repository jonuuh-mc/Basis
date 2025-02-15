package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

public abstract class GuiInteractableElement/*<T>*/ extends GuiElement
{
    protected final ResourceLocation resourceClickSound;

    protected boolean enabled;
    protected boolean focused;
    protected boolean mouseDown;

    protected GuiInteractableElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, true, tooltipStr);
        this.resourceClickSound = new ResourceLocation("core-config:click");

        this.enabled = true;
    }

//    public GuiInteractableElement(IInteractableElement parent, int id, int xPos, int yPos, String baseColorHex, int width, int height, String displayString)
//    {
//        this(parent, id, xPos, yPos, baseColorHex, width, height, displayString, "");
//    }

    protected GuiInteractableElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height)
    {
        this(parent, elementName, xPos, yPos, width, height, "");
    }

    protected GuiInteractableElement(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        this(parent, elementName, xPos, yPos, 200, 20);
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isFocused()
    {
        return focused;
    }

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    /**
     * Should run for every element when the left mouse is pressed down anywhere on screen,
     * onMousePress & onMouseRelease only run when mouse is actually pressed or released on an element
     */
    public boolean wasMousePressed(int mouseX, int mouseY)
    {
        return hovered && enabled && visible;
    }

    public void onMousePress(int mouseX, int mouseY)
    {
        mouseDown = true;

        if (!focused)
        {
            focused = true;
        }
    }

    public void onMouseRelease(int mouseX, int mouseY)
    {
        mouseDown = false;
    }

    public void onMouseScroll(int wheelDelta)
    {
    }

    public void onMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
    }

    public void onKeyTyped(char typedChar, int keyCode)
    {
    }

    public void playPressSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.create(resourceClickSound, 2.0F));
    }

//    public void claimHoverTooltip(int mouseX, int mouseY)
//    {
//        GuiTooltip.claim(this);
//    }

//    protected void sendChangeToParent()
//    {
//        if (parent != null)
//        {
//            parent.onChildInteract(this);
//        }
//    }
}

package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiElement;
import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.ISettingsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public abstract class GuiInteractableElement extends GuiElement
{
    private final ResourceLocation resourceClickSound;
    protected final ISettingsGui parent;
    protected final Map<String, Color> colorMap;

    protected String tooltipStr;

    public final int xPosInit;
    public final int yPosInit;
    private boolean drawBounds;

    protected boolean enabled;
    protected boolean hovered;
    protected boolean focused;
    protected boolean mouseDown;

    public int hoverTimeMs;
    public long startHoverTime;

//    protected GuiTooltipOverride guiTooltipOverride;

    protected GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(xPos, yPos, width, height, true);
        this.parent = parent;
        this.colorMap = new HashMap<>();
        this.colorMap.put("BASE", parent.getBaseColor());
        this.colorMap.put("ACCENT", parent.getAccentColor());
        this.colorMap.put("DISABLED", parent.getDisabledColor());
        this.resourceClickSound = new ResourceLocation("core-config:click");
        this.tooltipStr = tooltipStr;

        this.xPosInit = xPos;
        this.yPosInit = yPos;

        this.enabled = true;
        this.drawBounds = true;
    }

//    public GuiInteractableElement(IInteractableElement parent, int id, int xPos, int yPos, String baseColorHex, int width, int height, String displayString)
//    {
//        this(parent, id, xPos, yPos, baseColorHex, width, height, displayString, "");
//    }

    protected GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height)
    {
        this(parent, xPos, yPos, width, height, "");
    }

    protected GuiInteractableElement(ISettingsGui parent, int xPos, int yPos)
    {
        this(parent, xPos, yPos, 200, 20);
    }

    public ISettingsGui getParent()
    {
        return parent;
    }

    public Map<String, Color> getColorMap()
    {
        return colorMap;
    }

    public String getTooltipStr()
    {
        return tooltipStr;
    }

    public int getHoverTimeMs()
    {
        return hoverTimeMs;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isHovered()
    {
        return hovered;
    }

    public boolean isFocused()
    {
        return focused;
    }

    public boolean isMouseDown()
    {
        return mouseDown;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setHovered(boolean hovered)
    {
        this.hovered = hovered;
    }

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    public void addXPos(int xPos)
    {
        this.xPos = xPosInit + xPos;
    }

    public void addYPos(int yPos)
    {
        this.yPos = yPosInit + yPos;
    }

    public void setDrawBounds(boolean drawBounds)
    {
        this.drawBounds = drawBounds;
    }

    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        hovered = (mouseX >= xPos) && (mouseX < xPos + width)
                && (mouseY >= yPos) && (mouseY < yPos + height);

        if (drawBounds)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, yPos, width, height, new Color("#ff55ff"));
        }
    }

    public void claimHoverTooltip(int mouseX, int mouseY)
    {
        GuiTooltip.claim(this);
    }

    /**
     * Should run for every element when the left mouse is pressed down anywhere on screen,
     * onMousePress & onMouseRelease only run when mouse is actually pressed or released on an element
     */
    public boolean onMouseDownAmbiguous(int mouseX, int mouseY)
    {
        if (hovered)
        {
            if (enabled && visible) // TODO: enabled -> "canPress"?
            {
                playPressSound(parent.getMc().getSoundHandler());
                onMousePress(mouseX, mouseY);
                return true;
            }
        }
        else
        {
            focused = false;
        }
        return false;
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

    public void onKeyTyped(char typedChar, int keyCode)
    {
    }

    public void onScreenTick()
    {
    }

    public void playPressSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.create(resourceClickSound, 2.0F));
    }

    protected void sendChangeToParent()
    {
        if (parent != null)
        {
            parent.onChange(this);
        }
    }
}

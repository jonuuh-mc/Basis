package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiTooltip;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.GuiUtils;
import io.jonuuh.core.lib.config.gui.ISettingsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public abstract class GuiInteractableElement
{
    private final ResourceLocation resourceClickSound;
    protected final ISettingsGui parent;
    protected final Map<String, Color> colorMap;

    protected String tooltipStr;

    public final int xPosInit;
    public final int yPosInit;
    private boolean drawBounds;

    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;
    protected boolean enabled;
    protected boolean visible;
    protected boolean hovered;
    protected boolean focused;
    protected boolean mouseDown;

    public int hoverTimeMs;
    public long startHoverTime;

//    protected GuiTooltipOverride guiTooltipOverride;

    protected GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        this.parent = parent;
        this.colorMap = new HashMap<>();
        this.colorMap.put("BASE", parent.getBaseColor());
        this.colorMap.put("ACCENT", parent.getAccentColor());
        this.colorMap.put("DISABLED", parent.getDisabledColor());
        this.resourceClickSound = new ResourceLocation("core-config:click");
        this.tooltipStr = tooltipStr;

        this.xPosInit = xPos;
        this.yPosInit = yPos;

        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.visible = true;
        this.enabled = true;

        this.drawBounds = true;
//        this.guiTooltipOverride = new GuiTooltipOverride();
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

    public int getXPos()
    {
        return xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isVisible()
    {
        return visible;
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

    public void setVisible(boolean visible)
    {
        this.visible = visible;
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

    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }

    public void addXPos(int xPos)
    {
        this.xPos = xPosInit + xPos;
    }

    public void addYPos(int yPos)
    {
        this.yPos = yPosInit + yPos;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setDrawBounds(boolean drawBounds)
    {
        this.drawBounds = drawBounds;
    }

    public boolean onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            hovered = (mouseX >= xPos) && (mouseX < xPos + width)
                    && (mouseY >= yPos) && (mouseY < yPos + height);

//            trackHoverTime();

            if (drawBounds)
            {
                GuiUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, yPos, width, height, new Color("#ff55ff"));
            }
            return true;
        }

        return false;
    }

//    private void trackHoverTime()
//    {
//        // TODO: Minecraft.getSystemTime() ?
//
//        // TODO: does not work with guiSwitch?
//        if (hovered && !GuiTooltip.getInstance().isClaimed() /*&& !tooltip.str.isEmpty()*/) // tooltip is not being rendered currently
//        {
//            if (startHoverTime == 0)
//            {
//                startHoverTime = System.nanoTime() / 1000000;
//            }
//            hoverTimeMs = (int) ((System.nanoTime() / 1000000) - startHoverTime);
//        }
//        else if (startHoverTime != 0 || hoverTimeMs != 0)
//        {
//            startHoverTime = 0;
//            hoverTimeMs = 0;
//        }
//    }

//    @Override
//    protected void handleMouseMovements(Minecraft mc, int mouseX, int mouseY)
//    {
//        if (hovered && !tooltip.str.isEmpty())
//        {
//            if (startHoverTime == 0)
//            {
//                startHoverTime = System.nanoTime() / 1000000;
//            }
//            hoverTimeMs = (int) ((System.nanoTime() / 1000000) - startHoverTime);
//
//            if (hoverTimeMs >= 250)
//            {
//                drawTooltip(mc);
//            }
//        }
//        else if (startHoverTime != 0 || hoverTimeMs != 0)
//        {
//            startHoverTime = 0;
//            hoverTimeMs = 0;
//        }
//    }

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

//    public boolean canMousePress(int mouseX, int mouseY)
//    {
//        return enabled && visible && isHovered(mouseX, mouseY);
//    }

//    public void drawTooltip(Minecraft mc)
//    {
//        drawTooltip(mc, 1);
//    }
//
//    protected void drawTooltip(Minecraft mc, float scale)
//    {
//        drawingTooltip = true;
//
//        if (scale != 1)
//        {
//            float centerX = tooltip.x + (tooltip.rectW / 2F) - tooltip.padding;
//            float triPointY = tooltip.y - tooltip.padding + tooltip.rectH + tooltip.triH;
//
//            GL11.glPushMatrix();
//            GL11.glTranslatef(centerX, triPointY, 0.0F); // translate to point of tooltip triangle
//            GL11.glScalef(scale, scale, scale);
//            GL11.glTranslatef(-centerX, -(triPointY), 0.0F);
//        }
//
//        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, tooltip.x - tooltip.padding, tooltip.y - tooltip.padding, tooltip.rectW, tooltip.rectH, 2, tooltip.color, true);
//
//        GuiUtils.drawTriangle(GL11.GL_POLYGON, tooltip.x - tooltip.padding + (tooltip.rectW / 2F) - (tooltip.triW / 2F), tooltip.y - tooltip.padding + tooltip.rectH, tooltip.triW, tooltip.triH, tooltip.color, 0);
//
//        mc.fontRendererObj.drawString(tooltip.str, tooltip.x, tooltip.y, -1, false);
//
//        if (scale != 1)
//        {
//            GL11.glPopMatrix();
//        }
//    }

    public void playPressSound(SoundHandler soundHandler)
    {
        soundHandler.playSound(PositionedSoundRecord.create(resourceClickSound, 2.0F));
    }

    protected void sendChangeToParent(/*GuiInteractableElement thisElement*/)
    {
        if (parent != null)
        {
            parent.onChange(this/*thisElement*/); // TODO: not passing this will give generic type to onChange?
        }
    }

//    protected class GuiTooltipOverride
//    {
//        boolean isUsed;
//
//        String str;
//        Color color;
//        float posX;
//        float posY;
//
//        float padding;
//        float rectWidth;
//        float rectHeight;
//        float triWidth;
//        float triHeight;
//
////        public GuiTooltipOverride()
////        {
////            this.str = currentElement.tooltipStr;
////
////            int strWidth = mc.fontRendererObj.getStringWidth(str) - 1;
////
////            this.padding = 2F;
////
////            this.rectWidth = strWidth + (padding * 2);
////            this.rectHeight = (mc.fontRendererObj.FONT_HEIGHT - 1) + (padding * 2);
////
////            this.triWidth = 6;
////            this.triHeight = 3;
////
////            this.posX = currentElement.xPos - (strWidth / 2F) + (currentElement.width / 2F);
////            this.posY = currentElement.yPos + padding - rectHeight - triHeight - 2; // -2 somewhat arbitrary
////
////            this.color = currentElement.colorMap.get("BASE") /*Color.getRandom()*/;
////        }
//    }

//    protected class Tooltip
//    {
//        final float padding;
//        final float rectW;
//        final float rectH;
//        final float triW;
//        final float triH;
//        String str;
//        float x;
//        float y;
//        Color color;
//
//        protected Tooltip(String tooltip)
//        {
//            this.str = tooltip;
//
//            Minecraft mc = Minecraft.getMinecraft();
//            int strWidth = mc.fontRendererObj.getStringWidth(str) - 1;
//
//            this.padding = 2F;
//
//            this.rectW = strWidth + (padding * 2);
//            this.rectH = (mc.fontRendererObj.FONT_HEIGHT - 1) + (padding * 2);
//
//            this.triW = 6;
//            this.triH = 3;
//
//            this.x = xPosition - (strWidth / 2F) + (width / 2F);
//            this.y = yPosition + padding - rectH - triH - 2; // -2 somewhat arbitrary
//
//            this.color = baseColor;
//        }
//    }
}

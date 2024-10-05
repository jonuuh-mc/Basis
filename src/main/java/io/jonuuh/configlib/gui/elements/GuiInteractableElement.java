package io.jonuuh.configlib.gui.elements;

import io.jonuuh.configlib.util.Color;
import io.jonuuh.configlib.gui.GuiUtils;
import io.jonuuh.configlib.gui.ISettingsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class GuiInteractableElement extends GuiButton
{
    private final ResourceLocation resourceClickSound;
    private long startHoverTime;
    protected final ISettingsGui parent;
    public final Color baseColor;
    protected final Color accentColor;
    protected final Color disabledColor;
    protected Tooltip tooltip;
    protected int hoverTimeMs;
    private boolean drawBounds;

    GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height, String displayString, String tooltipStr)
    {
        super(-1, xPos, yPos, width, height, displayString);
        this.parent = parent;
        this.baseColor = parent.getBaseColor();
        this.accentColor = parent.getAccentColor();
        this.disabledColor = parent.getDisabledColor();
        this.tooltip = new Tooltip(tooltipStr);
        this.resourceClickSound = new ResourceLocation("configlib:click");
//        drawBounds = true;
    }

//    public GuiInteractableElement(IInteractableElement parent, int id, int xPos, int yPos, String baseColorHex, int width, int height, String displayString)
//    {
//        this(parent, id, xPos, yPos, baseColorHex, width, height, displayString, "");
//    }

    GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltip)
    {
        this(parent, xPos, yPos, width, height, "", tooltip);
    }

    GuiInteractableElement(ISettingsGui parent, int xPos, int yPos, int width, int height)
    {
        this(parent, xPos, yPos, width, height, "");
    }

    GuiInteractableElement(ISettingsGui parent, int xPos, int yPos)
    {
        this(parent, xPos, yPos, 200, 20);
    }

    public ISettingsGui getParentGui()
    {
        return parent;
    }

    public Color getBaseColor()
    {
        return baseColor;
    }

    public String getTooltipStr()
    {
        return tooltip.str;
    }

    public int getHoverTimeMs()
    {
        return hoverTimeMs;
    }

    void drawBounds()
    {
        drawBounds = !drawBounds;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible)
        {
            hovered = (mouseX >= xPosition) && (mouseX < xPosition + width)
                    && (mouseY >= yPosition) && (mouseY < yPosition + height);

            mouseDragged(mc, mouseX, mouseY);

            if (drawBounds)
            {
                GuiUtils.drawRectangle(GL11.GL_LINE_LOOP, xPosition, yPosition, width, height, new Color());
            }
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (hovered && !tooltip.str.isEmpty())
        {
            if (startHoverTime == 0)
            {
                startHoverTime = System.nanoTime() / 1000000;
            }
            hoverTimeMs = (int) ((System.nanoTime() / 1000000) - startHoverTime);

            if (hoverTimeMs >= 250)
            {
                drawTooltip(mc);
            }
        }
        else if (startHoverTime != 0 || hoverTimeMs != 0)
        {
            startHoverTime = 0;
            hoverTimeMs = 0;
        }
    }

    protected void drawTooltip(Minecraft mc)
    {
        drawTooltip(mc, 1);
    }

    protected void drawTooltip(Minecraft mc, float scale)
    {
        if (scale != 1)
        {
            float centerX = tooltip.x + (tooltip.rectW / 2F) - tooltip.padding;
            float triPointY = tooltip.y - tooltip.padding + tooltip.rectH + tooltip.triH;

            GL11.glPushMatrix();
            GL11.glTranslatef(centerX, triPointY, 0.0F); // translate to point of tooltip triangle
            GL11.glScalef(scale, scale, scale);
            GL11.glTranslatef(-centerX, -(triPointY), 0.0F);
        }

        GuiUtils.drawRoundedRect(GL11.GL_POLYGON, tooltip.x - tooltip.padding, tooltip.y - tooltip.padding, tooltip.rectW, tooltip.rectH, 2, tooltip.color, true);

        GuiUtils.drawTriangle(GL11.GL_POLYGON, tooltip.x - tooltip.padding + (tooltip.rectW / 2F) - (tooltip.triW / 2F), tooltip.y - tooltip.padding + tooltip.rectH, tooltip.triW, tooltip.triH, tooltip.color, 0);

        mc.fontRendererObj.drawString(tooltip.str, tooltip.x, tooltip.y, -1, false);

        if (scale != 1)
        {
            GL11.glPopMatrix();
        }
    }

    @Override
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

    protected class Tooltip
    {
        final float padding;
        final float rectW;
        final float rectH;
        final float triW;
        final float triH;
        String str;
        float x;
        float y;
        Color color;

        protected Tooltip(String tooltip)
        {
            this.str = tooltip;

            Minecraft mc = Minecraft.getMinecraft();
            int strWidth = mc.fontRendererObj.getStringWidth(str) - 1;

            this.padding = 2F;

            this.rectW = strWidth + (padding * 2);
            this.rectH = (mc.fontRendererObj.FONT_HEIGHT - 1) + (padding * 2);

            this.triW = 6;
            this.triH = 3;

            this.x = xPosition - (strWidth / 2F) + (width / 2F);
            this.y = yPosition + padding - rectH - triH - 2; // -2 somewhat arbitrary

            this.color = baseColor;
        }
    }
}

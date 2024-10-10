package io.jonuuh.configlib.gui.elements.interactable;

import io.jonuuh.configlib.util.Color;
import io.jonuuh.configlib.gui.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiButtonModern extends GuiButton
{
    private Color color = new Color(0.5F, 0.0F, 0.8F, 1.0F);
    private final int initialYPosition;

    public GuiButtonModern(int id, int xPos, int yPos, String displayString)
    {
        super(id, xPos, yPos, displayString);
        initialYPosition = yPos;
    }

    public GuiButtonModern(int id, int xPos, int yPos, int width, int height, String displayString)
    {
        super(id, xPos, yPos, width, height, displayString);
        initialYPosition = yPos;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.hovered);
//            GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);

            GuiUtils.drawRoundedRect(GL11.GL_POLYGON, this.xPosition, this.yPosition, this.width, this.height, 3, this.color, true);

            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;
//            if (this.packedFGColour != 0)
//            {
//                color = this.packedFGColour;
//            }
//            else if (!this.enabled)
//            {
//                color = 10526880;
//            }
//            else if (this.hovered)
//            {
//                color = 16777120;
//            }

            String buttonText = this.displayString;
            int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");
            if (strWidth > this.width - 6 && strWidth > ellipsisWidth)
            {
                buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, this.width - 6 - ellipsisWidth).trim() + "...";
            }

            this.drawCenteredString(mc.fontRendererObj, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);

        if (pressed)
        {
            this.color = new Color(0.5F, 0.0F, 0.5F, 1.0F);
            this.yPosition += 1; // affects hovered text color only?
        }
        return pressed;
    }

    @Override
    public void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_)
    {
        this.color = new Color(0.5F, 0.0F, 0.8F, 1.0F);
        this.yPosition = initialYPosition;
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_);
    }

    @Override
    public void playPressSound(SoundHandler p_playPressSound_1_)
    {
        ResourceLocation resource = new ResourceLocation("modtemplate:ui.click-loud");
        p_playPressSound_1_.playSound(PositionedSoundRecord.create(resource, 2.0F));
    }
}

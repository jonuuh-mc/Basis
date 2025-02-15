package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.config.gui.elements.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class GuiCheckbox extends GuiSettingElement
{
    protected boolean drawCheck;
    protected boolean isChecked;

    public GuiCheckbox(GuiContainer parent, String elementName, int xPos, int yPos, int size, String tooltipStr, boolean isChecked)
    {
        super(parent, elementName, xPos, yPos, size, size, tooltipStr);
        this.isChecked = isChecked;
        this.drawCheck = false; // TODO
    }

    public GuiCheckbox(GuiContainer parent, String elementName, int xPos, int yPos, String tooltipStr, boolean isChecked)
    {
        this(parent, elementName, xPos, yPos, 16, tooltipStr, isChecked);
    }

    public GuiCheckbox(GuiContainer parent, String elementName, int xPos, int yPos, boolean isChecked)
    {
        this(parent, elementName, xPos, yPos, "", isChecked);
    }

    public GuiCheckbox(GuiContainer parent, String elementName, int xPos, int yPos)
    {
        this(parent, elementName, xPos, yPos, false);
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public void flip()
    {
        setIsChecked(!isChecked);
        sendChangeToSetting();
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
        if (!drawCheck)
        {
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, parent.getInnerRadius(), parent.getColorMap().get("ACCENT"), true);

            if (isChecked)
            {
                float pad = width * 0.075F;
                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos + pad, yPos + pad, width - (pad * 2), height - (pad * 2), parent.getInnerRadius(), baseColor, true);
            }
        }
        else
        {
            Color boxColor = isChecked ? baseColor : parent.getColorMap().get("ACCENT");
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, parent.getInnerRadius(), boxColor, true);

            if (isChecked)
            {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glLineWidth(3F);

                float pad = 2F;
                RenderUtils.drawVertices(GL11.GL_LINE_STRIP,
                        new float[][]{
                                new float[]{xPos + width - pad, yPos + pad + 1},
                                new float[]{xPos + (width * 0.333F), yPos + height - pad - 1},
                                new float[]{xPos + pad, yPos + (width * 0.5F)}},
                        parent.getColorMap().get("ACCENT"));

                GL11.glLineWidth(1F);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public void onMousePress(int mouseX, int mouseY)
    {
        super.onMousePress(mouseX, mouseY);
        flip();
    }

    @Override
    protected void updateSetting()
    {
        ((BoolSetting) associatedSetting).setValue(isChecked);
    }
}

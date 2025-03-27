package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.config.setting.types.single.BoolSetting;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

// TODO: shouldn't this just extend guiswitch? think about use cases for each, google?
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
        trySendChangeToSetting();
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!drawCheck)
        {
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), width, height, parent.getInnerRadius(), getColor(GuiColorType.ACCENT1), true);

            if (isChecked)
            {
                float pad = width * 0.075F;
                RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos() + pad, worldYPos() + pad, width - (pad * 2), height - (pad * 2), parent.getInnerRadius(), getColor(GuiColorType.BASE), true);
            }
        }
        else
        {
            Color boxColor = isChecked ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT1);
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), width, height, parent.getInnerRadius(), boxColor, true);

            if (isChecked)
            {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glLineWidth(3F);

                float pad = 2F;
                RenderUtils.drawVertices(GL11.GL_LINE_STRIP,
                        new float[][]{
                                new float[]{worldXPos() + width - pad, worldYPos() + pad + 1},
                                new float[]{worldXPos() + (width * 0.333F), worldYPos() + height - pad - 1},
                                new float[]{worldXPos() + pad, worldYPos() + (width * 0.5F)}},
                        getColor(GuiColorType.ACCENT1));

                GL11.glLineWidth(1F);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        flip();
    }

    @Override
    protected void updateSetting()
    {
        if (associatedSetting instanceof BoolSetting)
        {
            ((BoolSetting) associatedSetting).setValue(isChecked);
        }
    }
}

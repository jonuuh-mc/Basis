package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

public class GuiCheckbox extends GuiElement
{
    protected static final ResourceLocation checkmarkResource = new ResourceLocation("core:textures/check.png");
    protected boolean isChecked;

    public GuiCheckbox(String elementName, float xPos, float yPos, float size, boolean isChecked)
    {
        super(elementName, xPos, yPos, size, size);
        this.isChecked = isChecked;
    }

    public GuiCheckbox(String elementName, float xPos, float yPos, boolean isChecked)
    {
        this(elementName, xPos, yPos, DEFAULT_HEIGHT, isChecked);
    }

    public GuiCheckbox(String elementName, float xPos, float yPos)
    {
        this(elementName, xPos, yPos, false);
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
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
//        Color boxColor = isChecked ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT1);
//        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), boxColor);

        RenderUtils.drawNineSliceTexturedRect(resourceGenericBackgroundTex,
                worldXPos(), worldYPos(), /*0*/zLevel /*- 3*/, getWidth(), getHeight(),
                52, 52, 12, 8, getColor(GuiColorType.ACCENT1));

        if (isChecked)
        {
            RenderUtils.drawTexturedRect(checkmarkResource, worldXPos(), worldYPos(), /*0*/zLevel, getWidth(), getHeight(), getColor(GuiColorType.BASE));
        }
    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        flip();
    }
}

package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.AbstractGuiScreen;
import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCheckbox extends GuiElement
{
    public static final ResourceLocation checkmarkResource = new ResourceLocation("core:checkmark.png");
    protected boolean isChecked;

    public GuiCheckbox(String elementName, float xPos, float yPos, float size, boolean isChecked)
    {
        super(elementName, xPos, yPos, size, size);
        this.isChecked = isChecked;
    }

    public GuiCheckbox(String elementName, float xPos, float yPos, boolean isChecked)
    {
        this(elementName, xPos, yPos, new Dimensions().height, isChecked);
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
        Color boxColor = isChecked ? getColor(GuiColorType.BASE) : getColor(GuiColorType.ACCENT1);
        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), boxColor);

        if (isChecked)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glPushMatrix();
            GL11.glTranslatef(worldXPos(), worldYPos(), 0);
            GL11.glScalef(getWidth() / 256, getHeight() / 256, 0);
            GL11.glTranslatef(-worldXPos(), -worldYPos(), 0);

            mc.getTextureManager().bindTexture(checkmarkResource);

            // TODO: finally remembered the problem with positions being floats
            //  need to make custom draw texture method? shouldn't be too bad
            AbstractGuiScreen.drawModalRectWithCustomSizedTexture((int) worldXPos(), (int) worldYPos(), 0, 0, 256, 256, 256, 256);
            GL11.glPopMatrix();
        }

    }

    @Override
    public void onMouseDown(int mouseX, int mouseY)
    {
        flip();
    }
}

package io.jonuuh.core.lib.gui.properties;

import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

public class ScissorBox
{
    public final GuiContainer host;
    private int x;
    private int y;
    private int width;
    private int height;

    public ScissorBox(GuiContainer host)
    {
        this.host = host;
    }

    public void start()
    {
        update();

        int windowOffset = 1;
        int textureOffset = 3;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        // Prevent trying to scissor an area with negative width/height
        if (width - textureOffset - windowOffset < 0 || height - textureOffset - windowOffset < 0)
        {
            return;
        }
        //        RenderUtils.drawRectangle(x/*-1*/, y/*-1*/, width/*+3*/, height/*+3*/, new Color("#4400ff00"));
        RenderUtils.scissorFromTopLeft(x + textureOffset, y + textureOffset, width - textureOffset - windowOffset, height - textureOffset - windowOffset);
    }

    public void end()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    private void update()
    {
        int greatestLeft = (int) getGreatestLeftBound();
        int leastRight = (int) getLeastRightBound();

        int greatestTopBound = (int) getGreatestTopBound();
        int leastBottom = (int) getLeastBottomBound();

        if (greatestLeft < leastRight && greatestTopBound < leastBottom)
        {
            int boundWidth = leastRight - greatestLeft;
            int boundHeight = leastBottom - greatestTopBound;

            this.x = greatestLeft;
            this.y = greatestTopBound;
            this.width = boundWidth;
            this.height = boundHeight;
        }
    }

    private float getGreatestLeftBound()
    {
        return host.hasParent() ? Math.max(host.getParent().getScissorBox().getGreatestLeftBound(), host.getLeftBound()) : host.getLeftBound();
    }

    private float getLeastRightBound()
    {
        return host.hasParent() ? Math.min(host.getParent().getScissorBox().getLeastRightBound(), host.getRightBound()) : host.getRightBound();
    }

    private float getGreatestTopBound()
    {
        return host.hasParent() ? Math.max(host.getParent().getScissorBox().getGreatestTopBound(), host.getTopBound()) : host.getTopBound();
    }

    private float getLeastBottomBound()
    {
        return host.hasParent() ? Math.min(host.getParent().getScissorBox().getLeastBottomBound(), host.getBottomBound()) : host.getBottomBound();
    }
}

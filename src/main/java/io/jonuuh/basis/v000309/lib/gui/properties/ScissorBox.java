//package io.jonuuh.basis.lib.gui.properties;
//
//import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
//import io.jonuuh.basis.lib.util.Color;
//import io.jonuuh.basis.lib.util.RenderUtils;
//import org.lwjgl.opengl.GL11;
//
//public class ScissorBox
//{
//    public final GuiContainer host;
//    private int x;
//    private int y;
//    private int width;
//    private int height;
//
//    public ScissorBox(GuiContainer host)
//    {
//        this.host = host;
//    }
//
//    public void start()
//    {
//        update();
//
//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//
//        // Prevent trying to scissor an area with negative width/height
//        if (width < 0 || height < 0)
//        {
//            return;
//        }
//
//        RenderUtils.drawRectangle(x, y, width, height, new Color("#4400ff00"));
//        RenderUtils.scissorFromTopLeft(x, y, width, height);
//    }
//
//    public void end()
//    {
//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//        GL11.glPopMatrix();
//    }
//
//    private void update()
//    {
//        int greatestLeft = (int) getGreatestLeftBound();
//        int leastRight = (int) getLeastRightBound();
//
//        int greatestTopBound = (int) getGreatestTopBound();
//        int leastBottom = (int) getLeastBottomBound();
//
//        // If neither width nor height is 0
//        if (greatestLeft < leastRight && greatestTopBound < leastBottom)
//        {
//            int boundWidth = leastRight - greatestLeft;
//            int boundHeight = leastBottom - greatestTopBound;
//
//            this.x = greatestLeft;
//            this.y = greatestTopBound;
//            this.width = boundWidth;
//            this.height = boundHeight;
//        }
//    }
//
//    private float getGreatestLeftBound()
//    {
//        return host.hasParent() ? Math.max(host.getParent().getScissorBox().getGreatestLeftBound(), host.getLeftBound()) : host.getLeftBound();
//    }
//
//    private float getLeastRightBound()
//    {
//        return host.hasParent() ? Math.min(host.getParent().getScissorBox().getLeastRightBound(), host.getRightBound()) : host.getRightBound();
//    }
//
//    private float getGreatestTopBound()
//    {
//        return host.hasParent() ? Math.max(host.getParent().getScissorBox().getGreatestTopBound(), host.getTopBound()) : host.getTopBound();
//    }
//
//    private float getLeastBottomBound()
//    {
//        return host.hasParent() ? Math.min(host.getParent().getScissorBox().getLeastBottomBound(), host.getBottomBound()) : host.getBottomBound();
//    }
//}

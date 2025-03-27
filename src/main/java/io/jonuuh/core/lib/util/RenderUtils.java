package io.jonuuh.core.lib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public abstract class RenderUtils
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static RenderManager getRenderManager()
    {
        return mc.getRenderManager();
    }

    public static FontRenderer getFontRenderer()
    {
        return mc.fontRendererObj;
    }

    public static int getStringWidthMinusOne(String str)
    {
        return getFontRenderer().getStringWidth(str) - 1; // getStringWidth is sometimes over by 1 depending on usage? (for loop end condition?)
    }

    // yoinked from somewhere in mc src code, not sure where
    public static String trimStringToWidthWithEllipsis(String str, int width)
    {
        int strWidth = getFontRenderer().getStringWidth(str);
        int ellipsisWidth = getFontRenderer().getStringWidth("...");

        if (strWidth > width - 6 && strWidth > ellipsisWidth)
        {
            return getFontRenderer().trimStringToWidth(str, width - 6 - ellipsisWidth).trim() + "...";
        }

        return str;
    }

    public static Vec3 getEntityPosForRender(EntityPlayer player, float partialTicks)
    {
        RenderManager renderManager = mc.getRenderManager();

        if (player == mc.thePlayer && renderManager.livingPlayer == mc.thePlayer)
        {
            // viewer pos (usually client player pos) is cached by render manager during each render pass
            return new Vec3(renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ);
        }

        double x = (player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks);
        double y = (player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks);
        double z = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks);
        return new Vec3(x, y, z);
    }

    public static void scissorFromTopLeft(int x, int y, int w, int h)
    {
        ScaledResolution sr = new ScaledResolution(mc);
//        System.out.println(sr.getScaleFactor() + " " + x + " " + y + " " + w + " " + h);
        GL11.glScissor(x * sr.getScaleFactor(), (sr.getScaledHeight() - y - h) * sr.getScaleFactor(),
                w * sr.getScaleFactor(), h * sr.getScaleFactor());
    }

    public static void drawBox(AxisAlignedBB bb, int glMode, Color color)
    {
        if (glMode == GL11.GL_LINES)
        {
            drawOutlinedBox(bb, color);
        }
        else if (glMode == GL11.GL_QUADS)
        {
            drawSolidBox(bb, color);
        }
    }

    public static void glHead(Color c)
    {
        // GL setup
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(c.getR(), c.getG(), c.getB(), c.getA());
        GL11.glLineWidth(3F);
//        GL11.glCullFace(GL11.GL_FRONT);

//        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
//        GL11.glPolygonOffset(1.0f, -1000000.0f);

        Vec3 renderPos = getEntityPosForRender(mc.thePlayer, 0.0F);
        Vec3 finalPos = new Vec3(0, 4,0);

        GL11.glTranslated((finalPos.xCoord - renderPos.xCoord), (finalPos.yCoord - renderPos.yCoord), (finalPos.zCoord - renderPos.zCoord));
    }

    public static void glTail()
    {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glCullFace(GL11.GL_BACK);

//        GL11.glPolygonOffset(1.0f, 1000000.0f);
//        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

        GL11.glPopMatrix();
    }

    public static void drawSolidBox(AxisAlignedBB bb, Color color)
    {
        glHead(color);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();

        glTail();
    }

    public static void drawOutlinedBox(AxisAlignedBB bb, Color color)
    {
        glHead(color);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        //////////
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        //////////
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();

        glTail();
    }

    // Vertices MUST be in counterclockwise order (opengl triangle winding order)
    public static void drawVertices(int glMode, float[][] vertices, Color color)
    {
        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        GL11.glPushMatrix();

//        if (rotationAngle != 0)
//        {
//            GL11.glTranslatef(vertices[0][0], vertices[0][1], 0.0F); // translate to object's origin
//            GL11.glRotatef(rotationAngle, 0.0F, 0.0F, 1.0F);
//            GL11.glTranslatef(-vertices[0][0], -vertices[0][1], 0.0F);
//        }

        // Enable transparency
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glBegin(glMode);
        for (float[] vertex : vertices)
        {
            GL11.glVertex2f(vertex[0], vertex[1]);
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Disable transparency
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTriangle(int glMode, float x, float y, float width, float height, Color color, float rotationAngle)
    {
        width = Math.max(width, 0);
        height = Math.max(height, 0);
        float hWidth = width / 2F;
        float hHeight = height / 2F;

        GL11.glPushMatrix();

//        if (rotationAngle != 0)
//        {
//            GL11.glTranslatef(x + hWidth, y + hHeight, 0.0F); // translate to object's origin
//            GL11.glRotatef(rotationAngle, 0.0F, 0.0F, 1.0F);
//            GL11.glTranslatef(-x - hWidth, -y - hHeight, 0.0F);
//        }

        // Order: top left, bottom left, bottom right, top right
        drawVertices(glMode, new float[][]{{x, y}, {x + (width / 2F), y + height}, {x + width, y}}, color);
        GL11.glPopMatrix();

//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        GL11.glLineWidth(1F);
//        drawVertices(GL11.GL_LINE_LOOP, new float[][]{{x, y}, {x + (width / 2F), y + height}, {x + width, y}}, color, rotationAngle);
//        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawRectangle(int glMode, float x, float y, float width, float height, Color color)
    {
        width = Math.max(width, 0);
        height = Math.max(height, 0);

        // Order: top left, bottom left, bottom right, top right
        drawVertices(glMode, new float[][]{{x, y}, {x, y + height}, {x + width, y + height}, {x + width, y}}, color);
    }

//    public static void drawTooltip()
//    {
//        // TODO: ?
//    }

    // TODO: make this (draw from corner rather than center) the default
    public static void drawRoundedRect(int glMode, float x, float y, float width, float height, float radius, Color color, boolean offsetRadius)
    {
        float centerX = x + (width / 2F);
        float centerY = y + (height / 2F);

        if (offsetRadius)
        {
            radius = Math.min(radius, (width / 2F));
            radius = Math.min(radius, (height / 2F));
        }

        width = offsetRadius ? width - (radius * 2) : width;
        height = offsetRadius ? height - (radius * 2) : height;
        drawRoundedRectCentered(glMode, centerX, centerY, width, height, radius, color);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(0.5F);
        drawRoundedRectCentered(GL11.GL_LINE_LOOP, centerX, centerY, width, height, radius, color);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /*
    Draws a rounded rectangle
    - Given the params, calculates vertices for four corners/quadrants of a circle, counter-clockwise from (1, 0) on a unit circle.
    - When the full object is drawn, all vertices are connected, so by spacing the quadrants apart (width and height),
      and drawing them in the right order (google "opengl triangle winding order") a line will be drawn between
      each quadrant, making it a rounded rectangle
    */
    public static void drawRoundedRectCentered(int glMode, float centerX, float centerY, float width, float height, float radius, Color color)
    {
        float ninetyDegRad = (float) (Math.PI / 2);

        width = Math.max(width, 0);
        height = Math.max(height, 0);
        radius = Math.max(radius, 0);

        float xRight = centerX - (width / 2.0F);
        float xLeft = centerX + (width / 2.0F);
        float yUp = centerY + (height / 2.0F);
        float yDown = centerY - (height / 2.0F);

        // TODO:
//        float[][] vertices;

        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        GL11.glPushMatrix();

        // Enable transparency
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glBegin(glMode);
        // Each corner is drawn around (x,y) as the center of the circle
        addQuadrantVertices(xRight, yUp, 0, radius); // top right
        addQuadrantVertices(xLeft, yUp, ninetyDegRad, radius); // top left
        addQuadrantVertices(xLeft, yDown, ninetyDegRad * 2, radius); // bottom left
        addQuadrantVertices(xRight, yDown, ninetyDegRad * 3, radius); // bottom right
        GL11.glEnd();

        // Disable transparency
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void addQuadrantVertices(float centerX, float centerY, float startAngle, float radius)
    {
        if (radius == 0)
        {
            GL11.glVertex2f(centerX, centerY);
            return;
        }

        float ninetyDegRad = (float) (Math.PI / 2);
        int segments = 32;

        for (float angle = startAngle; angle <= startAngle + ninetyDegRad; angle += ninetyDegRad / segments)
        {
            GL11.glVertex2f(centerX - (radius * MathHelper.cos(angle)), centerY + (radius * MathHelper.sin(angle)));
        }
    }

//    public static void drawRoundedRectBorder(int glMode, float centerX, float centerY, float width, float height, float innerRadius, float outerRadius, Color color)
//    {
//        float ninetyDegRad = (float) (Math.PI / 2);
//
//        width = Math.max(width, 0);
//        height = Math.max(height, 0);
//
//        float xRight = centerX - (width / 2.0F);
//        float xLeft = centerX + (width / 2.0F);
//        float yUp = centerY + (height / 2.0F);
//        float yDown = centerY - (height / 2.0F);
//
//        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
//        GL11.glPushMatrix();
//        GL11.glBegin(glMode);
//
//        addQuadrantVertices2(xRight, yUp, 0, outerRadius, innerRadius);
//        addQuadrantVertices2(xLeft, yUp, ninetyDegRad, outerRadius, innerRadius);
//        addQuadrantVertices2(xLeft, yDown, ninetyDegRad * 2, outerRadius, innerRadius);
//        addQuadrantVertices2(xRight, yDown, ninetyDegRad * 3, outerRadius, innerRadius);
//
//        // Connect the two ends of the triangle strip (glMode should be tri strip)
//        GL11.glVertex2f(xRight - (outerRadius * MathHelper.cos(ninetyDegRad / 32.0F)), yUp + (outerRadius * MathHelper.sin(ninetyDegRad / 32.0F)));
//        GL11.glVertex2f(xRight - innerRadius, yUp);
//
//        GL11.glEnd();
//        GL11.glPopMatrix();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    private static void addQuadrantVertices2(float centerX, float centerY, float startAngle, float innerRadius, float outerRadius)
//    {
//        if (innerRadius == 0)
//        {
//            GL11.glVertex2f(centerX, centerY);
//            return;
//        }
//
//        float ninetyDegRad = (float) (Math.PI / 2);
//        int segments = 32;
//        int vertex = 0;
//
//        for (float angle = startAngle; angle <= startAngle + ninetyDegRad; angle += ninetyDegRad / segments, vertex++)
//        {
//            float rad = ((startAngle <= ninetyDegRad ? vertex : vertex + 1) % 2 == 0) ? outerRadius : innerRadius;
//            GL11.glVertex2f(centerX - (rad * MathHelper.cos(angle)), centerY + (rad * MathHelper.sin(angle)));
//        }
//    }
}

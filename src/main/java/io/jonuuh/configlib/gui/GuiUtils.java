package io.jonuuh.configlib.gui;

import io.jonuuh.configlib.util.Color;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class GuiUtils
{
//    public enum ScreenDirection
//    {
//        UP,
//        DOWN,
//        RIGHT,
//        LEFT,
//        HORIZONTAL,
//        VERTICAL
//    }

    // Vertices MUST be in counterclockwise order (opengl triangle winding order)
    public static void drawVertices(int glMode, float[][] vertices, Color color)
    {
        drawVertices(glMode, vertices, color, 0);
    }

    // Vertices MUST be in counterclockwise order (opengl triangle winding order)
    public static void drawVertices(int glMode, float[][] vertices, Color color, float rotationAngle)
    {
        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        GL11.glPushMatrix();

        if (rotationAngle != 0)
        {
            GL11.glTranslatef(vertices[0][0], vertices[0][1], 0.0F); // translate to object's origin
            GL11.glRotatef(rotationAngle, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-vertices[0][0], -vertices[0][1], 0.0F);
        }

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

        // Disable transparency
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTriangle(int glMode, float x, float y, float width, float height, Color color, float rotationAngle)
    {
        width = Math.max(width, 0);
        height = Math.max(height, 0);

        // Order: top left, bottom left, bottom right, top right
        drawVertices(glMode, new float[][]{{x, y}, {x + (width / 2F), y + height}, {x + width, y}}, color, rotationAngle);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1F);
        drawVertices(GL11.GL_LINE_LOOP, new float[][]{{x, y}, {x + (width / 2F), y + height}, {x + width, y}}, color, rotationAngle);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
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

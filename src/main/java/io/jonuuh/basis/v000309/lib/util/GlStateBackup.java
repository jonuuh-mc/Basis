package io.jonuuh.basis.v000309.lib.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Used to back up and restore combinations of some global GL11 states.
 * <p>
 * Designed to be {@link Builder instantiated} prior to setting some GL states before a draw,
 * then {@link GlStateBackup#restore() restored} after the draw
 */
public class GlStateBackup
{
    private static final IntBuffer INT2 = BufferUtils.createIntBuffer(2);
    private static final FloatBuffer FLOAT4 = BufferUtils.createFloatBuffer(4);

    private final Builder builder;

    private boolean blend;
    private int blendSrc;
    private int blendDst;

    private boolean depthTest;
    private boolean texture2D;
    private boolean cullFace;

    private boolean lineSmooth;
    /** GL State can't be read, if the builder says this should be backed up, it will be restored to a default of GL_DONT_CARE */
    private int lineSmoothHint;
    private float lineWidth;

    private int polygonMode;

    private float[] currentColor;

    private GlStateBackup(Builder builder)
    {
        this.builder = builder;

        if (builder.backupBlend)
        {
            this.blend = GL11.glIsEnabled(GL11.GL_BLEND);
            this.blendSrc = GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB);
            this.blendDst = GL11.glGetInteger(GL14.GL_BLEND_DST_RGB);
        }

        if (builder.backupDepthTest)
        {
            this.depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        }
        if (builder.backupTexture2D)
        {
            this.texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        }
        if (builder.backupCullFace)
        {
            this.cullFace = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        }

        if (builder.backupLineSmooth)
        {
            this.lineSmooth = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        }
        if (builder.resetLineSmoothHint)
        {
            this.lineSmoothHint = GL11.GL_DONT_CARE;
        }
        if (builder.backupLineWidth)
        {
            this.lineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        }

        if (builder.backupPolygonMode)
        {
            INT2.clear();
            GL11.glGetInteger(GL11.GL_POLYGON_MODE, INT2);
            // Front face only - Assumes front and back faces use the same polygon mode,
            // probably should unless very strange, niche circumstances (for example if other loaded mods do something weird)
            this.polygonMode = INT2.get(0);
        }

        if (builder.backupColor)
        {
            FLOAT4.clear();
            GL11.glGetFloat(GL11.GL_CURRENT_COLOR, FLOAT4);

            this.currentColor = new float[4];

            for (int i = 0; i < 4; i++)
            {
                currentColor[i] = FLOAT4.get(i); // System.out.printf("i=%s:%s%n", i, colorBuf.get(i));
            }
        }
    }

    public void restore()
    {
        if (builder.backupBlend)
        {
            restoreBoolCap(blend, GL11.GL_BLEND);
            GL11.glBlendFunc(blendSrc, blendDst);
        }

        if (builder.backupDepthTest)
        {
            restoreBoolCap(depthTest, GL11.GL_DEPTH_TEST);
        }
        if (builder.backupTexture2D)
        {
            restoreBoolCap(texture2D, GL11.GL_TEXTURE_2D);
        }
        if (builder.backupCullFace)
        {
            restoreBoolCap(cullFace, GL11.GL_CULL_FACE);
        }

        if (builder.backupLineSmooth)
        {
            restoreBoolCap(lineSmooth, GL11.GL_LINE_SMOOTH);
        }
        if (builder.resetLineSmoothHint)
        {
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, lineSmoothHint);
        }
        if (builder.backupLineWidth)
        {
            GL11.glLineWidth(lineWidth);
        }

        if (builder.backupPolygonMode)
        {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, polygonMode);
        }

        if (builder.backupColor && currentColor != null)
        {
            GL11.glColor4f(currentColor[0], currentColor[1], currentColor[2], currentColor[3]);
        }
    }

    private void restoreBoolCap(boolean bool, int capability)
    {
        if (bool)
        {
            GL11.glEnable(capability);
        }
        else
        {
            GL11.glDisable(capability);
        }
    }

    public static GlStateBackup fullBackup()
    {
        return new GlStateBackup(new Builder()
                .backupBlend(true)
                .backupDepthTest(true)
                .backupTexture2D(true)
                .backupCullFace(true)
                .backupLineSmooth(true)
                .resetLineSmoothHint(true)
                .backupLineWidth(true)
                .backupPolygonMode(true)
                .backupColor(true)
        );
    }

    public static class Builder
    {
        private boolean backupBlend;

        private boolean backupDepthTest;
        private boolean backupTexture2D;
        private boolean backupCullFace;

        private boolean backupLineSmooth;
        private boolean resetLineSmoothHint;
        private boolean backupLineWidth;

        private boolean backupPolygonMode;
        private boolean backupColor;

        public Builder backupBlend(boolean backupBlend)
        {
            this.backupBlend = backupBlend;
            return this;
        }

        public Builder backupDepthTest(boolean backupDepthTest)
        {
            this.backupDepthTest = backupDepthTest;
            return this;
        }

        public Builder backupTexture2D(boolean backupTexture2D)
        {
            this.backupTexture2D = backupTexture2D;
            return this;
        }

        public Builder backupCullFace(boolean backupCullFace)
        {
            this.backupCullFace = backupCullFace;
            return this;
        }

        public Builder backupLineSmooth(boolean backupLineSmooth)
        {
            this.backupLineSmooth = backupLineSmooth;
            return this;
        }

        /**
         * If set, will restore to GL11.GL_DONT_CARE
         *
         * @see GlStateBackup#lineSmoothHint
         */
        public Builder resetLineSmoothHint(boolean resetLineSmoothHint)
        {
            this.resetLineSmoothHint = resetLineSmoothHint;
            return this;
        }

        public Builder backupLineWidth(boolean backupLineWidth)
        {
            this.backupLineWidth = backupLineWidth;
            return this;
        }

        public Builder backupPolygonMode(boolean backupPolygonMode)
        {
            this.backupPolygonMode = backupPolygonMode;
            return this;
        }

        public Builder backupColor(boolean backupColor)
        {
            this.backupColor = backupColor;
            return this;
        }

        public GlStateBackup build()
        {
            return new GlStateBackup(this);
        }
    }
}


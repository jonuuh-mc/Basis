package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public abstract class GuiElement
{
    public final Minecraft mc;
    public final GuiContainer parent;
    public final String elementName;

    protected int xPosInit;
    protected int yPosInit;

    protected int xPos;
    protected int yPos;
    protected int width;
    protected int height;

    protected boolean visible;
    protected boolean hovered;
    protected int zLevel;

    protected boolean drawBounds;

    protected String tooltipStr;
    protected GuiTooltip tooltip;

    protected Color baseColor;

    public int hoverTimeMs;
    public long startHoverTime;

    protected GuiElement(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, boolean visible, String tooltipStr)
    {
        this.mc = Minecraft.getMinecraft();
        this.parent = parent;
        this.elementName = elementName;

        this.xPosInit = xPos;
        this.yPosInit = yPos;

        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        this.visible = visible;
        this.hovered = false;
        this.zLevel = 0;

        this.drawBounds = true;
        this.tooltipStr = tooltipStr;
        this.baseColor = new Color("#1450A0"); // TODO:

        // TODO: try moving to top of constructor
        if (hasParent() /*&& !elementName.contains("$")*/)
        {
            parent.putChild(elementName, this);
        }
    }

    @Override
    public String toString()
    {
        return "GuiElement{" + "elementName='" + elementName + '\'' + ", parent=" + parent + '}';
    }

    public boolean hasParent()
    {
        return parent != null;
    }

    public int getXPos()
    {
        return xPos;
    }

    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public int getYPos()
    {
        return yPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }

    public void setTempXPos(int xPos)
    {
        this.xPos = xPosInit + xPos;
    }

    public void setTempYPos(int yPos)
    {
        this.yPos = yPosInit + yPos;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isHovered()
    {
        return hovered;
    }

    public void setHovered(boolean hovered)
    {
        this.hovered = hovered;
    }

    public int getZLevel()
    {
        return zLevel;
    }

    public void setZLevel(int zLevel)
    {
        this.zLevel = zLevel;
    }

    public boolean shouldDrawBounds()
    {
        return drawBounds;
    }

    public void setDrawBounds(boolean drawBounds)
    {
        this.drawBounds = drawBounds;
    }

    public String getTooltipStr()
    {
        return tooltipStr;
    }

    public void setTooltipStr(String tooltipStr)
    {
        this.tooltipStr = tooltipStr;
    }

    public Color getBaseColor()
    {
        return baseColor;
    }

    public void setBaseColor(Color baseColor)
    {
        this.baseColor = baseColor;
    }

    public int getHoverTimeMs()
    {
        return hoverTimeMs;
    }

    public void setHoverTimeMs(int hoverTimeMs)
    {
        this.hoverTimeMs = hoverTimeMs;
    }

    public long getStartHoverTime()
    {
        return startHoverTime;
    }

    public void setStartHoverTime(long startHoverTime)
    {
        this.startHoverTime = startHoverTime;
    }

    // TODO: zLevel
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
//        if (visible)
//        {
        // TODO: why is this not in if (visible) -> changed (anything broken?)
        hovered = (mouseX >= xPos) && (mouseX < xPos + width)
                && (mouseY >= yPos) && (mouseY < yPos + height);

        if (drawBounds)
        {
            RenderUtils.drawRectangle(GL11.GL_LINE_LOOP, xPos, yPos, width, height, new Color("#ff55ff"));
        }

        drawElement(mouseX, mouseY, partialTicks);

//            handleTooltip(mouseX, mouseY);
//        }
    }

    protected abstract void drawElement(int mouseX, int mouseY, float partialTicks);

    public void onScreenTick()
    {
    }

    public void handleTooltip(int mouseX, int mouseY)
    {
        if (hovered /*&& !element.getTooltipStr().isEmpty()*/)
        {
            if (startHoverTime == 0)
            {
                startHoverTime = Minecraft.getSystemTime();
            }

            if (hoverTimeMs >= 500)
            {
                this.tooltip = new GuiTooltip();
                tooltip.draw();
                return;
            }

            hoverTimeMs = (int) (Minecraft.getSystemTime() - startHoverTime);
        }
        else if (startHoverTime != 0 || hoverTimeMs != 0)
        {
            startHoverTime = 0;
            hoverTimeMs = 0;
        }
    }


//    public void claimHoverTooltip(int mouseX, int mouseY)
//    {
//        GuiTooltip.claim(this);
//    }

    protected class GuiTooltip
    {
        public float posX;
        public float posY;

        public float padding;
        public float rectWidth;
        public float rectHeight;
        public float triWidth;
        public float triHeight;

        private GuiTooltip()
        {
//            if (getTooltipStr().isEmpty())
//            {
//                return;
//            }

//        System.out.println("SETTING tooltip for: " + currentElement);
            int strWidth = mc.fontRendererObj.getStringWidth(getTooltipStr()) - 1;

            this.padding = 2F;

            this.rectWidth = strWidth + (padding * 2);
            this.rectHeight = (mc.fontRendererObj.FONT_HEIGHT - 1) + (padding * 2);

            this.triWidth = 6;
            this.triHeight = 3;

            this.posX = getXPos() - (strWidth / 2F) + (getWidth() / 2F);
            this.posY = getYPos() + padding - rectHeight - triHeight - 2; // -2 somewhat arbitrary
        }

        void draw()
        {
//        System.out.println("drawing tooltip for: " + currentElement);
            RenderUtils.drawRoundedRect(GL11.GL_POLYGON, posX - padding, posY - padding, rectWidth, rectHeight, 2, baseColor, true);

            RenderUtils.drawTriangle(GL11.GL_POLYGON, posX - padding + (rectWidth / 2F) - (triWidth / 2F), posY - padding + rectHeight, triWidth, triHeight, baseColor, 0);

            mc.fontRendererObj.drawString(getTooltipStr(), posX, posY, -1, false);
        }
    }
}

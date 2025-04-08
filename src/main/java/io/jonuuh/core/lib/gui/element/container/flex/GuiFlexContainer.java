package io.jonuuh.core.lib.gui.element.container.flex;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.MathUtils;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class GuiFlexContainer extends GuiContainer
{
    protected Map<GuiElement, FlexItemProperties> flexPropertiesMap;
    protected FlexDirection direction;
    protected FlexJustify justifyContent;
    protected FlexAlign alignItems;
    protected boolean isMainAxisFull; // TODO: track scaling within scaleMainAxisOverflow to maintain flag

    public GuiFlexContainer(String elementName, float xPos, float yPos, float width, float height, Map<GuiColorType, Color> colorMap)
    {
        super(elementName, xPos, yPos, width, height, colorMap);
        this.flexPropertiesMap = new HashMap<>();
        this.direction = FlexDirection.ROW;
        this.justifyContent = FlexJustify.START;
        this.alignItems = FlexAlign.START;
    }

    public GuiFlexContainer(String elementName, float xPos, float yPos, float width, float height, float outerRadius, float innerRadius)
    {
        this(elementName, xPos, yPos, width, height, outerRadius, innerRadius, null);
    }

    public FlexDirection getDirection()
    {
        return direction;
    }

    public void setDirection(FlexDirection direction)
    {
        this.direction = direction;
    }

    public FlexJustify getJustifyContent()
    {
        return justifyContent;
    }

    public void setJustifyContent(FlexJustify justifyContent)
    {
        this.justifyContent = justifyContent;
    }

    public FlexAlign getAlignItems()
    {
        return alignItems;
    }

    public void setAlignItems(FlexAlign alignItems)
    {
        this.alignItems = alignItems;
    }

    public boolean isHorizontal()
    {
        return direction == FlexDirection.ROW || direction == FlexDirection.ROW_REVERSE;
    }

    public boolean isReversed()
    {
        return direction == FlexDirection.ROW_REVERSE || direction == FlexDirection.COLUMN_REVERSE;
    }

    public float getMainAxisSize()
    {
        return isHorizontal() ? getWidth() : getHeight();
    }

//    public float getMainAxisMaxSize()
//    {
//        return isHorizontal() ? getInitialWidth() : getInitialHeight();
//    }
//
//    public float getMainAxisItemsMaxSize()
//    {
//        return (float) children.stream().mapToDouble(this::getItemMaxBasis).sum();
//    }

    public float getItemBasesSum()
    {
        return (float) children.stream().mapToDouble(this::getItemBasis).sum();
    }

    // TODO: why does this work w/out floating point error correction - check in debug?
    protected boolean isMainAxisFull()
    {
        return getMainAxisSize() == getItemBasesSum();
    }

    protected float getItemMinBasis(GuiElement child)
    {
        return (isHorizontal() ? child.getMinWidth() : child.getMinHeight());
    }

    protected float getItemBasis(GuiElement child)
    {
        return (isHorizontal() ? child.getWidth() : child.getHeight());
    }

    protected float getItemMaxBasis(GuiElement child)
    {
        return (isHorizontal() ? child.getMaxWidth() : child.getMaxHeight());
    }

    protected void setItemBasis(GuiElement child, float basis)
    {
        if (isHorizontal())
        {
            child.setWidth(basis);
        }
        else
        {
            child.setHeight(basis);
        }
    }


    protected FlexAlign getItemAlign(GuiElement child)
    {
        boolean hasProperty = flexPropertiesMap.get(child).getAlign() != null;
        return hasProperty ? flexPropertiesMap.get(child).getAlign() : alignItems;
    }

//    public int getTotalGrowth()
//    {
//        int sum = 0;
//        for (GuiElement child : children)
//        {
//            sum += flexPropertiesMap.get(child).getGrow();
//        }
//        return sum;
//    }
//
//    public int getTotalShrink()
//    {
//        int sum = 0;
//        for (GuiElement child : children)
//        {
//            sum += flexPropertiesMap.get(child).getShrink();
//        }
//        return sum;
//    }

    // TODO: this is fine right? containsKey definitely checks for identical reference? (unless guiElement overrides equals?)
    public void setFlexProperties(GuiElement child, FlexItemProperties flexProperties)
    {
        if (flexPropertiesMap.containsKey(child))
        {
            flexPropertiesMap.put(child, flexProperties);
        }
    }

    public void addChild(GuiElement child, FlexItemProperties flexProperties)
    {
        super.addChild(child);
        flexPropertiesMap.put(child, flexProperties);
//        child.setFlexProperties(flexProperties);
    }

    @Override
    public void addChild(GuiElement child)
    {
        this.addChild(child, new FlexItemProperties());
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), getColor(GuiColorType.BACKGROUND));

        drawArrow(direction, new Color("FFD700"), 8);

        FlexDirection alignDir = (direction == FlexDirection.ROW || direction == FlexDirection.ROW_REVERSE)
                ? FlexDirection.COLUMN : FlexDirection.ROW;
        drawArrow(alignDir, new Color(EnumChatFormatting.WHITE), 5);

        String info = this.elementName;
//        String info = direction.toString().toLowerCase() + ", " + justifyContent.toString().toLowerCase() + ", " + alignItems.toString().toLowerCase();
        float textX = worldXPos() + (getWidth() / 4) - (mc.fontRendererObj.getStringWidth(info) / 2F);
        float textY = worldYPos() + getHeight() - mc.fontRendererObj.FONT_HEIGHT;
        mc.fontRendererObj.drawString(info, textX, textY, getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
//        System.out.println("FlexContainer onInitGui: " + this.elementName);
        // TODO: might be a problem later
        if (!hasChildren())
        {
            return;
        }

        // TODO:
        // root container
        if (!hasParent())
        {
            int screenWidth = scaledResolution.getScaledWidth();
            int screenHeight = scaledResolution.getScaledHeight();

//            int xPadding = screenWidth / 10;
//            int yPadding = screenHeight / 10;
            int xPadding = 90;
            int yPadding = 90;
//            System.out.printf("(%s,%s) -> %s%n", screenWidth, screenHeight, scaledResolution.getScaleFactor());

            setLocalXPos(Math.max((screenWidth / 2F) - (getMaxWidth() / 2), xPadding));
            setLocalYPos(Math.max((screenHeight / 2F) - (getMaxHeight() / 2), yPadding));

            setWidth(Math.min(screenWidth - getLocalXPos() - xPadding, getMaxWidth()));
            setHeight(Math.min(screenHeight - getLocalYPos() - yPadding, getMaxHeight()));
        }

        if (elementName.equals("mainFlex"))
        {
//            this.setJustifyContent(FlexJustify.START);
//            flexPropertiesMap.get(this.children.get(0)).setGrow(1);
//            flexPropertiesMap.get(this.children.get(0)).setAlign(FlexAlign.STRETCH);

//            this.children.get(1).setFlexProperties(new FlexItemProperties().setGrow(1));
//            this.children.get(2).setFlexProperties(new FlexItemProperties().setGrow(100));
//            this.children.get(3).setFlexProperties(new FlexItemProperties().setGrow(1));
//            System.out.println();
//            this.children.get(2).setFlexProperties(new FlexItemProperties().setBasis(50));
//            this.children.get(2).setWidth(50);
//            this.children.get(2).setFlexProperties(new FlexItemProperties().setAlign(FlexAlign.END));
        }

        // The total free space along the main axis
        // Can be positive to denote that free space is available or negative to denote that the children are overflowing
        float freeLength = getMainAxisSize() - getItemBasesSum();

//        float maxAllocated = getMainAxisItemsMaxSize();
//        float freeSizeMax = getMainAxisMaxSize() - maxAllocated;
//
//        float totalLostSize = freeSizeMax - freeSize;
//        float remainingFreeSize = freeSizeMax - (freeSizeMax - freeSize);
//
//        System.out.println(elementName + " " + freeSize + " across all runs: " + maxAllocated + " " + freeSizeMax +
//                " " + totalLostSize + " " + remainingFreeSize) ;

        if (freeLength > 0)
        {
            growMainAxis(freeLength);
        }
        else if (freeLength < 0)
        {
            shrinkMainAxis(freeLength);
        }

        justifyMainAxis();

        scaleCrossAxisOverflow();
        alignCrossAxis();
    }

    /**
     * Shrink this container's children along the main axis.
     * <p>
     * Horizontal main axis: shrink width, Vertical main axis: shrink height.
     * <p>
     * How much each child shrinks is proportional to both their relative sizes and their relative shrink properties.
     * <p>
     * If every child has a default shrink property (1), they shrink only relative to each others size (larger basis child shrinks more).
     * If any child has a non-default shrink property, the children will shrink relative to theirs and each other's size
     * and theirs and each other's shrink property.
     * <p>
     * A shrink property of 0 means a child won't shrink at all.
     *
     * @param freeLength Free space along the main axis, should always be negative if this function is called
     */
    protected void shrinkMainAxis(float freeLength)
    {
        // Total of all children's shrink weights
        float totalShrinkWeight = 0;

        for (GuiElement child : children)
        {
            totalShrinkWeight += (getItemBasis(child) * flexPropertiesMap.get(child).getShrink());
        }

        for (GuiElement child : children)
        {
            // *Not* how much the child will shrink by; have had a hard time trying to visualize this number
            // Can't really describe it better than how much "weight" a child has in the shrink
            float shrinkWeight = getItemBasis(child) * flexPropertiesMap.get(child).getShrink();

            // The actual physical amount this child will shrink by (a negative #, freeLength should always be -)
            // A total shrink weight of 0 means all children have a shrink property of 0 (or a basis of 0),
            // in that case always shrink by 0 to avoid div by 0
            float shrinkAmount = totalShrinkWeight > 0 ? freeLength * (shrinkWeight / totalShrinkWeight) : 0;

            // Clamp new basis between min basis and main axis size
            // (max of main axis size if a child has shrink property of 0;
            // even if a child has a shrink of 0, prevent it from breaking out the side of the container)
            float newBasisClamped = (float) MathUtils.clamp(getItemBasis(child) + shrinkAmount, getItemMinBasis(child), getMainAxisSize());
            setItemBasis(child, newBasisClamped);
        }
    }

    protected void growMainAxis(float freeLength)
    {
//        float totalBasis = 0;
//
//        for (GuiElement child : children)
//        {
//            totalBasis += getItemBasis(child);
//        }
//
//        for (GuiElement child : children)
//        {
//            float growAmount = freeLength * (getItemBasis(child) / totalBasis);
//            setItemBasis(child, Math.min(getItemMaxBasis(child), getItemBasis(child) + growAmount));
//        }

        float totalGrowWeight = 0;

        for (GuiElement child : children)
        {
            totalGrowWeight += (getItemBasis(child) * (flexPropertiesMap.get(child).getGrow() + 1));
        }

        for (GuiElement child : children)
        {
            float growWeight = getItemBasis(child) * (flexPropertiesMap.get(child).getGrow() + 1);
            float growAmount = freeLength * (growWeight / totalGrowWeight);
            System.out.println("par. width: " + getMainAxisSize() + "; growing " + child.elementName
                    + " with g=" + flexPropertiesMap.get(child).getGrow() + " by " + growAmount);

            float newBasis = (flexPropertiesMap.get(child).getGrow() == 0)
                    ? Math.min(getItemMaxBasis(child), getItemBasis(child) + growAmount)
                    : getItemBasis(child) + growAmount;

            setItemBasis(child, newBasis);
        }

//        int totalGrow = getTotalGrowth();
//
//        for (GuiElement child : children)
//        {
//            // default of 0
//            int itemGrow = flexPropertiesMap.get(child).getGrow();
//
//            // if no items have any grow, growth is 0,
//            // also even if total grow is not 0, any item with no grow has a growth of 0: 0/?
//            float growth = (totalGrow != 0) ? (freeLength * ((float) itemGrow / totalGrow)) : 0;
//
//            System.out.println("growing " + child.elementName + " with g=" + itemGrow + " by" + growth);
//
//            setItemBasis(child, Math.max(getItemMaxBasis(child), getItemBasis(child) + growth));
//        }
    }

    /**
     * TODO: desperately need to fix this, causes problems
     * <p>
     * Shrink and grow overflowing items on the cross axis.
     * <p>
     * If an items' max size on the cross axis is greater than the cross axis size,
     * set the item's size to the cross axis size. (trim upon overflow)
     * <p>
     * If an items' max size on the cross axis is less than the cross axis size,
     * set the item's size to its max size. (scale back up post-overflow)
     * <p>
     * This usually needlessly sets the child's size to itself, doing nothing.
     * Probably doesn't matter, resizing isn't a constantly ticking event anyway
     */
    protected void scaleCrossAxisOverflow()
    {
        // TODO: ?
//        float freeLength = getMainAxisSize() - getItemBasesSum();
        float freeCrossAxisLength;

        if (isHorizontal())
        {
            for (GuiElement child : children)
            {
//                // If child is overflowing container height
//                if (child.getHeight() > this.getHeight())
//                {
//                    child.setHeight(this.getHeight());
//                }
//
//                // If child is under container height but its max height is still greater than container height
//                else if (child.getHeight() < this.getHeight() && child.getMaxHeight() >= this.getHeight())
//                {
//                    child.setHeight(this.getHeight());
//                }
//
//                else if (child.getHeight() < this.getHeight() && child.getMaxHeight() < this.getHeight())
//                {
//                    child.setHeight(child.getMaxHeight());
//                }
                child.setHeight(Math.min(child.getMaxHeight(), this.getHeight()));
            }
        }
        else
        {
            for (GuiElement child : children)
            {
                child.setWidth(Math.min(child.getMaxWidth(), this.getWidth()));
            }
        }

//        Consumer<GuiElement> attemptCrossAxisScale = isHorizontal()
//                ? child -> child.setHeight(Math.min(child.getInitialHeight(), this.getHeight()))
//                : child -> child.setWidth(Math.min(child.getInitialWidth(), this.getWidth()));
//        children.forEach(attemptCrossAxisScale);
    }

    protected void justifyMainAxis()
    {
        float allocatedSizePostOverFlowScale = getItemBasesSum();
        float freeSizePostOverFlowScale = getMainAxisSize() - allocatedSizePostOverFlowScale;

//        System.out.println(elementName + " pre scale freeSize: " + freeSize +
//                " post scale: " + allocatedSizePostOverFlowScale + " " + getMainAxisSize());


        if (!isReversed())
        {
            justifyInOrder(allocatedSizePostOverFlowScale, freeSizePostOverFlowScale);
        }
        else
        {
            justifyInReverse(allocatedSizePostOverFlowScale, freeSizePostOverFlowScale);
        }
    }

    protected void justifyInOrder(float allocatedSize, float freeSize)
    {
        float sizeCovered = justifyContent == FlexJustify.CENTER && !isMainAxisFull() ? (freeSize / 2) : 0;

        float sizePerSpace = justifyContent == FlexJustify.BETWEEN ? ((children.size() > 1) ? freeSize / (children.size() - 1) : 0)
                : justifyContent == FlexJustify.AROUND ? freeSize / children.size()
                : justifyContent == FlexJustify.EVENLY ? freeSize / (1 + children.size()) : 0;

        // Justify start by default if no free space
        if (isMainAxisFull() || justifyContent == FlexJustify.START || justifyContent == FlexJustify.CENTER)
        {
            for (GuiElement child : children)
            {
                justifyChildLocalPos(child, sizeCovered);
                sizeCovered += getItemBasis(child);
            }
        }
        else
        {
            switch (justifyContent)
            {
                case END:
                    for (int i = children.size() - 1; i >= 0; i--)
                    {
                        GuiElement child = children.get(i);
                        sizeCovered += getItemBasis(child);
                        justifyChildLocalPos(child, this.getWidth() - sizeCovered);
                    }
                    break;
                case BETWEEN:
                    for (int i = 0; i < children.size(); i++)
                    {
                        GuiElement child = children.get(i);
                        justifyChildLocalPos(child, sizeCovered + (sizePerSpace * i));
                        sizeCovered += getItemBasis(child);
                    }
                    break;
                case AROUND:
                    sizeCovered = sizePerSpace / 2;
                    for (GuiElement child : children)
                    {
                        justifyChildLocalPos(child, sizeCovered);
                        sizeCovered += getItemBasis(child) + sizePerSpace;
                    }
                    break;
                case EVENLY:
                    for (int i = 0; i < children.size(); i++)
                    {
                        GuiElement child = children.get(i);
                        justifyChildLocalPos(child, sizeCovered + (sizePerSpace * (i + 1)));
                        sizeCovered += getItemBasis(child);
                    }
                    break;
            }
        }
    }

    protected void justifyInReverse(float allocatedSize, float freeSize)
    {
        float sizeCovered = justifyContent == FlexJustify.CENTER && !isMainAxisFull() ? (freeSize / 2) : 0;

        float sizePerSpace = justifyContent == FlexJustify.BETWEEN ? ((children.size() > 1) ? freeSize / (children.size() - 1) : 0)
                : justifyContent == FlexJustify.AROUND ? freeSize / children.size()
                : justifyContent == FlexJustify.EVENLY ? freeSize / (1 + children.size()) : 0;

        // Justify start by default if negative free space
        if (isMainAxisFull() || justifyContent == FlexJustify.START || justifyContent == FlexJustify.CENTER)
        {
            for (GuiElement child : children)
            {
                sizeCovered += getItemBasis(child);
                justifyChildLocalPos(child, allocatedSize - sizeCovered);
            }
        }
        else
        {
            switch (justifyContent)
            {
                case END:
                    for (int i = children.size() - 1; i >= 0; i--)
                    {
                        GuiElement child = children.get(i);
                        sizeCovered += getItemBasis(child);
                        justifyChildLocalPos(child, sizeCovered);
                    }
                    break;
                case BETWEEN:
                    for (int i = 0; i < children.size(); i++)
                    {
                        GuiElement child = children.get(i);
                        sizeCovered += getItemBasis(child);
                        justifyChildLocalPos(child, allocatedSize - sizeCovered - (sizePerSpace * i));
                    }
                    break;
                case AROUND:
                    sizeCovered = sizePerSpace / 2;
                    for (GuiElement child : children)
                    {
                        sizeCovered += getItemBasis(child) + sizePerSpace;
                        justifyChildLocalPos(child, sizeCovered);
                    }
                    break;
                case EVENLY:
                    for (int i = 0; i < children.size(); i++)
                    {
                        GuiElement child = children.get(i);
                        sizeCovered += getItemBasis(child);
                        justifyChildLocalPos(child, allocatedSize - sizeCovered - (sizePerSpace * (i + 1)));
                    }
                    break;
            }
        }
    }

    protected void alignCrossAxis()
    {
        for (GuiElement child : children)
        {
            FlexAlign alignment = getItemAlign(child);

            switch (alignment)
            {
                case START:
                    // stretch(child, true);
                    alignChildLocalPos(child, 0);
                    break;
                case END:
                    // stretch(child, true);
                    alignChildLocalPos(child, isHorizontal() ? this.getHeight() - child.getHeight() : this.getWidth() - child.getWidth());
                    break;
                case CENTER:
                    // stretch(child, true);
                    float pos = isHorizontal() ? (this.getHeight() / 2) - (child.getHeight() / 2) : (this.getWidth() / 2) - (child.getWidth() / 2);
                    alignChildLocalPos(child, pos);
                    break;
                case STRETCH:
                    // TODO: on direction change from row to col, reset all children dimens. to initials?
                    //  otherwise elements remain stretched on prev axis after flip?
                    if (isHorizontal())
                    {
                        child.setHeight(this.getHeight());
                    }
                    else
                    {
                        child.setWidth(this.getWidth());
                    }
                    break;
            }
        }
    }

    protected void justifyChildLocalPos(GuiElement child, float pos)
    {
        switch (direction)
        {
            case ROW:
            case ROW_REVERSE:
                child.setLocalXPos(pos);
                break;
            case COLUMN:
            case COLUMN_REVERSE:
                child.setLocalYPos(pos);
                break;
        }
    }

    protected void alignChildLocalPos(GuiElement child, float pos)
    {
        switch (direction)
        {
            case ROW:
            case ROW_REVERSE:
                child.setLocalYPos(pos);
                break;
            case COLUMN:
            case COLUMN_REVERSE:
                child.setLocalXPos(pos);
                break;
        }
    }

    // TODO: debug
    private void drawArrow(FlexDirection direction, Color color, float headSize)
    {
        float tailX = 0;
        float tailY = 0;
        float headX = 0;
        float headY = 0;
        float[] headEdge1 = new float[2];
        float[] headEdge2 = new float[2];

        switch (direction)
        {
            case ROW:
                tailY = headY = worldYPos() + (getHeight() / 2);
                tailX = worldXPos();
                headX = tailX + getWidth();
                headEdge1 = new float[]{headX - headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY + headSize};
                break;
            case ROW_REVERSE:
                tailY = headY = worldYPos() + (getHeight() / 2);
                headX = worldXPos();
                tailX = headX + getWidth();
                headEdge1 = new float[]{headX + headSize, headY + headSize};
                headEdge2 = new float[]{headX + headSize, headY - headSize};
                break;
            case COLUMN:
                tailX = headX = worldXPos() + (getWidth() / 2);
                tailY = worldYPos();
                headY = tailY + getHeight();
                headEdge1 = new float[]{headX + headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY - headSize};
                break;
            case COLUMN_REVERSE:
                tailX = headX = worldXPos() + (getWidth() / 2);
                headY = worldYPos();
                tailY = headY + getHeight();
                headEdge1 = new float[]{headX - 10, headY + 10};
                headEdge2 = new float[]{headX + 10, headY + 10};
                break;
        }

//        System.out.printf("{tX:%s, tY:%s}, {hX:%s, hY:%s}%n", arrowTailX, arrowTailY, arrowHeadX, arrowHeadY);
        RenderUtils.drawVertices(GL11.GL_POLYGON, new float[][]{{headX, headY}, headEdge1, headEdge2}, color);
        RenderUtils.drawVertices(GL11.GL_LINE_STRIP, new float[][]{{tailX, tailY}, {headX, headY}}, color);
    }
}

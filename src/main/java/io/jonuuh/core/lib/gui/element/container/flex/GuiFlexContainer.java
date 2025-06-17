package io.jonuuh.core.lib.gui.element.container.flex;

import io.jonuuh.core.lib.gui.element.GuiElement;
import io.jonuuh.core.lib.gui.element.container.GuiContainer;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexAlign;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexDirection;
import io.jonuuh.core.lib.gui.element.container.flex.properties.FlexJustify;
import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiFlexContainer extends GuiContainer
{
    protected final List<FlexItem> flexItems;
    protected FlexDirection direction;
    protected FlexJustify justifyContent;
    protected FlexAlign alignItems;
//    protected boolean isMainAxisFull; // TODO: track scaling within resizeItems to maintain flag?

    public GuiFlexContainer(String elementName, float xPos, float yPos, float width, float height, Map<GuiColorType, Color> colorMap)
    {
        super(elementName, xPos, yPos, width, height, colorMap);
        this.flexItems = new ArrayList<>();
        this.direction = FlexDirection.ROW;
        this.justifyContent = FlexJustify.START;
        this.alignItems = FlexAlign.START;
    }

    public GuiFlexContainer(String elementName, float xPos, float yPos, float width, float height)
    {
        this(elementName, xPos, yPos, width, height, null);
    }

    public FlexDirection getDirection()
    {
        return direction;
    }

    public GuiFlexContainer setDirection(FlexDirection direction)
    {
        this.direction = direction;
        return this;
    }

    public FlexJustify getJustifyContent()
    {
        return justifyContent;
    }

    public GuiFlexContainer setJustifyContent(FlexJustify justifyContent)
    {
        this.justifyContent = justifyContent;
        return this;
    }

    public FlexAlign getAlignItems()
    {
        return alignItems;
    }

    public GuiFlexContainer setAlignItems(FlexAlign alignItems)
    {
        this.alignItems = alignItems;
        return this;
    }

    /**
     * Retrieve the FlexItem wrapping the given GuiElement in this GuiFlexContainer
     */
    public FlexItem getFlexItem(GuiElement element)
    {
        for (FlexItem item : flexItems)
        {
            if (item.getElement().equals(element))
            {
                return item;
            }
        }
        return null;
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
        return isHorizontal()
                ? getWidth() - (getPadding().getLeft() + getPadding().getRight())
                : getHeight() - (getPadding().getTop() + getPadding().getBottom());
    }

    public float getCrossAxisSize()
    {
        return isHorizontal()
                ? getHeight() - (getPadding().getTop() + getPadding().getBottom())
                : getWidth() - (getPadding().getLeft() + getPadding().getRight());
    }

    /**
     * Adds a child to this container's list of children, but NOT to this GuiFlexContainer's list of flex items
     * <p>
     * For a GuiFlexContainer {@link GuiFlexContainer#addItem(FlexItem)} should almost always be used instead!
     */
    @Deprecated
    @Override
    public void addChild(GuiElement child)
    {
        super.addChild(child);
    }

    /**
     * @see GuiFlexContainer#addChild(GuiElement)
     */
    @Deprecated
    @Override
    public void addChildren(Collection<GuiElement> children)
    {
        super.addChildren(children);
    }

    /**
     * @see GuiFlexContainer#addChild(GuiElement)
     */
    @Deprecated
    @Override
    public void addChildren(GuiElement... children)
    {
        super.addChildren(children);
    }

    public void addItem(FlexItem item)
    {
        super.addChild(item.getElement());
        flexItems.add(item);
    }

    public void addItems(Collection<FlexItem> items)
    {
        for (FlexItem item : items)
        {
            addItem(item);
        }
    }

    public void addItems(FlexItem... items)
    {
        for (FlexItem item : items)
        {
            addItem(item);
        }
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), getColor(GuiColorType.BACKGROUND));
//        RenderUtils.drawNineSliceTexturedRect(resourceGenericBackgroundTex,
//                worldXPos(), worldYPos(), zLevel - 90, getWidth(), getHeight(), 52, 52, 12, 12);

        if (debug)
        {
            drawArrow(direction, new Color("FFD700"), 8);

            FlexDirection alignDir = (direction == FlexDirection.ROW || direction == FlexDirection.ROW_REVERSE) ? FlexDirection.COLUMN : FlexDirection.ROW;
            drawArrow(alignDir, new Color(EnumChatFormatting.WHITE), 5);

            String info = this.elementName;
//        String info = direction.toString().toLowerCase() + ", " + justifyContent.toString().toLowerCase() + ", " + alignItems.toString().toLowerCase();
            float textX = worldXPos() + (getWidth() / 4) - (mc.fontRendererObj.getStringWidth(info) / 2F);
            float textY = worldYPos() + getHeight() - mc.fontRendererObj.FONT_HEIGHT;
            mc.fontRendererObj.drawString(info, textX, textY, getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
        }

        super.onScreenDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void onInitGui(ScaledResolution scaledResolution)
    {
        updateItemsLayout();
    }

    public void updateItemsLayout()
    {
        if (flexItems.isEmpty())
        {
            return;
        }

        // should fix a few small problems (e.g. size reset is for post-stretch and direction change)
        // forget what pos reset is for
        for (FlexItem item : flexItems)
        {
            GuiElement element = item.getElement();
            element.setLocalXPos(0);
            element.setLocalYPos(0);
            element.setWidth(item.getInitWidth());
            element.setHeight(item.getInitHeight());
        }

        // The total free space along the main axis gained or lost SINCE the last resize
        // Should be positive when free space is available and negative when items are overflowing
        float freeLength = getMainAxisSize() - getItemBasesSum();

        if (freeLength != 0)
        {
            ResizeType resizeType = (freeLength < 0) ? ResizeType.SHRINK : ResizeType.GROW;
            resizeMainAxisItems(freeLength, resizeType);
        }

        justifyMainAxis();

        scaleCrossAxisOverflow();
        alignCrossAxis();
    }

    // note: if an element has a basis of 0, it won't ever grow - is this a problem?
    // what if an element is shrunk to the point of being 0 and then cannot grow back up again
    protected void resizeMainAxisItems(float freeLength, ResizeType resizeType)
    {
        Map<FlexItem, Float> resizeWeights = new HashMap<>();

        // Total of all item's resize weights
        float totalResizeWeight = 0;

        for (FlexItem item : flexItems)
        {
            resizeWeights.put(item, getItemResizeWeight(item, resizeType));
            totalResizeWeight += resizeWeights.get(item);
        }

        // A total resize weight of 0 means all items have a shrink property of 0,
        // or a grow property of 0 (or a basis of 0): in which case nothing needs to be resized
        if (totalResizeWeight <= 0)
        {
            return;
        }

        for (FlexItem item : flexItems)
        {
            // *Not* how much the child will shrink/grow by; have had a hard time trying to visualize this number
            // Can't really describe it better than how much "weight" a child has in the shrink/grow
            float resizeWeight = resizeWeights.get(item);

            // The actual physical amount this child will shrink/grow by
            // (Shrink: A negative #, freeLength should always be - during a shrink)
            // (Grow: A positive #, freeLength should always be + during a grow)
            float resizeAmount = freeLength * (resizeWeight / totalResizeWeight);
            float currBasisResized = getItemBasis(item) + resizeAmount;
            float newBasis;

            if (resizeType.equals(ResizeType.SHRINK))
            {
                // Prevent new basis from going below min basis TODO: test that this works correctly (item should break out of bounds of its container if shrinking container below item min basis?)
                newBasis = Math.max(currBasisResized, getItemMinBasis(item));
            }
            else
            {
                // Prevent new basis from going above max basis TODO: how does container size work here? worried about breaking out of side, etc?
                newBasis = Math.min(currBasisResized, getItemMaxBasis(item));
            }

            setItemBasis(item, newBasis);
        }
    }

    /**
     * Spaces the items out along the main axis according to this containers' {@link GuiFlexContainer#justifyContent} property
     * <p>
     * This does not change the widths or heights of any elements, only the x or y positions of the items in the container.
     */
    protected void justifyMainAxis()
    {
        float freeLength = getMainAxisSize() - getItemBasesSum();
        boolean isMainAxisFull = freeLength == 0; // TODO: why does this work w/out floating point error correction - check in debug?

        float defaultPadding = isHorizontal() ? getPadding().getLeft() : getPadding().getTop();

        // Justify from start by default
        float currentPos = isReversed() ? defaultPadding + freeLength : defaultPadding;

        // If no free space, don't change starting pos
        // (e.g. if justifyContent=CENTER but no free space, use default starting pos of justifyContent=START)
        if (!isMainAxisFull)
        {
            switch (justifyContent)
            {
                case END:
                    currentPos = isReversed() ? defaultPadding : defaultPadding + freeLength;
                    break;
                case CENTER:
                    currentPos = defaultPadding + (freeLength / 2F);
                    break;
                case BETWEEN:
                    currentPos = defaultPadding;
                    break;
                case AROUND:
                    currentPos = defaultPadding + ((freeLength / flexItems.size()) / 2);
                    break;
                case EVENLY:
                    currentPos = defaultPadding + (freeLength / (flexItems.size() + 1));
                    break;
            }
        }

        int i = isReversed() ? flexItems.size() - 1 : 0;

        // TODO: implement flex item order property in here?
        while (isReversed() ? i >= 0 : i < flexItems.size())
        {
            FlexItem item = flexItems.get(i);
            justifyChildLocalPos(item, currentPos);
            currentPos += getItemBasis(item);

            if (!isMainAxisFull)
            {
                switch (justifyContent)
                {
                    case BETWEEN:
                        currentPos += (flexItems.size() > 1 ? freeLength / (flexItems.size() - 1) : 0);
                        //                  ^ prevent div by zero if only one item in flexItems
                        break;
                    case EVENLY:
                        currentPos += (freeLength / (flexItems.size() + 1));
                        break;
                    case AROUND:
                        currentPos += (freeLength / (flexItems.size()));
                        break;
                }
            }

            i += isReversed() ? -1 : 1;
        }
    }

    /**
     * Aligns the items along the cross axis according to this containers' {@link GuiFlexContainer#alignItems} property
     * <p>
     * This can change the widths or heights of items if the container or an item has a FlexAlign property of stretch
     */
    protected void alignCrossAxis()
    {
        float paddingOffset = isHorizontal() ? getPadding().getTop() : getPadding().getLeft();

        for (FlexItem item : flexItems)
        {
            FlexAlign itemAlignment = (item.getAlign() != null) ? item.getAlign() : alignItems;

            switch (itemAlignment)
            {
                case START:
                    alignChildLocalPos(item, paddingOffset);
                    break;
                case END:
                    alignChildLocalPos(item, getCrossAxisSize() - getItemCrossAxisLength(item) + paddingOffset);
                    break;
                case CENTER:
                    alignChildLocalPos(item, (getCrossAxisSize() / 2) - (getItemCrossAxisLength(item) / 2) + paddingOffset);
                    break;
                case STRETCH:
                    alignChildLocalPos(item, paddingOffset);
                    setItemCrossAxisLength(item, getCrossAxisSize());
                    break;
            }
        }
    }

    /**
     * TODO: desperately need to fix this, causes problems
     * TODO: was past me just referring to the situation with dropdowns being expanded on window resize?
     *  was this fixed by element min/max implementation?
     * TODO: might make sense to loop over children instead, scrollbars might only be relevant on cross axis and
     *  thus this could be used naturally to prevent them overflowing parent?
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
        for (FlexItem item : flexItems)
        {
            float crossAxisSize = getCrossAxisSize();

            // If item is under or equal to container height
            if (getItemCrossAxisLength(item) <= crossAxisSize)
            {
                float itemInitCrossAxisLength = isHorizontal() ? item.getInitHeight() : item.getInitWidth();

                // If item initial height is greater than container height
                // (grow item back up after it was previously cropped down on cross axis)
                if (itemInitCrossAxisLength > crossAxisSize /*&& item.getMaxHeight() <= this.getHeight()*/)
                {                                            // ^ stylistic decision: should item max dimens. only be enforced along main axis? might make most sense that way
                    setItemCrossAxisLength(item, crossAxisSize);
                }
            }
            // Else: if item is greater than container height, crop to container height
            else
            {
                setItemCrossAxisLength(item, crossAxisSize);
            }
        }
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
    protected float getItemMinBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getMinWidth() : item.getMinHeight());
    }

    protected float getItemMaxBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getMaxWidth() : item.getMaxHeight());
    }

    protected float getItemBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getElement().getWidth() : item.getElement().getHeight());
    }

    protected void setItemBasis(FlexItem item, float basis)
    {
        GuiElement element = item.getElement();
        if (isHorizontal())
        {
            element.setWidth(basis);
            return;
        }
        element.setHeight(basis);
    }

    protected float getItemBasesSum()
    {
        return (float) flexItems.stream().mapToDouble(this::getItemBasis).sum();
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
    protected float getItemCrossAxisLength(FlexItem item)
    {
        return (isHorizontal() ? item.getElement().getHeight() : item.getElement().getWidth());
    }

    protected void setItemCrossAxisLength(FlexItem item, float basis)
    {
        GuiElement element = item.getElement();
        if (isHorizontal())
        {
            element.setHeight(basis);
            return;
        }
        element.setWidth(basis);
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
    protected float getItemResizeWeight(FlexItem item, ResizeType resizeType)
    {
        boolean isShrink = resizeType.equals(ResizeType.SHRINK);
        return getItemBasis(item) * (isShrink ? item.getShrink() : item.getGrow());
    }

    protected void justifyChildLocalPos(FlexItem item, float pos)
    {
        switch (direction)
        {
            case ROW:
            case ROW_REVERSE:
                item.getElement().setLocalXPos(pos);
                break;
            case COLUMN:
            case COLUMN_REVERSE:
                item.getElement().setLocalYPos(pos);
                break;
        }
    }

    protected void alignChildLocalPos(FlexItem item, float pos)
    {
        switch (direction)
        {
            case ROW:
            case ROW_REVERSE:
                item.getElement().setLocalYPos(pos);
                break;
            case COLUMN:
            case COLUMN_REVERSE:
                item.getElement().setLocalXPos(pos);
                break;
        }
    }

    protected enum ResizeType
    {
        SHRINK,
        GROW
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

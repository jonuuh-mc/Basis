package io.jonuuh.basis.lib.gui.element.container.behavior;

import io.jonuuh.basis.lib.gui.element.ElementUtils;
import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.container.FlexItem;
import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.lib.gui.properties.FlexAlign;
import io.jonuuh.basis.lib.gui.properties.FlexDirection;
import io.jonuuh.basis.lib.gui.properties.FlexJustify;
import io.jonuuh.basis.lib.gui.properties.Spacing;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexBehavior
{
    private final GuiContainer host;
    protected final List<FlexItem> flexItems;
    protected FlexDirection direction;
    protected FlexJustify justify;
    protected FlexAlign align;

    protected boolean doResize;
    protected boolean doJustify;
    protected boolean doAlign;
    //    protected List<FlexItem> resizeExcludes;
//    protected List<FlexItem> justifyExcludes;
//    protected List<FlexItem> alignExcludes;
    protected Float mainAxisSize;

    protected FlexBehavior(Builder builder)
    {
//        this.container = container;
        this.flexItems = builder.flexItems;
//        addItems(builder.flexItems);
//        this.flexItems = new ArrayList<>();
////        addItems(builder.flexItems);
        this.direction = builder.direction;
        this.justify = builder.justify;
        this.align = builder.align;

        this.doResize = builder.doResize;
        this.doJustify = builder.doJustify;
        this.doAlign = builder.doAlign;

        this.mainAxisSize = builder.mainAxisSize;

        this.host = builder.host;
    }

    public GuiContainer getHost()
    {
        return host;
    }

    public Spacing getHostPadding()
    {
        return getHost().getPadding();
    }

    public FlexDirection getDirection()
    {
        return direction;
    }

    public FlexBehavior setDirection(FlexDirection direction)
    {
        this.direction = direction;
        return this;
    }

    public FlexJustify getJustifyContent()
    {
        return justify;
    }

    public FlexBehavior setJustifyContent(FlexJustify justify)
    {
        this.justify = justify;
        return this;
    }

    public FlexAlign getAlignItems()
    {
        return align;
    }

    public FlexBehavior setAlignItems(FlexAlign align)
    {
        this.align = align;
        return this;
    }

    /**
     * Retrieve the FlexItem wrapping the given GuiElement in this FlexBehavior
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

    public List<GuiElement> getElements()
    {
        List<GuiElement> elements = new ArrayList<>();
        for (FlexItem item : flexItems)
        {
            elements.add(item.getElement());
        }
        return elements;
    }

    public void addItem(FlexItem item)
    {
        addItem(item, flexItems.size());
    }

    public void addItem(FlexItem item, int index)
    {
        getHost().addChild(item.getElement(), index);
        flexItems.add(index, item);
    }

    public void removeItem(FlexItem item)
    {
        getHost().removeChild(item.getElement());
        flexItems.remove(item);
    }

    public void updateItemsLayout()
    {
        if (flexItems.isEmpty())
        {
            return;
        }

        // Reset position and size of items before running flex algorithm
        // Size reset is for direction changes while items are currently align-stretched
        for (FlexItem item : flexItems)
        {
            GuiElement element = item.getElement();
//            element.setLocalXPos(0);
//            element.setLocalYPos(0);
            element.setWidth(item.getInitWidth());
            element.setHeight(item.getInitHeight());
        }

        if (doResize)
        {
            // The total free space along the main axis
            // Should be positive when free space is available and negative when items are overflowing
            float freeLength = getMainAxisSize() - getItemBasesSum();

            if (freeLength != 0)
            {
                ResizeType resizeType = (freeLength < 0) ? ResizeType.SHRINK : ResizeType.GROW;
                resizeMainAxisItems(freeLength, resizeType);
            }
        }

        if (doJustify)
        {
            if (getHost().getScrollBehavior() == null)
            {
                justifyMainAxis();
            }
            else
            {
                // TODO: dumb
                float sliderValue = getHost().getScrollBehavior().getSlider().getNormalizedValue();
                getHost().getScrollBehavior().getSlider().setNormalizedValue(0);
                justifyMainAxis();
                getHost().getScrollBehavior().getSlider().setNormalizedValue(sliderValue);
            }
        }

        if (doAlign)
        {
            scaleCrossAxisOverflow(); // TODO:
            alignCrossAxis();
        }
    }

    // note: if an element has a basis of 0, it won't ever grow - is this a problem?
    // what if an element is shrunk to the point of being 0 and then cannot grow back up again
    private void resizeMainAxisItems(float freeLength, ResizeType resizeType)
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

//        List<FlexItem> flaggedItems = new ArrayList<>();
//
//        if (resizeType.equals(ResizeType.SHRINK))
//        {
//            float redistributedWeight = 0;
//
//            for (FlexItem item : flexItems)
//            {
//                float resizeWeight = resizeWeights.get(item);
//                float resizeLength = freeLength * (resizeWeight / totalResizeWeight);
//
//                if (getItemBasis(item) + resizeLength < getItemMinBasis(item))
//                {
//                    float extraLength = getItemMinBasis(item) - (getItemBasis(item) + resizeLength);
//                    redistributedWeight += (extraLength * totalResizeWeight) / freeLength;
//                    flaggedItems.add(item);
////            System.out.println(item.getElement().elementName + " " + (getItemBasis(item) + resizeLength) + " " + extraLength + " " + redistributedWeight);
//                }
//            }
//
//            for (FlexItem item : resizeWeights.keySet())
//            {
//                if (!flaggedItems.contains(item))
//                {
//                    resizeWeights.put(item, resizeWeights.get(item) + (Math.abs(redistributedWeight) / (flexItems.size() - flaggedItems.size())));
//                }
//            }
//        }

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
     * Spaces the items out along the main axis according to this containers' {@link FlexBehavior#justify} property
     * <p>
     * This does not change the widths or heights of any elements, only the x or y positions of the items in the container.
     */
    private void justifyMainAxis()
    {
        float freeLength = getMainAxisSize() - getItemBasesSum();
        boolean isMainAxisFull = freeLength == 0; // TODO: why does this work w/out floating point error correction - check in debug? edit: can screen only resize by precise fixed amts? e.g. min change = 0.25?

        float defaultPadding = isHorizontal() ? getHostPadding().left() : getHostPadding().top();

        // Justify from start by default
        float currentPos = isReversed() ? defaultPadding + freeLength : defaultPadding;

        // If no free space, don't change starting pos
        // (e.g. if justifyContent=CENTER but no free space, use default starting pos of justifyContent=START)
        if (!isMainAxisFull)
        {
            switch (justify)
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

//        // TODO: sort integer list while maintaining original relative order of null elements?
//        flexItems.sort(Comparator.comparingInt(item -> item.getOrder() != null ? item.getOrder() : 0));

        int i = isReversed() ? flexItems.size() - 1 : 0;

        while (isReversed() ? i >= 0 : i < flexItems.size())
        {
            FlexItem item = flexItems.get(i);
            justifyChildLocalPos(item, currentPos);
            currentPos += getItemBasis(item);

            if (!isMainAxisFull)
            {
                switch (justify)
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
     * Aligns the items along the cross axis according to this containers' {@link FlexBehavior#align} property
     * <p>
     * This can change the widths or heights of items if the container or an item has a FlexAlign property of stretch
     */
    private void alignCrossAxis()
    {
        float paddingOffset = isHorizontal() ? getHostPadding().top() : getHostPadding().left();

        for (FlexItem item : flexItems)
        {
            FlexAlign itemAlignment = (item.getAlign() != null) ? item.getAlign() : align;

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

    // TODO: please fix me...
    private void scaleCrossAxisOverflow()
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
    private boolean isHorizontal()
    {
        return direction == FlexDirection.ROW || direction == FlexDirection.ROW_REVERSE;
    }

    private boolean isReversed()
    {
        return direction == FlexDirection.ROW_REVERSE || direction == FlexDirection.COLUMN_REVERSE;
    }

    protected float getMainAxisSize()
    {
        if (mainAxisSize != null)
        {
//            System.out.println(mainAxisSize);
            return isHorizontal()
                    ? mainAxisSize - getHost().getPadding().left() - getHost().getPadding().right()
                    : mainAxisSize - getHost().getPadding().top() - getHost().getPadding().bottom();
        }
        return isHorizontal() ? ElementUtils.getInnerWidth(getHost()) : ElementUtils.getInnerHeight(getHost());
    }

    private float getCrossAxisSize()
    {
        return isHorizontal() ? ElementUtils.getInnerHeight(getHost()) : ElementUtils.getInnerWidth(getHost());
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
    private float getItemMinBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getMinWidth() : item.getMinHeight());
    }

    private float getItemMaxBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getMaxWidth() : item.getMaxHeight());
    }

    private float getItemBasis(FlexItem item)
    {
        return (isHorizontal() ? item.getElement().getWidth() : item.getElement().getHeight());
    }

    private void setItemBasis(FlexItem item, float basis)
    {
        GuiElement element = item.getElement();
        if (isHorizontal())
        {
            element.setWidth(basis);
            return;
        }
        element.setHeight(basis);
    }

    private float getItemBasesSum()
    {
        return (float) flexItems.stream().mapToDouble(this::getItemBasis).sum();
    }

    // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
    private float getItemCrossAxisLength(FlexItem item)
    {
        return (isHorizontal() ? item.getElement().getHeight() : item.getElement().getWidth());
    }

    private void setItemCrossAxisLength(FlexItem item, float basis)
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
    private float getItemResizeWeight(FlexItem item, ResizeType resizeType)
    {
        boolean isShrink = resizeType.equals(ResizeType.SHRINK);
        return getItemBasis(item) * (isShrink ? item.getShrink() : item.getGrow());
    }

    private void justifyChildLocalPos(FlexItem item, float pos)
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

    private void alignChildLocalPos(FlexItem item, float pos)
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

    public void drawInspector()
    {
        Minecraft mc = Minecraft.getMinecraft();
        drawArrow(getDirection(), new Color("FFD700"), 8);

        FlexDirection alignDir = (getDirection() == FlexDirection.ROW || getDirection() == FlexDirection.ROW_REVERSE) ? FlexDirection.COLUMN : FlexDirection.ROW;
        drawArrow(alignDir, new Color(EnumChatFormatting.WHITE), 5);

        String info = direction.toString() + ", " + justify.toString() + ", " + align.toString();
        float textX = ElementUtils.getInnerLeftBound(getHost()) + 1;
        float textY = ElementUtils.getInnerBottomBound(getHost()) - mc.fontRendererObj.FONT_HEIGHT;
        mc.fontRendererObj.drawString(info, textX, textY, -1, true);
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

        float hostX = ElementUtils.getInnerLeftBound(getHost());
        float hostY = ElementUtils.getInnerTopBound(getHost());
        float hostWidth = ElementUtils.getInnerWidth(getHost());
        float hostHeight = ElementUtils.getInnerHeight(getHost());

        switch (direction)
        {
            case ROW:
                tailY = headY = hostY + (hostHeight / 2);
                tailX = hostX;
                headX = tailX + hostWidth;
                headEdge1 = new float[]{headX - headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY + headSize};
                break;
            case ROW_REVERSE:
                tailY = headY = hostY + (hostHeight / 2);
                headX = hostX;
                tailX = headX + hostWidth;
                headEdge1 = new float[]{headX + headSize, headY + headSize};
                headEdge2 = new float[]{headX + headSize, headY - headSize};
                break;
            case COLUMN:
                tailX = headX = hostX + (hostWidth / 2);
                tailY = hostY;
                headY = tailY + hostHeight;
                headEdge1 = new float[]{headX + headSize, headY - headSize};
                headEdge2 = new float[]{headX - headSize, headY - headSize};
                break;
            case COLUMN_REVERSE:
                tailX = headX = hostX + (hostWidth / 2);
                headY = hostY;
                tailY = headY + hostHeight;
                headEdge1 = new float[]{headX - 10, headY + 10};
                headEdge2 = new float[]{headX + 10, headY + 10};
                break;
        }

        RenderUtils.drawVertices(GL11.GL_POLYGON, new float[][]{{headX, headY}, headEdge1, headEdge2}, color);
        RenderUtils.drawVertices(GL11.GL_LINE_STRIP, new float[][]{{tailX, tailY}, {headX, headY}}, color);
    }

    private enum ResizeType
    {
        SHRINK,
        GROW
    }

    public static class Builder
    {
        protected GuiContainer host = null;
        protected final List<FlexItem> flexItems = new ArrayList<>();
        protected FlexDirection direction = FlexDirection.ROW;
        protected FlexJustify justify = FlexJustify.START;
        protected FlexAlign align = FlexAlign.START;

        protected boolean doResize = true;
        protected boolean doJustify = true;
        protected boolean doAlign = true;

        // TODO: implement this
        //  should maybe be lists of elementNames rather than flexitems
        //  should make heavily chained building impossible (requires pre-making item)
        //  if a reference to the item is necessary in this way
//        protected List<FlexItem> resizeExcludes = new ArrayList<>();
//        protected List<FlexItem> justifyExcludes = new ArrayList<>();
//        protected List<FlexItem> alignExcludes = new ArrayList<>();

        protected Float mainAxisSize = null;

        public Builder host(GuiContainer host)
        {
            this.host = host;
            return this;
        }

        public Builder item(FlexItem item)
        {
            this.flexItems.add(item);
            return this;
        }

        public Builder items(Collection<FlexItem> items)
        {
            this.flexItems.addAll(items);
            return this;
        }

        public Builder items(FlexItem... items)
        {
            this.flexItems.addAll(Arrays.asList(items));
            return this;
        }

        public Builder direction(FlexDirection direction)
        {
            this.direction = direction;
            return this;
        }

        public Builder justify(FlexJustify justify)
        {
            this.justify = justify;
            return this;
        }

        public Builder align(FlexAlign align)
        {
            this.align = align;
            return this;
        }

        public Builder doResize(boolean doResize)
        {
            this.doResize = doResize;
            return this;
        }

        public Builder doJustify(boolean doJustify)
        {
            this.doJustify = doJustify;
            return this;
        }

        public Builder doAlign(boolean doAlign)
        {
            this.doAlign = doAlign;
            return this;
        }

        public Builder mainAxisSize(float mainAxisSize)
        {
            this.mainAxisSize = mainAxisSize;
            return this;
        }

        public FlexBehavior build()
        {
            return new FlexBehavior(this);
        }
    }
}

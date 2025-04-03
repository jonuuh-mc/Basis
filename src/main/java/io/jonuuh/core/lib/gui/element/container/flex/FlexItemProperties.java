package io.jonuuh.core.lib.gui.element.container.flex;

public class FlexItemProperties
{
    private int order;
    private int grow;
    private int shrink;
    //    private float basis;
    private FlexAlign align;

    public FlexItemProperties(int order, int grow, int shrink, /*float basis,*/ FlexAlign align)
    {
        setOrder(order);
        setGrow(grow);
        setShrink(shrink);
//        setBasis(basis);
        setAlign(align);
    }

    public FlexItemProperties()
    {
        this(0, 0, 1, /*0,*/ null);
    }

    public int getOrder()
    {
        return order;
    }

    public FlexItemProperties setOrder(int order)
    {
        this.order = order;
        return this;
    }

    public int getGrow()
    {
        return grow;
    }

    public FlexItemProperties setGrow(int grow)
    {
        this.grow = Math.max(grow, 0);
        return this;
    }

    public int getShrink()
    {
        return shrink;
    }

    public FlexItemProperties setShrink(int shrink)
    {
        this.shrink = Math.max(shrink, 0);
        return this;
    }

//    public float getBasis()
//    {
//        return basis;
//    }
//
//    public FlexItemProperties setBasis(float basis)
//    {
//        this.basis = basis;
//        return this;
//    }

    public FlexAlign getAlign()
    {
        return align;
    }

    public FlexItemProperties setAlign(FlexAlign align)
    {
        this.align = align;
        return this;
    }
}

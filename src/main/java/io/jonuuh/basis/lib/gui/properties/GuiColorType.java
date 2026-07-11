package io.jonuuh.basis.lib.gui.properties;

/**
 * An attempt at having some organized way to tell elements how to color which part of themselves.
 * <p>
 * Problem is these terms are maybe too generic, and it's up to each element to decide
 * which of these it should use and what it should use it for.
 * <p>
 * For example, a hypothetical: ACCENT1 could be used by a slider to color its pointer,
 * while a checkbox could use it to color its checkmark icon.
 * <p>
 * This means anyone creating an instance of an element needs to actually go and read the internals
 * of that element to know which color types would have an effect and what effect they would have.
 * <p>
 * That isn't really a huge problem since I'm the only one using this library, but it still
 * should be designed far better.
 * <p>
 * This usage is connected to the (currently implemented) design that GuiColorTypes are inherited.
 * The motivation behind that was to remove the need to reapply common colors to every element
 * down a long tree of elements.
 * <p>
 * If colors are generally reused through a whole tree (which they are, that's what a color palette is),
 * you can just apply some colors to some upstream element, then downstream elements can walk up the tree,
 * fetch, and use it.
 * <p>
 * In practice, this design has been questionable:
 * - Worst case is where one element midway along a tree should use a different color than the inherited one,
 * which requires that new color to be applied to that one element, and then the usual inherited color
 * to be reapplied to all that odd element's descendants (otherwise they would inherit that odd element's odd color).
 * - This has been somewhat rare so far, the more common case is just that a color needs to be redefined at some
 * arbitrary point in a tree, but then it's fine for downstream elements to inherit it. Looking at some code laying
 * out an element tree, things like this leads to fragmented, haphazard calls to .color() in GuiElement's Builder,
 * whose pattern is not at all easily inferred.
 * - In general tl;dr:, code where element trees are laid out just generally has too many hidden details involving colors.
 * Even for the author of the lib and the author of the code laying out an element tree (me), it's hard to keep track of
 * what element is colored in what way
 */
public enum GuiColorType
{
    BASE,
    ACCENT1,
    ACCENT2,
    BACKGROUND,
    BORDER,
    BASE_HOVER,
    ACCENT_HOVER
}

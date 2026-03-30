package io.jonuuh.basis.lib.gui.element;

// TODO: likely should move all bounding calculations into base GuiElement, we'll see

/**
 * <pre>
 * {@code
 *
 *                       Left Pad                                         Right Pad
 *                       ◀───────▶                                        ◀───────▶
 *
 *                     World    Inner                                   Inner
 *                     X Pos    Left                                    Right
 *                       │       │                                        │
 *
 *                       │       │                                        │
 *        ▲   World ─ ─ ─╔════════════════════════════════════════════════════════╗─ ─ ─         ▲
 *        │   Y Pos      ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *    Top │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *    Pad │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *        │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *        ▼   Inner ─ ─ ─║▒▒▒▒▒▒▒╔════════════════════════════════════════╗▒▒▒▒▒▒▒║─ ─ ─    ▲  Height
 *            Top        ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║         │    │
 *                       ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║         │    │
 *                       ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║       Inner  │
 *                       ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║      Height  │
 *                       ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║         │    │
 *                       ║▒▒▒▒▒▒▒║████████████████████████████████████████║▒▒▒▒▒▒▒║         │    │
 *        ▲   Inner ─ ─ ─║▒▒▒▒▒▒▒╚════════════════════════════════════════╝▒▒▒▒▒▒▒║─ ─ ─    ▼    │
 *        │   Bottom     ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 * Bottom │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *    Pad │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *        │              ║▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒║              │
 *        ▼              ╞═══════╤════════════════════════════════════════╤═══════╝              ▼
 *
 *                       │       │                                        │
 *
 *                               ◀───────────────Inner─Width──────────────▶
 *
 *                       ◀──────────────────────────Width─────────────────────────▶
 *
 * }
 * </pre>
 */
public final class ElementUtils
{
    public static float getInnerLeftBound(GuiElement element)
    {
        return element.worldXPos() + element.getPadding().left();
    }

    public static float getInnerRightBound(GuiElement element)
    {
        return element.worldXPos() + element.getWidth() - element.getPadding().right();
    }

    public static float getInnerTopBound(GuiElement element)
    {
        return element.worldYPos() + element.getPadding().top();
    }

    public static float getInnerBottomBound(GuiElement element)
    {
        return element.worldYPos() + element.getHeight() - element.getPadding().bottom();
    }

    public static float getInnerWidth(GuiElement element)
    {
        return getInnerRightBound(element) - getInnerLeftBound(element);
    }

    public static float getInnerHeight(GuiElement element)
    {
        return getInnerBottomBound(element) - getInnerTopBound(element);
    }

//    public static float getOuterLeftBound(GuiElement element)
//    {
//        return element.worldXPos() - element.getMargin().left();
//    }
//
//    public static float getOuterRightBound(GuiElement element)
//    {
//        return element.worldXPos() + element.getWidth() + element.getMargin().right();
//    }
//
//    public static float getOuterTopBound(GuiElement element)
//    {
//        return element.worldYPos() - element.getMargin().top();
//    }
//
//    public static float getOuterBottomBound(GuiElement element)
//    {
//        return element.worldYPos() + element.getHeight() + element.getMargin().bottom();
//    }
//
//    public static float getOuterWidth(GuiElement element)
//    {
//        return getOuterRightBound(element) - getOuterLeftBound(element);
//    }
//
//    public static float getOuterHeight(GuiElement element)
//    {
//        return getOuterBottomBound(element) - getOuterTopBound(element);
//    }
}

package io.jonuuh.core.lib.config.gui.elements;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class GuiTextFieldVanilla
{
//    private final int id;
    private final FontRenderer fontRendererInstance;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    /**
     * Has the current text being edited on the textbox.
     */
    private String text = "";
    /**
     * maximum number of character that can be contained in this textbox
     */
    private int maxStringLength = 32;
    /**
     * updated every tick, when ! % 6 the "cursor" will be drawn (flashing "cursor")
     */
    private int cursorCounter;
    /**
     * enable drawing background and outline
     */
    private boolean enableBackgroundDrawing = true;
    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus = true;
    /**
     * If this value is true along with isEnabled, keyTyped will process the keys.
     */
    private boolean isFocused;
    /**
     * If this value is true along with isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled = true;
    /**
     * The current character index that should be used as start of the rendered text.
     */
    private int lineScrollOffset;
    /**
     * The current position of the cursor
     */
    private int cursorPosition;
    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    private boolean visible = true;
//    private GuiPageButtonList.GuiResponder guiResponder;
    private Predicate<String> validator = Predicates.alwaysTrue();

    public GuiTextFieldVanilla(FontRenderer fontRendererObj, int xPos, int yPos, int width, int height)
    {
//        this.id = id;
        this.fontRendererInstance = fontRendererObj;
        this.xPosition = xPos;
        this.yPosition = yPos;
        this.width = width;
        this.height = height;
    }

//    public void setGuiResponder(GuiPageButtonList.GuiResponder guiResponder)
//    {
//        this.guiResponder = guiResponder;
//    }
//
//    public void setMaxStringLength(int p_146203_1_)
//    {
//        maxStringLength = p_146203_1_;
//
//        if (text.length() > p_146203_1_)
//        {
//            text = text.substring(0, p_146203_1_);
//        }
//    }
//
//    public int getMaxStringLength()
//    {
//        return maxStringLength;
//    }
//
//    public int getCursorPosition()
//    {
//        return cursorPosition;
//    }
//
//    public boolean getEnableBackgroundDrawing()
//    {
//        return enableBackgroundDrawing;
//    }
//
//    public void setEnableBackgroundDrawing(boolean enableBackgroundDrawing)
//    {
//        this.enableBackgroundDrawing = enableBackgroundDrawing;
//    }
//
//    /**
//     * Sets the text colour for this textbox (disabled text will not use this colour)
//     */
//    public void setTextColor(int enabledColor)
//    {
//        this.enabledColor = enabledColor;
//    }
//
//    public void setDisabledTextColour(int disabledColor)
//    {
//        this.disabledColor = disabledColor;
//    }

//    public void setFocused(boolean isFocused)
//    {
//        if (isFocused && !this.isFocused)
//        {
//            this.cursorCounter = 0;
//        }
//
//        this.isFocused = isFocused;
//    }

//    public boolean isFocused()
//    {
//        return isFocused;
//    }
//
//    public void setEnabled(boolean p_146184_1_)
//    {
//        this.isEnabled = p_146184_1_;
//    }
//
//    public int getSelectionEnd()
//    {
//        return selectionEnd;
//    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth()
    {
        return enableBackgroundDrawing ? width - 8 : width;
    }

    //    public void setCanLoseFocus(boolean canLoseFocus)
//    {
//        this.canLoseFocus = canLoseFocus;
//    }
//
//    public boolean isVisible()
//    {
//        return visible;
//    }
//
//    public void setVisible(boolean visible)
//    {
//        this.visible = visible;
//    }
//
    public void updateCursorCounter()
    {
        ++cursorCounter;
    }
//
//    public String getText()
//    {
//        return this.text;
//    }
//
//    public void setText(String p_146180_1_)
//    {
//        if (this.validator.apply(p_146180_1_))
//        {
//            if (p_146180_1_.length() > this.maxStringLength)
//            {
//                this.text = p_146180_1_.substring(0, this.maxStringLength);
//            }
//            else
//            {
//                this.text = p_146180_1_;
//            }
//
//            this.setCursorPositionEnd();
//        }
//    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText()
    {
        int i = Math.min(cursorPosition, selectionEnd);
        int j = Math.max(cursorPosition, selectionEnd);
        return text.substring(i, j);
    }

//    public void setValidator(Predicate<String> validator)
//    {
//        validator = validator;
//    }
//
//    public int getId()
//    {
//        return id;
//    }

    public int getNthWordFromCursor(int n)
    {
        return getNthWordFromPos(n, cursorPosition);
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int n, int position)
    {
        return func_146197_a(n, position, true);
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int numChars)
    {
        setCursorPosition(selectionEnd + numChars);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int position)
    {
        cursorPosition = position;
        int i = text.length();
        cursorPosition = MathHelper.clamp_int(cursorPosition, 0, i);
        setSelectionPos(cursorPosition);
    }

//    public void setCursorPositionZero()
//    {
//        setCursorPosition(0);
//    }

//    /**
//     * sets the cursors position to after the text
//     */
//    public void setCursorPositionEnd()
//    {
//        setCursorPosition(text.length());
//    }


    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int p_146199_1_)
    {
        int i = text.length();

        if (p_146199_1_ > i)
        {
            p_146199_1_ = i;
        }

        if (p_146199_1_ < 0)
        {
            p_146199_1_ = 0;
        }

        selectionEnd = p_146199_1_;

        if (fontRendererInstance != null)
        {
            if (lineScrollOffset > i)
            {
                lineScrollOffset = i;
            }

            int j = getWidth();
            String s = fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), j);
            int k = s.length() + lineScrollOffset;

            if (p_146199_1_ == lineScrollOffset)
            {
                lineScrollOffset -= fontRendererInstance.trimStringToWidth(text, j, true).length();
            }

            if (p_146199_1_ > k)
            {
                lineScrollOffset += p_146199_1_ - k;
            }
            else if (p_146199_1_ <= lineScrollOffset)
            {
                lineScrollOffset -= lineScrollOffset - p_146199_1_;
            }

            lineScrollOffset = MathHelper.clamp_int(lineScrollOffset, 0, i);
        }
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String textIn)
    {
        String s = "";
        String textInFiltered = ChatAllowedCharacters.filterAllowedCharacters(textIn);
        int i = Math.min(cursorPosition, selectionEnd);
        int j = Math.max(cursorPosition, selectionEnd);
        int k = maxStringLength - text.length() - (i - j);
        int l = 0;

        if (!text.isEmpty())
        {
            s = s + text.substring(0, i);
        }

        if (k < textInFiltered.length())
        {
            s = s + textInFiltered.substring(0, k);
            l = k;
        }
        else
        {
            s = s + textInFiltered;
            l = textInFiltered.length();
        }

        if (!text.isEmpty() && j < text.length())
        {
            s = s + text.substring(j);
        }

        if (validator.apply(s))
        {
            text = s;
            moveCursorBy(i - selectionEnd + l);

//            if (guiResponder != null)
//            {
//                guiResponder.func_175319_a(id, text);
//            }
        }
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
     * the cursor.
     */
    public void deleteWords(int numWords)
    {
        if (!text.isEmpty())
        {
            if (selectionEnd != cursorPosition)
            {
                writeText("");
            }
            else
            {
                deleteFromCursor(getNthWordFromCursor(numWords) - cursorPosition);
            }
        }
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int p_146175_1_)
    {
        if (!text.isEmpty())
        {
            if (selectionEnd != cursorPosition)
            {
                writeText("");
            }
            else
            {
                boolean flag = p_146175_1_ < 0;
                int i = flag ? cursorPosition + p_146175_1_ : cursorPosition;
                int j = flag ? cursorPosition : cursorPosition + p_146175_1_;
                String s = "";

                if (i >= 0)
                {
                    s = text.substring(0, i);
                }

                if (j < text.length())
                {
                    s = s + text.substring(j);
                }

                if (validator.apply(s))
                {
                    text = s;

                    if (flag)
                    {
                        moveCursorBy(p_146175_1_);
                    }

//                    if (guiResponder != null)
//                    {
//                        guiResponder.func_175319_a(id, text);
//                    }
                }
            }
        }
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_)
    {
        int i = p_146197_2_;
        boolean flag = p_146197_1_ < 0;
        int j = Math.abs(p_146197_1_);

        for (int k = 0; k < j; ++k)
        {
            if (!flag)
            {
                int l = text.length();
                i = text.indexOf(32, i);

                if (i == -1)
                {
                    i = l;
                }
                else
                {
                    while (p_146197_3_ && i < l && text.charAt(i) == 32)
                    {
                        ++i;
                    }
                }
            }
            else
            {
                while (p_146197_3_ && i > 0 && text.charAt(i - 1) == 32)
                {
                    --i;
                }

                while (i > 0 && text.charAt(i - 1) != 32)
                {
                    --i;
                }
            }
        }

        return i;
    }

    public boolean textboxKeyTyped(char typedChar, int keyCode)
    {
        if (!isFocused)
        {
            return false;
        }
        else if (GuiScreen.isKeyComboCtrlA(keyCode))
        {
            setCursorPosition(text.length());
            setSelectionPos(0);
            return true;
        }
        else if (GuiScreen.isKeyComboCtrlC(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());
            return true;
        }
        else if (GuiScreen.isKeyComboCtrlV(keyCode))
        {
            if (isEnabled)
            {
                writeText(GuiScreen.getClipboardString());
            }
            return true;
        }
        else if (GuiScreen.isKeyComboCtrlX(keyCode))
        {
            GuiScreen.setClipboardString(getSelectedText());

            if (isEnabled)
            {
                writeText("");
            }
            return true;
        }
        else
        {
            switch (keyCode)
            {
                case Keyboard.KEY_BACK:
//                    if (GuiScreen.isCtrlKeyDown())
//                    {
//                        if (isEnabled)
//                        {
//                            deleteWords(-1);
//                        }
//                    }
                    if (isEnabled)
                    {
                        deleteFromCursor(-1);
                    }
                    return true;

//                case Keyboard.KEY_HOME:
//                    if (GuiScreen.isShiftKeyDown())
//                    {
//                        setSelectionPos(0);
//                    }
//                    else
//                    {
//                        setCursorPosition(0);
//                    }
//                    return true;

                case Keyboard.KEY_LEFT:
                    if (GuiScreen.isShiftKeyDown())
                    {
//                        if (GuiScreen.isCtrlKeyDown())
//                        {
//                            setSelectionPos(getNthWordFromPos(-1, selectionEnd));
//                        }
//                        else
//                        {
                            setSelectionPos(selectionEnd - 1);
//                        }
                    }
//                    else if (GuiScreen.isCtrlKeyDown())
//                    {
//                        setCursorPosition(getNthWordFromCursor(-1));
//                    }
                    else
                    {
                        moveCursorBy(-1);
                    }
                    return true;

                case Keyboard.KEY_RIGHT:
                    if (GuiScreen.isShiftKeyDown())
                    {
//                        if (GuiScreen.isCtrlKeyDown())
//                        {
//                            setSelectionPos(getNthWordFromPos(1, selectionEnd));
//                        }
//                        else
//                        {
                            setSelectionPos(selectionEnd + 1);
//                        }
                    }
//                    else if (GuiScreen.isCtrlKeyDown())
//                    {
//                        setCursorPosition(getNthWordFromCursor(1));
//                    }
                    else
                    {
                        moveCursorBy(1);
                    }
                    return true;

//                case Keyboard.KEY_END:
//                    if (GuiScreen.isShiftKeyDown())
//                    {
//                        setSelectionPos(text.length());
//                    }
//                    else
//                    {
//                        setCursorPosition(text.length());
//                    }
//                    return true;

//                case Keyboard.KEY_DELETE:
//                    if (GuiScreen.isCtrlKeyDown())
//                    {
//                        if (isEnabled)
//                        {
//                            deleteWords(1);
//                        }
//                    }
//                    else if (isEnabled)
//                    {
//                        deleteFromCursor(1);
//                    }
//                    return true;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) // not one of first reserved 0-31, + not DEL or section sign
                    {
                        if (isEnabled)
                        {
                            writeText(Character.toString(typedChar));
                        }
                        return true;
                    }
                    else
                    {
                        return false;
                    }
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int buttonClicked)
    {
//        System.out.println(lineScrollOffset);

        boolean isHovered = (mouseX >= xPosition) && (mouseX < xPosition + width)
                && (mouseY >= yPosition) && (mouseY < yPosition + height);

        if (canLoseFocus)
        {
            if (isHovered && !isFocused)
            {
                cursorCounter = 0;
            }
            isFocused = isHovered;
        }

        // set cursor pos where left mouse was clicked
        if (isFocused && isHovered && buttonClicked == 0) // TODO: isHovered implied?
        {
            int i = mouseX - xPosition;

            if (enableBackgroundDrawing)
            {
                i -= 4;
            }

            String s = fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            setCursorPosition(fontRendererInstance.trimStringToWidth(s, i).length() + lineScrollOffset);
        }
    }

    public void drawTextBox()
    {
//        enableBackgroundDrawing = false;
        if (visible)
        {
            if (enableBackgroundDrawing)
            {
//                Gui.drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
                Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216);
            }

            int color = isEnabled ? enabledColor : disabledColor;
            int j = cursorPosition - lineScrollOffset;
            int k = selectionEnd - lineScrollOffset;
            String s = fontRendererInstance.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean shouldDrawCursor = isFocused && cursorCounter / 6 % 2 == 0 && flag;
            int l = enableBackgroundDrawing ? xPosition + 4 : xPosition;
            int i1 = enableBackgroundDrawing ? yPosition + (height - 8) / 2 : yPosition;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (!s.isEmpty())
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = fontRendererInstance.drawStringWithShadow(s1, (float) l, (float) i1, color);
            }

            boolean shouldCursorBeVerticalMaybe = cursorPosition < text.length() || text.length() >= maxStringLength;
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + width : l;
            }
            else if (shouldCursorBeVerticalMaybe)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length())
            {
                j1 = fontRendererInstance.drawStringWithShadow(s.substring(j), (float) j1, (float) i1, color);
            }

            if (shouldDrawCursor)
            {
                if (shouldCursorBeVerticalMaybe)
                {
                    // draw vertical cursor
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRendererInstance.FONT_HEIGHT, -3092272);
                }
                else
                {
                    // draw horizontal cursor
                    fontRendererInstance.drawStringWithShadow("_", (float) k1, (float) i1, color);
                }
            }

            if (k != j)
            {
                int l1 = l + fontRendererInstance.getStringWidth(s.substring(0, k));
                drawSelectionHighlight(k1, i1 - 1, l1 - 1, i1 + 1 + fontRendererInstance.FONT_HEIGHT);
            }
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawSelectionHighlight(int x2, int y2, int x1, int y1)
    {
        // swap x's
        if (x2 < x1)
        {
            int i = x2;
            x2 = x1;
            x1 = i;
        }

        // swap y's
        if (y2 < y1)
        {
            int j = y2;
            y2 = y1;
            y1 = j;
        }

        // clamp x
        if (x1 > xPosition + width)
        {
            x1 = xPosition + width;
        }

        // clamp x
        if (x2 > xPosition + width)
        {
            x2 = xPosition + width;
        }

//        GlStateManager.enableBlend();
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
////        GlStateManager.disableTexture2D();
//        GuiUtils.drawRectangle(GL11.GL_POLYGON, xB + 1, yB, 10, 11, new Color("#ff00ff").setA(0.5F));
//        GlStateManager.disableBlend();
////        GlStateManager.enableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387); // GL_OR_REVERSE
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x2, y1, 0.0D).endVertex();
        worldrenderer.pos(x1, y1, 0.0D).endVertex();
        worldrenderer.pos(x1, y2, 0.0D).endVertex();
        worldrenderer.pos(x2, y2, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
}

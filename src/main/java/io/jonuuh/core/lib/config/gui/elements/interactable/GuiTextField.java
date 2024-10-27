package io.jonuuh.core.lib.config.gui.elements.interactable;

import io.jonuuh.core.lib.util.RenderUtils;
import io.jonuuh.core.lib.config.gui.ISettingsGui;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class GuiTextField extends GuiInteractableElement
{
    public FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public String text = "";
    public int maxTextLength = 32;
    public int cursorPos;
    public int cursorFlashCounter;
    public boolean isTyping;
    public int selectionPos;

    public GuiTextField(ISettingsGui parent, int xPos, int yPos, int width, int height, String text, String tooltipStr)
    {
        super(parent, xPos, yPos, width, height, tooltipStr);
        this.text = text;
    }

    public GuiTextField(ISettingsGui parent, int xPos, int yPos, int width, int height, String text)
    {
        super(parent, xPos, yPos, width, height);
        this.text = text;
    }

    public GuiTextField(ISettingsGui parent, int xPos, int yPos, String text)
    {
        super(parent, xPos, yPos);
        this.text = text;
    }

//    public void setCursorPosition(int position)
//    {
//        cursorPos = position;
////        cursorPos = MathHelper.clamp_int(position, 0, text.length());
////        setSelectionPos(cursorPos);
//    }

    public void setSelectionPos(int selectionPos)
    {
        this.selectionPos = (int) clamp(selectionPos, 0, text.length());
    }

    public void setCursorPos(int cursorPos)
    {
        this.cursorPos = (int) clamp(cursorPos, 0, text.length());
    }

    public void moveCursorAndSelection(int amount)
    {
        setCursorPos(cursorPos + amount);
        setSelectionPos(selectionPos + amount);
    }

    private int getCursorScreenPos()
    {
        return xPos + fontRenderer.getStringWidth(getTextBeforeCursor()) - 1;
    }

    private String getTextBelowMouseX(int mouseX)
    {
        return fontRenderer.trimStringToWidth(text, mouseX - xPos + 1);
    }

    public int getSelectionStart()
    {
        return Math.min(cursorPos, selectionPos);
    }

    private int getSelectionEnd()
    {
        return Math.max(cursorPos, selectionPos);
    }

    public String getSelectedText()
    {
        return text.substring(getSelectionStart(), getSelectionEnd());
    }

    public String getTextBeforeCursor()
    {
        return text.substring(0, cursorPos);
    }

    public String getTextAfterCursor()
    {
        return text.substring(cursorPos);
    }

    public String insertText(String insertion, int index)
    {
        return replaceText(insertion, index, index);
//        return text.substring(0, index) + insertion + text.substring(index);
    }

    public String replaceText(String replacement, int startIndex, int endIndex)
    {
        System.out.println(" replacement: `" + replacement + "`" + " start: " + startIndex + " end: " + endIndex);
        System.out.println(text.substring(0, startIndex) + " `" + replacement + "` " + text.substring(endIndex));

        return text.substring(0, startIndex) + replacement + text.substring(endIndex);
    }

//    public String deleteCharsFromStart(String str, int charAmount)
//    {
//        return str.substring(charAmount);
//    }
//
//    public String deleteCharsFromEnd(String str, int charAmount)
//    {
//        return str.substring(0, str.length() - charAmount);
//    }

    public void clearSelection()
    {
        selectionPos = cursorPos;
    }

    public boolean hasSelection()
    {
        return selectionPos != cursorPos;
    }

    public boolean isSelectionForward()
    {
        return selectionPos >/*=*/ cursorPos;
    }

    protected double clamp(double value, double min, double max)
    {
        return Math.min((Math.max(value, min)), max);
    }

    @Override
    public void onScreenTick()
    {
        cursorFlashCounter++; // TODO: originally pre-increment for some reason?
    }

    // 12345678901234567890

    @Override
    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        super.onScreenDraw(mc, mouseX, mouseY);

        if (mouseDown)
        {
            setSelectionPos(getTextBelowMouseX(mouseX).length());
        }

        RenderUtils.drawRectangle(GL11.GL_POLYGON, xPos, yPos, width, height, new Color("#242424").setA(0.75F));

        if (hasSelection())
        {
            drawSelectionHighlight();
        }

        if (Display.isActive() && focused && (cursorFlashCounter % 20 < 10 || isTyping))
        {
            RenderUtils.drawRectangle(GL11.GL_POLYGON, getCursorScreenPos(), yPos - 2, 1, fontRenderer.FONT_HEIGHT + 2, new Color("#00ff00")/*.setA(0.5F)*/);
        }

        fontRenderer.drawString(text, xPos, yPos, -1);
    }

    public void drawSelectionHighlight()
    {
        int cursorX = getCursorScreenPos();
        int rectWidth = fontRenderer.getStringWidth(getSelectedText()) - 1;
        int rectX = isSelectionForward() ? cursorX + 1 : cursorX - rectWidth;
//        System.out.println("cursorPos: " + cursorPos + ", selectionPos: " + selectionPos);

        RenderUtils.drawRectangle(GL11.GL_POLYGON, rectX, yPos, rectWidth, fontRenderer.FONT_HEIGHT - 1, new Color("#009ac2").setA(0.75F)); // 3399FF
    }

    public void onMousePress(int mouseX, int mouseY)
    {
//        super.onMousePress(mouseX, mouseY);
        mouseDown = true;

        if (!focused)
        {
            focused = true;
            cursorFlashCounter = 0;
        }

        if (text.isEmpty())
        {
            return;
        }

        setCursorPos(getTextBelowMouseX(mouseX).length());
        clearSelection(); // TODO: redundant, set to cursorPos anyway in onScreenDraw

//        System.out.println(deleteCharsFromIndex("HelloWorld12345", 0, 2));
//        System.out.println(deleteCharsFromIndex("HelloWorld12345", 0, -2));
//        System.out.println(deleteCharsFromIndex("HelloWorld12345", 14, 2));
//        System.out.println(deleteCharsFromIndex("HelloWorld12345", 14, -16));

//        if (enableBackgroundDrawing)
//        {
//            i -= 4;
//        }

//        String s = fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
//
//        setCursorPosition(fontRenderer.trimStringToWidth(s, i).length() + lineScrollOffset);
//        }
    }

    protected void replaceSelection(String replacement)
    {
        int selectionLength = getSelectedText().length();
        text = replaceText(replacement, getSelectionStart(), getSelectionEnd());

        if (!isSelectionForward())
        {
            setCursorPos(cursorPos - selectionLength); // TODO: move to replaceText?
        }
    }

    public void deleteText()
    {
        if (hasSelection())
        {
            replaceSelection("");
        }
        else if (cursorPos > 0 /*!text.isEmpty()*/) // TODO: separate func?
        {
            text = replaceText("", cursorPos - 1, cursorPos);
            setCursorPos(cursorPos - 1); // TODO: move to replaceText?
        }

        clearSelection();
    }

    // filter and trim new text being added to the text
    protected String cleanTextIn(String textIn)
    {
        String textInFiltered = ChatAllowedCharacters.filterAllowedCharacters(textIn);
//        int postLength;
//
//        if (hasSelection())
//        {
//            postLength = (text.length() - getSelectedText().length()) + textInFiltered.length();
//        }
//        else
//        {
//            postLength = text.length() + textInFiltered.length();
//        }
//
//        if (postLength > maxTextLength)
//        {
//            System.out.println("`" + textInFiltered + "`" + "postLength: " + postLength + " maxTextLength: " + maxTextLength);
//            textInFiltered = textInFiltered.substring(0, maxTextLength - text.length());
//        }
//        String textInFilteredTrimmed = textInFiltered;

        int availableTextLength = maxTextLength - text.length();

        if (hasSelection())
        {
            availableTextLength += getSelectedText().length();
        }

        if (textInFiltered.length() > availableTextLength)
        {
            return textInFiltered.substring(0, availableTextLength);
            // System.out.println("`" + textInFiltered + "` `" + textInFilteredTrimmed + "`");
        }

        return textInFiltered;
    }

    public void writeText(String textIn)
    {
        String textInCleaned = cleanTextIn(textIn);

        if (textInCleaned.isEmpty())
        {
            return;
        }

        if (hasSelection())
        {
            replaceSelection(textInCleaned);
        }
        else /*if (!text.isEmpty())*/
        {
            text = insertText(textInCleaned, cursorPos);
            setCursorPos(cursorPos + textInCleaned.length());
//            text = replaceText("", cursorPos, cursorPos);
//            setCursorPos(cursorPos - 1);
        }

        clearSelection();

//        String s = "";
//        int start = getSelectionStart()/*Math.min(cursorPos, selectionPos)*/;
//        int end = getSelectionEnd()/*Math.max(cursorPos, selectionPos)*/;
//        int k = maxTextLength - text.length() - (start - end);
//        int l;
//
//        if (!text.isEmpty())
//        {
//            s = s + text.substring(0, start);
//        }
//
//        if (k < textInFiltered.length())
//        {
//            s = s + textInFiltered.substring(0, k);
//            l = k;
//        }
//        else
//        {
//            s = s + textInFiltered;
//            l = textInFiltered.length();
//        }
//
//        if (!text.isEmpty() && end < text.length())
//        {
//            s = s + text.substring(end);
//        }
//
////        if (validator.apply(s))
////        {
//        text = s;
//        moveCursorBy(start - selectionPos + l);
////        }
    }

    public void onKeyTyped(char typedChar, int keyCode)
    {
        isTyping = true;
        cursorFlashCounter = 0;

        if (!focused)
        {
            return;
        }
//        else if (GuiScreen.isKeyComboCtrlA(keyCode))
//        {
//            setCursorPosition(text.length());
//            setSelectionPos(0);
//            return true;
//        }
//        else if (GuiScreen.isKeyComboCtrlC(keyCode))
//        {
//            GuiScreen.setClipboardString(getSelectedText());
//            return true;
//        }
        else if (GuiScreen.isKeyComboCtrlV(keyCode) && enabled)
        {
            writeText(GuiScreen.getClipboardString());
        }
//        else if (GuiScreen.isKeyComboCtrlX(keyCode))
//        {
//            GuiScreen.setClipboardString(getSelectedText());
//
//            if (enabled)
//            {
//                writeText("");
//            }
//            return true;
//        }
        else
        {
            switch (keyCode)
            {
                case Keyboard.KEY_BACK:
                    if (enabled)
                    {
//                        int deletionAmt = (selectionPos < cursorPos) ? -getSelectedText().length()
//                                : (selectionPos > cursorPos) ? getSelectedText().length() : 1;
                        deleteText();
//                        deleteText(hasSelection() ? getSelectedText().length() : 1, selectionPos <= cursorPos);

//                        if (selectionPos == cursorPos)
//                        {
//                            deleteText(1);
//                        }
//                        else if (selectionPos > cursorPos)
//                        {
//                            deleteText(getSelectedText().length());
//                        }
//                        else if (selectionPos < cursorPos)
//                        {
//                            deleteText(getSelectedText().length());
//                        }
                    }
                    break;
//                    return true;

                case Keyboard.KEY_LEFT:
//                    setCursorPos(cursorPos - 1);
                    moveCursorAndSelection(-1);
                    break;
//                    if (GuiScreen.isShiftKeyDown())
//                    {
//                        setSelectionPos(selectionEnd - 1);
//                    }
//                    else
//                    {
//                        moveCursorBy(-1);
//                    }
//                    return true;

                case Keyboard.KEY_RIGHT:
                    moveCursorAndSelection(1);
//                    setCursorPos(cursorPos + 1);
                    break;
//                    if (GuiScreen.isShiftKeyDown())
//                    {
//                        setSelectionPos(selectionEnd + 1);
//                    }
//                    else
//                    {
//                        moveCursorBy(1);
//                    }
//                    return true;

                default:
                    if (enabled && ChatAllowedCharacters.isAllowedCharacter(typedChar)) // not one of first reserved 0-31, + not DEL or section sign
                    {
                        writeText(Character.toString(typedChar));
                        break;
//                        return true;
                    }
//                    else
//                    {
//                        return false;
//                    }
            }
        }

        isTyping = false;
    }
}

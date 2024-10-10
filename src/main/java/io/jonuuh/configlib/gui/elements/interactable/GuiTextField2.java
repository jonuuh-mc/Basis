package io.jonuuh.configlib.gui.elements.interactable;

import io.jonuuh.configlib.gui.GuiUtils;
import io.jonuuh.configlib.gui.ISettingsGui;
import io.jonuuh.configlib.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTextField2 extends GuiInteractableElement
{
    public FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    public String text = "";
    public int maxTextLength = 32;
    public int cursorPos;
    public int cursorFlashCounter;
    public boolean isTyping;
    public int selectionPos;

    public GuiTextField2(ISettingsGui parent, int xPos, int yPos, int width, int height, String tooltipStr)
    {
        super(parent, xPos, yPos, width, height, tooltipStr);
    }

    public GuiTextField2(ISettingsGui parent, int xPos, int yPos, int width, int height)
    {
        super(parent, xPos, yPos, width, height);
    }

    public GuiTextField2(ISettingsGui parent, int xPos, int yPos)
    {
        super(parent, xPos, yPos);
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

    public String deleteCharsFromIndex(String str, int startIndex, int charAmount)
    {
        if (charAmount < 0)
        {
            startIndex += charAmount; // subtract (add negative)
            startIndex += 1; // make startIndex always inclusive
            charAmount *= -1;
        }

        System.out.println("str: " + str + " startIndex: " + startIndex + " charAmount: " + charAmount);

        if (startIndex < 0 || startIndex == str.length() - 1)
        {
            return str;
        }

        return str.substring(0, startIndex) + str.substring(startIndex + charAmount);
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

    protected double clamp(double value, double min, double max)
    {
        return Math.min((Math.max(value, min)), max);
    }

    @Override
    public void onScreenTick()
    {
        cursorFlashCounter++; // TODO: originally pre-increment for some reason?
    }

    @Override
    public boolean onScreenDraw(Minecraft mc, int mouseX, int mouseY)
    {
        boolean wasDrawn = super.onScreenDraw(mc, mouseX, mouseY);

        if (wasDrawn)
        {
            if (mouseDown)
            {
                selectionPos = getTextBelowMouseX(mouseX).length();
            }

            GuiUtils.drawRectangle(GL11.GL_POLYGON, xPos, yPos, width, height, new Color("#242424").setA(0.75F));

            if (hasSelection())
            {
                drawSelectionHighlight();
            }

            if (/*Minecraft.getMinecraft().inGameHasFocus &&*/ focused && (cursorFlashCounter % 20 < 10 || isTyping))
            {
                GuiUtils.drawRectangle(GL11.GL_POLYGON, getCursorScreenPos(), yPos - 2, 1, fontRenderer.FONT_HEIGHT + 2, new Color("#00ff00")/*.setA(0.5F)*/);
            }

            fontRenderer.drawString(text, xPos, yPos, -1);
        }
        return wasDrawn;
    }

    public void drawSelectionHighlight()
    {
        int cursorX = getCursorScreenPos();
        int rectWidth = fontRenderer.getStringWidth(getSelectedText()) - 1;
        int rectX = (selectionPos >= cursorPos) ? cursorX + 1 : cursorX - rectWidth;
//        System.out.println("cursorPos: " + cursorPos + ", selectionPos: " + selectionPos);

        GuiUtils.drawRectangle(GL11.GL_POLYGON, rectX, yPos, rectWidth, fontRenderer.FONT_HEIGHT - 1, new Color("#009ac2").setA(0.75F)); // 3399FF
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

        cursorPos = getTextBelowMouseX(mouseX).length();
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

    public void deleteText(int charAmount, boolean backward)
    {
        if (backward && getTextBeforeCursor().length() < charAmount)
        {
            return;
        }
        else if (!backward && getTextAfterCursor().length() < charAmount)
        {
            return;
        }
//        System.out.println(charAmount);

        String before;
        String after;

        if (backward)
        {
            String s = getTextBeforeCursor();
            before = deleteCharsFromIndex(s, s.length() - 1, -charAmount)  /*text.substring(0, cursorPos - charAmount)*/;
            after = getTextAfterCursor();
            setCursorPos(cursorPos - charAmount);
        }
        else
        {
            before = getTextBeforeCursor();
            after = text.substring(cursorPos + charAmount);
        }

        text = before + after;
        clearSelection();
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String textIn)
    {
        String textInFiltered = ChatAllowedCharacters.filterAllowedCharacters(textIn);

        String before;
        String after;

        if (hasSelection())
        {
            System.out.println("INDEX: " + text.indexOf(getSelectedText()));

            before = text.substring(0, selectionPos);
            after = getTextAfterCursor();
        }
        else
        {
            before = getTextBeforeCursor();
            after = getTextAfterCursor();
        }

        text = before + textInFiltered + after;

        if (text.length() > maxTextLength)
        {
            text = text.substring(0, maxTextLength);
        }

        setCursorPos(cursorPos + textInFiltered.length());
        selectionPos = cursorPos;


//        int i = Math.min(cursorPos, selectionEnd);
//        int j = Math.max(cursorPos, selectionEnd);
//        int k = maxStringLength - text.length() - (i - j);
//        int l = 0;
//
//        if (!text.isEmpty())
//        {
//            s = s + text.substring(0, i);
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
//        if (!text.isEmpty() && j < text.length())
//        {
//            s = s + text.substring(j);
//        }

//        if (validator.apply(s))
//        {
//        text = s;
//        moveCursorBy(i - selectionEnd + l);
//        }
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

                        deleteText(hasSelection() ? getSelectedText().length() : 1, selectionPos <= cursorPos);

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

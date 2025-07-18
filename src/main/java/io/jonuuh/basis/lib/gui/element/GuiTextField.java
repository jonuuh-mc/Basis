package io.jonuuh.basis.lib.gui.element;

import io.jonuuh.basis.lib.gui.event.input.KeyInputEvent;
import io.jonuuh.basis.lib.gui.event.lifecycle.ScreenTickEvent;
import io.jonuuh.basis.lib.gui.listener.input.KeyInputListener;
import io.jonuuh.basis.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.basis.lib.gui.listener.lifecycle.ScreenTickListener;
import io.jonuuh.basis.lib.util.Color;
import io.jonuuh.basis.lib.util.MathUtils;
import io.jonuuh.basis.lib.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class GuiTextField extends GuiElement implements KeyInputListener, ScreenTickListener, MouseClickListener
{
    protected FontRenderer fontRenderer = mc.fontRendererObj;
    protected String text;
    protected int maxTextLength;
    protected int cursorPos;
    protected int cursorFlashCounter;
    protected boolean isTyping;
    protected int selectionPos;
    protected boolean enabled;
    protected boolean mouseDown;

    public GuiTextField(Builder builder)
    {
        super(builder);
        this.text = builder.text;
        this.maxTextLength = builder.maxTextLength;
    }

    // TODO: some textfield logic: `this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;`

//    public void setCursorPosition(int position)
//    {
//        cursorPos = position;
////        cursorPos = MathHelper.clamp_int(position, 0, text.length());
////        setSelectionPos(cursorPos);
//    }

    public void setSelectionPos(int selectionPos)
    {
        this.selectionPos = (int) MathUtils.clamp(selectionPos, 0, text.length());
    }

    public void setCursorPos(int cursorPos)
    {
        this.cursorPos = (int) MathUtils.clamp(cursorPos, 0, text.length());
    }

    public void moveCursorAndSelection(int amount)
    {
        setCursorPos(cursorPos + amount);
        setSelectionPos(selectionPos + amount);
    }

    private float getCursorScreenPos()
    {
        return worldXPos() + fontRenderer.getStringWidth(getTextBeforeCursor()) - 1;
    }

    private String getTextBelowMouseX(int mouseX)
    {
        return fontRenderer.trimStringToWidth(text, (int) (mouseX - worldXPos() + 1));
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

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public boolean isMouseDown()
    {
        return mouseDown;
    }

    @Override
    public void setMouseDown(boolean mouseDown)
    {
        this.mouseDown = mouseDown;
    }

    @Override
    public void onScreenTick(ScreenTickEvent event)
    {
        cursorFlashCounter++; // TODO: originally pre-increment for some reason?
    }

    // 12345678901234567890

//    @Override
//    public void onScreenDraw(Minecraft mc, int mouseX, int mouseY)
//    {
//        super.onScreenDraw(mc, mouseX, mouseY);
//
//        if (mouseDown)
//        {
//            setSelectionPos(getTextBelowMouseX(mouseX).length());
//        }
//
//        RenderUtils.drawRectangle(GL11.GL_POLYGON, xPos, yPos, width, height, new Color("#242424").setA(0.75F));
//
//        if (hasSelection())
//        {
//            drawSelectionHighlight();
//        }
//
//        if (Display.isActive() && focused && (cursorFlashCounter % 20 < 10 || isTyping))
//        {
//            RenderUtils.drawRectangle(GL11.GL_POLYGON, getCursorScreenPos(), yPos - 2, 1, fontRenderer.FONT_HEIGHT + 2, new Color("#00ff00")/*.setA(0.5F)*/);
//        }
//
//        fontRenderer.drawString(text, xPos, yPos, -1);
//    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (isMouseDown())
        {
            setSelectionPos(getTextBelowMouseX(mouseX).length());
        }

        RenderUtils.drawRectangle(worldXPos(), worldYPos(), getWidth(), getHeight(), new Color("#BF242424"));

        if (hasSelection())
        {
            drawSelectionHighlight();
        }

        if (Display.isActive() && isFocused() && (cursorFlashCounter % 20 < 10 || isTyping))
        {
            RenderUtils.drawRectangle(getCursorScreenPos(), worldYPos() - 2, 1, fontRenderer.FONT_HEIGHT + 2, new Color("#00ff00")/*.setA(0.5F)*/);
        }

        fontRenderer.drawString(text, worldXPos(), worldYPos(), -1, false);
    }

    public void drawSelectionHighlight()
    {
        float cursorX = getCursorScreenPos();
        int rectWidth = fontRenderer.getStringWidth(getSelectedText()) - 1;
        float rectX = isSelectionForward() ? cursorX + 1 : cursorX - rectWidth;
//        System.out.println("cursorPos: " + cursorPos + ", selectionPos: " + selectionPos);

        RenderUtils.drawRectangle(rectX, worldYPos(), rectWidth, fontRenderer.FONT_HEIGHT - 1, new Color("#BF009ac2")); // 3399FF
    }

    public void onMouseDown(int mouseX, int mouseY)
    {
        if (!isFocused())
        {
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

    @Override
    public void onKeyTyped(KeyInputEvent event)
    {
        isTyping = true;
        cursorFlashCounter = 0;

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
        if (GuiScreen.isKeyComboCtrlV(event.keyCode) && isEnabled())
        {
            writeText(GuiScreen.getClipboardString());
        }
//        else if (GuiScreen.isKeyComboCtrlX(keyCode))
//        {
//            GuiScreen.setClipboardString(getSelectedText());
//
//            if (isEnabled())
//            {
//                writeText("");
//            }
//            return true;
//        }
        else
        {
            switch (event.keyCode)
            {
                case Keyboard.KEY_BACK:
                    if (isEnabled())
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
                    if (isEnabled() && ChatAllowedCharacters.isAllowedCharacter(event.typedChar)) // not one of first reserved 0-31, + not DEL or section sign
                    {
                        writeText(Character.toString(event.typedChar));
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

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiTextField>
    {
        protected String text;
        protected int maxTextLength;

        public Builder(String elementName)
        {
            super(elementName);
        }

        public Builder text(String text)
        {
            this.text = text;
            return self();
        }

        public Builder maxTextLength(int maxTextLength)
        {
            this.maxTextLength = maxTextLength;
            return self();
        }

        @Override
        protected Builder self()
        {
            return this;
        }

        @Override
        public GuiTextField build()
        {
            return new GuiTextField(this);
        }
    }
}

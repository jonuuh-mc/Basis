package io.jonuuh.basis.lib.gui.element;

import io.jonuuh.basis.lib.gui.event.input.KeyInputEvent;
import io.jonuuh.basis.lib.gui.event.input.MouseDownEvent;
import io.jonuuh.basis.lib.gui.event.lifecycle.ScreenTickEvent;
import io.jonuuh.basis.lib.gui.listener.input.KeyInputListener;
import io.jonuuh.basis.lib.gui.listener.input.MouseClickListener;
import io.jonuuh.basis.lib.gui.listener.lifecycle.ScreenTickListener;
import io.jonuuh.basis.lib.gui.properties.GuiColorType;
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
    /** Position of the cursor within the text */
    protected int cursorPos;
    /**
     * Position of the start/end of the selection within the text.
     * <p>
     * This will be equal to cursorPos when nothing is selected (the default state),
     * otherwise it may be less than or greater than cursorPos
     */
    protected int selectionPos;
    /** Used to flash cursor on and off based on screen tick timing */
    protected int cursorFlashCounter;
    /** Used to prevent cursor flashing while typing */
    protected boolean isTyping;
    protected boolean enabled;
    protected boolean mouseDown;

    public GuiTextField(Builder builder)
    {
        super(builder);
        this.text = builder.text;
        this.maxTextLength = builder.maxTextLength;
        this.enabled = builder.enabled;

        setHeight((mc.fontRendererObj.FONT_HEIGHT - 1) + getPadding().top() + getPadding().bottom());
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
        setCursorPos(text.length());
        setSelectionPos(text.length());
    }

    public void clearText()
    {
        setText("");
    }

    public void setCursorPos(int cursorPos)
    {
        this.cursorPos = (int) MathUtils.clamp(cursorPos, 0, text.length());
    }

    public void setSelectionPos(int selectionPos)
    {
        this.selectionPos = (int) MathUtils.clamp(selectionPos, 0, text.length());
    }

    public void moveCursorAndSelection(int amount)
    {
        setCursorPos(cursorPos + amount);
        setSelectionPos(selectionPos + amount);
    }

    public float getCursorScreenPos()
    {
        return ElementUtils.getInnerLeftBound(this) + fontRenderer.getStringWidth(getTextBeforeCursor()) - 1;
    }

    public String getTextBelowMouseX(int mouseX)
    {
        return fontRenderer.trimStringToWidth(text, (int) (mouseX - ElementUtils.getInnerLeftBound(this) + 1));
    }

    public String getTextBeforeCursor()
    {
        return text.substring(0, cursorPos);
    }

//    public String getTextAfterCursor()
//    {
//        return text.substring(cursorPos);
//    }

    public int getSelectionStart()
    {
        return Math.min(cursorPos, selectionPos);
    }

    public int getSelectionEnd()
    {
        return Math.max(cursorPos, selectionPos);
    }

    public String getSelectedText()
    {
        return text.substring(getSelectionStart(), getSelectionEnd());
    }

    public boolean hasSelection()
    {
        return selectionPos != cursorPos;
    }

    public void clearSelection()
    {
        selectionPos = cursorPos;
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
        cursorFlashCounter++;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        if (!isVisible())
        {
            return;
        }
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        RenderUtils.drawRoundedRectWithBorder(worldXPos(), worldYPos(), getWidth(), getHeight(),
                getCornerRadius(), 1, getColor(GuiColorType.BACKGROUND), getColor(GuiColorType.BORDER));

        if (isMouseDown())
        {
            setSelectionPos(getTextBelowMouseX(mouseX).length());
        }

        if (hasSelection())
        {
            if (!isFocused())
            {
                setSelectionPos(cursorPos);
            }
            else
            {
                drawSelectionHighlight();
            }
        }

        if (Display.isActive() && isFocused() && (cursorFlashCounter % 20 < 10 || isTyping))
        {
            RenderUtils.drawRectangle(getCursorScreenPos(), ElementUtils.getInnerTopBound(this) - 2, 1, fontRenderer.FONT_HEIGHT + 2, getColor(GuiColorType.BASE));
        }

//        RenderUtils.drawRoundedRectWithBorder(
//                ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this),
//                ElementUtils.getInnerWidth(this), ElementUtils.getInnerHeight(this),
//                getCornerRadius(), 1, getColor(GuiColorType.BASE), getColor(GuiColorType.BORDER));

        String trimmedText = RenderUtils.trimStringToWidthWithEllipsis(text, (int) ElementUtils.getInnerWidth(this));
        fontRenderer.drawString(trimmedText, ElementUtils.getInnerLeftBound(this), ElementUtils.getInnerTopBound(this), -1, false);
    }

    protected void drawSelectionHighlight()
    {
        float cursorX = getCursorScreenPos();
        int rectWidth = fontRenderer.getStringWidth(getSelectedText()) - 1;
        float rectX = isSelectionForward() ? cursorX + 1 : cursorX - rectWidth;

        RenderUtils.drawRectangle(rectX, ElementUtils.getInnerTopBound(this), rectWidth, fontRenderer.FONT_HEIGHT - 1, getColor(GuiColorType.BASE).addA(-0.2F));
    }

    @Override
    public void onMouseDown(MouseDownEvent event)
    {
        MouseClickListener.super.onMouseDown(event);

        if (!isFocused())
        {
            cursorFlashCounter = 0;
        }

        if (text.isEmpty())
        {
            return;
        }

        setCursorPos(getTextBelowMouseX(event.mouseX).length());
        clearSelection(); // TODO: redundant? set to cursorPos anyway in onScreenDraw
    }

    @Override
    public void onKeyTyped(KeyInputEvent event)
    {
        isTyping = true;
        cursorFlashCounter = 0;

        if (GuiScreen.isCtrlKeyDown())
        {
            // Select all
            if (GuiScreen.isKeyComboCtrlA(event.keyCode))
            {
                setCursorPos(text.length());
                setSelectionPos(0);
            }
            // Copy
            else if (GuiScreen.isKeyComboCtrlC(event.keyCode))
            {
                GuiScreen.setClipboardString(getSelectedText());
            }
            // Paste
            else if (GuiScreen.isKeyComboCtrlV(event.keyCode) && isEnabled())
            {
                writeString(GuiScreen.getClipboardString());
            }
            // Cut
            else if (GuiScreen.isKeyComboCtrlX(event.keyCode))
            {
                GuiScreen.setClipboardString(getSelectedText());

                if (isEnabled())
                {
                    deleteText();
                }
            }
        }
        else
        {
            switch (event.keyCode)
            {
                case Keyboard.KEY_LEFT:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        setCursorPos(cursorPos - 1);
                    }
                    else
                    {
                        moveCursorAndSelection(-1);
                    }
                    break;

                case Keyboard.KEY_RIGHT:
                    if (GuiScreen.isShiftKeyDown())
                    {
                        setCursorPos(cursorPos + 1);
                    }
                    else
                    {
                        moveCursorAndSelection(1);
                    }
                    break;

                case Keyboard.KEY_BACK:
                    if (isEnabled())
                    {
                        deleteText();
                    }
                    break;

                default:
                    // Enabled + not one of first ascii reserved 0-31, + not DEL or section sign
                    if (isEnabled() && ChatAllowedCharacters.isAllowedCharacter(event.typedChar))
                    {
                        writeString(Character.toString(event.typedChar));
                        break;
                    }
            }
        }

        isTyping = false;
    }

    public void writeString(String str)
    {
        String cleanedText = cleanText(str);

        if (cleanedText.isEmpty())
        {
            return;
        }

        if (hasSelection())
        {
            replaceSelection(cleanedText);
        }
        else /*if (!text.isEmpty())*/
        {
            this.text = insertText(cleanedText, cursorPos);
            setCursorPos(cursorPos + cleanedText.length());
        }

        clearSelection();
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

    public String insertText(String insertion, int index)
    {
        return replaceText(insertion, index, index);
    }

    public String replaceText(String replacement, int startIndex, int endIndex)
    {
//        System.out.println(" replacement: `" + replacement + "`" + " start: " + startIndex + " end: " + endIndex);
//        System.out.println(text.substring(0, startIndex) + " `" + replacement + "` " + text.substring(endIndex));
        return text.substring(0, startIndex) + replacement + text.substring(endIndex);
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

    /**
     * Filter and trim a new string being added to the text.
     *
     * @param str A new string
     * @return The cleaned new string
     */
    protected String cleanText(String str)
    {
        // Filter the string
        String filteredStr = ChatAllowedCharacters.filterAllowedCharacters(str);

        float availableWidth = ElementUtils.getInnerWidth(this) - fontRenderer.getStringWidth(text);

        // If there's a selection, it will be replaced by the new string after it's cleaned,
        // so it can be treated as empty (available space)
        if (hasSelection())
        {
            availableWidth += fontRenderer.getStringWidth(getSelectedText());
        }

        // Trim the string if necessary
        if (fontRenderer.getStringWidth(filteredStr) > availableWidth)
        {
            return fontRenderer.trimStringToWidth(filteredStr, (int) availableWidth);
        }

        return filteredStr;
    }

    public static class Builder extends GuiElement.AbstractBuilder<Builder, GuiTextField>
    {
        protected String text = "";
        protected int maxTextLength = 32;
        protected boolean enabled = true;

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

        public Builder enabled(boolean enabled)
        {
            this.enabled = enabled;
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

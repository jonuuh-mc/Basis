package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.properties.GuiColorType;
import io.jonuuh.core.lib.gui.properties.GuiEventType;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

public class GuiButton extends GuiElement
{
    protected String buttonLabel;

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, String buttonLabel, Consumer<GuiElement> mouseDownBehavior)
    {
        super(elementName, xPos, yPos, width, height);
        init(buttonLabel, mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, Consumer<GuiElement> mouseDownBehavior)
    {
        super(elementName, xPos, yPos, width, height);
        init("", mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, Consumer<GuiElement> mouseDownBehavior)
    {
        super(elementName, xPos, yPos);
        init("", mouseDownBehavior);
    }

    // TODO: completely insane design; want to call the separate super()s in constructors
    //  to avoid re-hardcoding default fields, but not set all the subclass fields in every constructor.
    //  this is a strange workaround
    private void init(String buttonLabel, Consumer<GuiElement> mouseDownBehavior)
    {
        this.buttonLabel = buttonLabel;
        assignPostEventBehavior(GuiEventType.MOUSE_DOWN, mouseDownBehavior);
    }

    public String getButtonLabel()
    {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel)
    {
        this.buttonLabel = buttonLabel;
    }

    @Override
    protected void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        RenderUtils.drawRoundedRect(worldXPos(), worldYPos(), getWidth(), getHeight(), getCornerRadius(), getColor(GuiColorType.BASE));

        String buttonText = RenderUtils.trimStringToWidthWithEllipsis(buttonLabel, (int) getWidth());

        mc.fontRendererObj.drawString(buttonText, worldXPos() + (getWidth() / 2) - ((float) mc.fontRendererObj.getStringWidth(buttonText) / 2),
                worldYPos() + (getHeight() / 2) - ((float) mc.fontRendererObj.FONT_HEIGHT / 2), getColor(GuiColorType.ACCENT1).toPackedARGB(), true);
    }

//    @Override
//    public void onMouseDown(int mouseX, int mouseY)
//    {
//        setLocalYPos(getLocalYPos() + 1);
//    }
//
//    @Override
//    public void onMouseUp(int mouseX, int mouseY)
//    {
//        setLocalYPos(getLocalYPos() - 1);
//    }

    @Override
    protected void onKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == Keyboard.KEY_RETURN /*|| keyCode == Keyboard.KEY_SPACE*/)
        {
            // TODO:
//            tryApplyCustomEventBehavior(GuiEventType.MOUSE_DOWN);
//            yPos += 1;
//            playClickSound(mc.getSoundHandler());
        }
    }
}

package io.jonuuh.core.lib.gui.element;

import io.jonuuh.core.lib.gui.GuiColorType;
import io.jonuuh.core.lib.gui.event.GuiEventBehavior;
import io.jonuuh.core.lib.gui.event.GuiEventType;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiButton extends GuiElement
{
    protected String buttonLabel;

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, String buttonLabel, GuiEventBehavior mouseDownBehavior)
    {
        super(elementName, xPos, yPos, width, height);
        init(buttonLabel, mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, float width, float height, GuiEventBehavior mouseDownBehavior)
    {
        super(elementName, xPos, yPos, width, height);
        init("", mouseDownBehavior);
    }

    public GuiButton(String elementName, float xPos, float yPos, GuiEventBehavior mouseDownBehavior)
    {
        super(elementName, xPos, yPos);
        init("", mouseDownBehavior);
    }

    // TODO: completely insane design; want to call the separate super()s in constructors
    //  to avoid re-hardcoding default fields, but not set all the subclass fields in every constructor.
    //  this is a strange workaround
    private void init(String buttonLabel, GuiEventBehavior mouseDownBehavior)
    {
        this.buttonLabel = buttonLabel;
        assignCustomEventBehavior(GuiEventType.MOUSE_DOWN, mouseDownBehavior);
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
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, worldXPos(), worldYPos(), getWidth(), getHeight(), 3, getColor(GuiColorType.BASE), true);

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

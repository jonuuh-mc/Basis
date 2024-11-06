package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.config.gui.elements.interactable.sliders.GuiIntSlider;
import io.jonuuh.core.lib.util.Color;
import io.jonuuh.core.lib.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class GuiScrollContainer extends GuiContainer
{
    protected final GuiIntSlider slider;

    public GuiScrollContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, Map<String, Color> colorMap, float outerRadius, float innerRadius, String tooltipStr, int sliderWindowHeight)
    {
        super(parent, elementName, xPos, yPos, width, height, colorMap, outerRadius, innerRadius, tooltipStr);

        this.slider = new GuiIntSlider(this, elementName + "$slider", width - 16, 0, 16, height, 0, sliderWindowHeight, 0, true);

//        if (!hasParent())
//        {
//            this.slider = new GuiIntSlider(this, elementName + "$slider", xPos + width - 16, yPos, 16, height, 0, sliderWindowHeight, 0, true);
//        }
//        else
//        {
//            this.slider = new GuiIntSlider(this, elementName + "$slider",
//                    xPos + parent.xPos + width /*+ 5*/ - 16,
//                    yPos + parent.yPos,
//                    16, height, 0, sliderWindowHeight, 0, true);
//        }

//        slider.xPos += this.xPos;
//        slider.yPos += this.yPos;
//        slider.xPosInit += this.xPosInit;
//        slider.yPosInit += this.yPosInit;
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);
//        slider.onScreenDraw(mouseX, mouseY, partialTicks);

//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        RenderUtils.scissorFromTopLeft(xPos, yPos, width, height);

//        this.onScreenDraw(mouseX, mouseY, partialTicks);
//        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
//
////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
////        GL11.glPopMatrix();
//
//        slider.onScreenDraw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos - 20, yPos - 20, width + 40, height + 40, 6, baseColor.copy().setA(0.8F), true);
        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, 6, new Color("#242424").setA(0.8F), true);

//        GuiIntSlider slider = (GuiIntSlider) interactableChildrenMap.get(this.elementName + "$slider");
        int value = slider.getValue(0);

        for (GuiElement element : getChildren())
        {
            if (element == slider)
            {
                continue;
            }

            element.setTempYPos(-value);

            element.visible = (element.yPos + element.height > this.yPos) && (element.yPos < this.yPos + this.height);
            if (element instanceof GuiInteractableElement)
            {
                ((GuiInteractableElement) element).setEnabled(element.visible);
            }
        }
    }

//    @Override
//    public void handleScreenTick()
//    {
//        this.onScreenTick();
//        childrenMap.values().forEach(GuiElement::onScreenTick);
//    }
//
//    @Override
//    public void handleKeyTyped(char typedChar, int keyCode)
//    {
//        for (GuiInteractableElement element : interactableChildrenMap.values())
//        {
//            element.onKeyTyped(typedChar, keyCode);
//        }
//    }

    @Override
    public List<GuiInteractableElement> handleMouseDown(int mouseX, int mouseY)
    {
//        if (slider.wasMousePressed(mouseX, mouseY))
//        {
//            System.out.println("slider pressed: " + slider.elementName);
//            slider.playPressSound(this.mc.getSoundHandler());
//            slider.onMousePress(mouseX, mouseY);
//            lastMouseDownElement = slider;
//            return null;
//        }

        return super.handleMouseDown(mouseX, mouseY);
//        return null;
    }

//    public void handleMouseRelease(int mouseX, int mouseY)
//    {
//        if (lastMouseDownElement != null)
//        {
//            lastMouseDownElement.onMouseRelease(mouseX, mouseY);
//            lastMouseDownElement = null;
//        }
//    }
}

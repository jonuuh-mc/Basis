//package io.jonuuh.core.lib.gui.elements;
//
//import io.jonuuh.core.lib.gui.elements.interactable.GuiInteractableElement;
//import io.jonuuh.core.lib.gui.elements.sliders.GuiScrollSlider;
//import io.jonuuh.core.lib.util.Color;
//import io.jonuuh.core.lib.util.RenderUtils;
//import org.lwjgl.opengl.GL11;
//
//import java.util.Map;
//
//public class GuiScrollContainer extends GuiContainer
//{
//    protected final GuiScrollSlider slider;
//    protected int lastSliderValue;
//
//    public GuiScrollContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, Map<String, Color> colorMap, float outerRadius, float innerRadius, String tooltipStr, int totalScrollingHeight)
//    {
//        super(parent, elementName, xPos, yPos, width, height, colorMap, outerRadius, innerRadius, tooltipStr);
//
//        this.slider = new GuiScrollSlider(this, elementName + "$slider", width - 8, 0, 8, height, 0, totalScrollingHeight, 0);
//        this.lastSliderValue = slider.getValueInt();
////        if (!hasParent())
////        {
////            this.slider = new GuiIntSlider(this, elementName + "$slider", xPos + width - 16, yPos, 16, height, 0, sliderWindowHeight, 0, true);
////        }
////        else
////        {
////            this.slider = new GuiIntSlider(this, elementName + "$slider",
////                    xPos + parent.xPos + width /*+ 5*/ - 16,
////                    yPos + parent.yPos,
////                    16, height, 0, sliderWindowHeight, 0, true);
////        }
//
////        slider.xPos += this.xPos;
////        slider.yPos += this.yPos;
////        slider.xPosInit += this.xPosInit;
////        slider.yPosInit += this.yPosInit;
//    }
//
////    @Override
////    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
////    {
//////        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
//////        for (GuiElement element : childrenMap.values())
//////        {
//////            element.onScreenDraw(mouseX, mouseY, partialTicks);
//////        }
////
////        super.onScreenDraw(mouseX, mouseY, partialTicks);
////        // nested containers make it this far
////
////        if (this.hasParent())
////        {
////            return;
////        }
////        // root container makes it past here
////
////        // // // // //
////        GL11.glPushMatrix();
////        GL11.glEnable(GL11.GL_SCISSOR_TEST);
////        RenderUtils.scissorFromTopLeft(this.xPos, this.yPos, this.width, this.height);
////
////        for (GuiElement element : this.getChildren())
////        {
////            if (element instanceof GuiContainer)
////            {
////                continue;
////            }
////            element.onScreenDraw(mouseX, mouseY, partialTicks);
////        }
////
//////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////        GL11.glPopMatrix();
////        // // // // //
////
////        GuiContainer container2 = (GuiContainer) this.childrenMap.get("CONTAINER2");
////        RenderUtils.scissorFromTopLeft(container2.xPos, Math.max(container2.yPos, this.yPos), container2.width, container2.height);
////
////        container2.onScreenDraw(mouseX, mouseY, partialTicks);
////        for (GuiElement element : container2.getChildren())
////        {
////            if (element instanceof GuiContainer)
////            {
////                continue;
////            }
////            element.onScreenDraw(mouseX, mouseY, partialTicks);
////        }
////
////
////        GuiContainer container3 = (GuiContainer) container2.childrenMap.get("CONTAINER3");
////        RenderUtils.scissorFromTopLeft(container3.xPos, Math.max(Math.max(container3.yPos, container2.yPos), this.yPos), container3.width, container3.height);
////
////        container3.onScreenDraw(mouseX, mouseY, partialTicks);
////        for (GuiElement element : container3.getChildren())
////        {
////            if (element instanceof GuiContainer)
////            {
////                continue;
////            }
////            element.onScreenDraw(mouseX, mouseY, partialTicks);
////        }
////
////        GL11.glPopMatrix();
////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
////
////        // TODO: last working here 11/19/24
////
//////        GuiContainer deepestNestedContainer = this;
////
//////        System.out.println(getNestedContainers());
////
//////        GuiContainer container = getNestedContainers().get(0);
////
//////        for (GuiContainer container : getNestedContainers())
//////        {
//////        container.onScreenDraw(mouseX, mouseY, partialTicks);
//////
//////        GL11.glPushMatrix();
//////        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//////        RenderUtils.scissorFromTopLeft(container.xPos, container.yPos, container.width, container.height);
//////
//////        for (GuiElement guiElement : container.getNestedChildren())
//////        {
//////            if (guiElement instanceof GuiContainer)
//////            {
////////                GL11.glDisable(GL11.GL_SCISSOR_TEST);
////////                GL11.glPopMatrix();
//////
//////                GL11.glPushMatrix();
//////                GL11.glEnable(GL11.GL_SCISSOR_TEST);
//////                RenderUtils.scissorFromTopLeft(guiElement.xPos, guiElement.yPos, guiElement.width, guiElement.height);
//////            }
//////            guiElement.onScreenDraw(mouseX, mouseY, partialTicks);
//////        }
//////
//////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////        GL11.glPopMatrix();
//////
//////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////        GL11.glPopMatrix();
////
//////            if (element instanceof GuiContainer)
//////            {
//////                if (element.getNumParents() > deepestNestedContainer.getNumParents())
//////                {
//////                    deepestNestedContainer = (GuiContainer) element;
//////                }
////////                if (element.getNumParents() == 1)
////////                {
//////                    GL11.glPushMatrix();
//////                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
//////                    RenderUtils.scissorFromTopLeft(element.xPos, element.yPos, element.width, element.height);
////////                }
////////                else if (element.getNumParents() > 1)
////////                {
////////                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
////////                    GL11.glPopMatrix();
////////
////////                    GL11.glPushMatrix();
////////                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
////////                    RenderUtils.scissorFromTopLeft(element.xPos, element.yPos, element.width, element.height);
////////                }
//////
//////                element.onScreenDraw(mouseX, mouseY, partialTicks);
//////            }
//////            else
//////            {
//////                element.onScreenDraw(mouseX, mouseY, partialTicks);
//////            }
//////        }
////
//////        System.out.println(deepestNestedContainer.getNumParents());
//////        for (int i = 0; i < deepestNestedContainer.getNumParents(); i++)
//////        {
//////            GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////            GL11.glPopMatrix();
//////        }
////
////
//////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////        GL11.glPopMatrix();
////
//////        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//////        GL11.glPopMatrix();
////
////
//////        super.onScreenDraw(mouseX, mouseY, partialTicks);
////
//////        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
////
//////        for (GuiElement element : getNestedChildren())
//////        {
//////            if (!(element instanceof GuiContainer))
//////            {
//////                element.onScreenDraw(mouseX, mouseY, partialTicks);
//////            }
//////            else
//////            {
//////                super.onScreenDraw(mouseX, mouseY, partialTicks);
//////            }
//////        }
////
////
//////        getNestedChildren().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
//////        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
////    }
//
//    // TODO: make recursive
//    public void handleMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
//    {
//        super.handleMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
//
////        int value = slider.getValueInt();
////
//////        if (slider.isMouseDown() && lastSliderValue != value)
//////        {
//////            System.out.println(lastSliderValue - value);
////
////        for (GuiElement element : getNestedChildren())
////        {
////            if (element == slider)
////            {
////                continue;
////            }
////
////            element.yPos += (lastSliderValue - value);
////
////            // TODO: last working here
////            element.visible = ((element.yPos + element.height > element.parent.yPos)
////                    && (element.yPos < element.parent.yPos + element.parent.height));
////
//////                element.visible = ((element.yPos + element.height > this.yPos) && (element.yPos < this.yPos + this.height))
//////                        && ((element.yPos + element.height > element.parent.yPos) && (element.yPos < element.parent.yPos + element.parent.height));
////
//////                element.visible = (element.yPos + element.height > this.yPos) && (element.yPos < this.yPos + this.height);
////
////            if (element instanceof GuiInteractableElement)
////            {
////                ((GuiInteractableElement) element).setEnabled(element.visible);
////            }
////        }
////
////        lastSliderValue = value;
//    }
//
//    @Override
//    protected void drawElement(int mouseX, int mouseY, float partialTicks)
//    {
////        GL11.glPushMatrix();
////        GL11.glEnable(GL11.GL_SCISSOR_TEST);
////        RenderUtils.scissorFromTopLeft(xPos, yPos, width, height);
//
////        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos - 20, yPos - 20, width + 40, height + 40, 6, baseColor.copy().setA(0.8F), true);
//        RenderUtils.drawRoundedRect(GL11.GL_POLYGON, xPos, yPos, width, height, 6, new Color("#242424").setA(0.8F), true);
//
//        int value = slider.getValueInt();
//
//        // TODO: all this should be in onScreenTick?
//        if (slider.isMouseDown() && lastSliderValue != value)
//        {
////            System.out.println(lastSliderValue - value);
//
//            for (GuiElement element : getNestedChildren())
//            {
//                if (element == slider)
//                {
//                    continue;
//                }
//
//                element.yPos += (lastSliderValue - value);
//
//                // TODO: last working here
//                element.visible = ((element.yPos + element.height > element.parent.yPos)
//                        && (element.yPos < element.parent.yPos + element.parent.height));
//
////                element.visible = ((element.yPos + element.height > this.yPos) && (element.yPos < this.yPos + this.height))
////                        && ((element.yPos + element.height > element.parent.yPos) && (element.yPos < element.parent.yPos + element.parent.height));
//
////                element.visible = (element.yPos + element.height > this.yPos) && (element.yPos < this.yPos + this.height);
//
//                if (element instanceof GuiInteractableElement)
//                {
//                    ((GuiInteractableElement) element).setEnabled(element.visible);
//                }
//            }
//
//            lastSliderValue = value;
//            System.out.println("slider value changed: " + slider.elementName);
//        }
//    }
//
////    @Override
////    public void handleScreenTick()
////    {
////        this.onScreenTick();
////        childrenMap.values().forEach(GuiElement::onScreenTick);
////    }
////
////    @Override
////    public void handleKeyTyped(char typedChar, int keyCode)
////    {
////        for (GuiInteractableElement element : interactableChildrenMap.values())
////        {
////            element.onKeyTyped(typedChar, keyCode);
////        }
////    }
//
//    @Override
//    public void handleMouseDown(int mouseX, int mouseY)
//    {
////        if (slider.wasMousePressed(mouseX, mouseY))
////        {
////            System.out.println("slider pressed: " + slider.elementName);
////            slider.playPressSound(this.mc.getSoundHandler());
////            slider.onMousePress(mouseX, mouseY);
////            lastMouseDownElement = slider;
////            return null;
////        }
//
//        super.handleMouseDown(mouseX, mouseY);
////        return null;
//
////        getNestedChildren().forEach(System.out::println);
//    }
//
////    public void handleMouseRelease(int mouseX, int mouseY)
////    {
////        if (lastMouseDownElement != null)
////        {
////            lastMouseDownElement.onMouseRelease(mouseX, mouseY);
////            lastMouseDownElement = null;
////        }
////    }
//}

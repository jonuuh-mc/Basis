package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class GuiRootContainer extends GuiContainer
{
    public GuiRootContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, Map<String, Color> colorMap, float outerRadius, float innerRadius, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, colorMap, outerRadius, innerRadius, tooltipStr);
    }

//    public List<GuiElement> getNestedChildren()
//    {
//        List<GuiElement> elements = new ArrayList<>();
////        elements.add(this);
//
//        for (GuiElement element : childrenMap.values())
//        {
//            if (element instanceof GuiContainer)
//            {
//                elements.add(element);
//                elements.addAll(((GuiContainer) element).getNestedChildren());
//            }
//            else
//            {
//                elements.add(element);
//            }
//        }
//
//        return elements;
//    }
//
//    public List<GuiContainer> getNestedContainers()
//    {
//        return getNestedChildren().stream()
//                .filter(guiElement -> guiElement instanceof GuiContainer)
//                .map(guiElement -> (GuiContainer) guiElement)
//                .collect(Collectors.toList());
//    }
//
//    public Map<String, Color> getColorMap()
//    {
//        return colorMap;
//    }
//
//    public float getOuterRadius()
//    {
//        return outerRadius;
//    }
//
//    public float getInnerRadius()
//    {
//        return innerRadius;
//    }

    public void putChild(String elementName, GuiElement element)
    {
        element.xPos += this.xPos;
        element.yPos += this.yPos;
        element.xPosInit += this.xPosInit;
        element.yPosInit += this.yPosInit;

        element.zLevel = getNumParents();
        System.out.println(element.elementName + " zLevel: " + element.zLevel);

        childrenMap.put(elementName, element);
        // TODO: hold reference to root and always add to map in it?

        if (element instanceof GuiInteractableElement)
        {
            interactableChildrenMap.put(elementName, (GuiInteractableElement) element);
        }
    }

    public void putChildren(Map<String, GuiElement> map)
    {
        for (Map.Entry<String, GuiElement> entry : map.entrySet())
        {
            putChild(entry.getKey(), entry.getValue());
        }
    }

    public void setXPos(int xPos)
    {
        this.xPos = xPos;

        for (GuiElement element : getNestedChildren())
        {
            element.setXPos(element.xPosInit + xPos - this.xPosInit);
        }
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;

        for (GuiElement element : getNestedChildren())
        {
            element.setYPos(element.yPosInit + yPos - this.yPosInit);
        }
    }

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
//        GL11.glPushMatrix();
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        RenderUtils.scissorFromTopLeft(xPos, yPos, width, height);

        super.onScreenDraw(mouseX, mouseY, partialTicks);

//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//        GL11.glPopMatrix();
        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));

//        for (GuiElement element : getNestedChildren())
//        {
//            if (!(element instanceof GuiContainer))
//            {
//                element.onScreenDraw(mouseX, mouseY, partialTicks);
//            }
//            else
//            {
//                super.onScreenDraw(mouseX, mouseY, partialTicks);
////                System.out.println(element.elementName + " " + element.getNumParents());
//                if (element.getNumParents() == 0)
//                {
//                    super.onScreenDraw(mouseX, mouseY, partialTicks);
//
////                    element.onScreenDraw(mouseX, mouseY, partialTicks);
//                }
//            }
//        }

    }

    public void handleScreenTick()
    {
        this.onScreenTick();
        childrenMap.values().forEach(GuiElement::onScreenTick);
    }

    // TODO: make recursive
    public void handleKeyTyped(char typedChar, int keyCode)
    {
        for (GuiInteractableElement element : interactableChildrenMap.values())
        {
            element.onKeyTyped(typedChar, keyCode);
        }
    }

    public void handleMouseDown(int mouseX, int mouseY)
    {
        List<GuiInteractableElement> allMouseDownElements = getMouseDownElements(mouseX, mouseY);

        System.out.println(allMouseDownElements);

        if (allMouseDownElements.isEmpty())
        {
            return;
        }

        GuiInteractableElement highestZLevelElement = allMouseDownElements.get(0);

        for (GuiInteractableElement element : allMouseDownElements)
        {
            if (element.getZLevel() > highestZLevelElement.getZLevel())
            {
                highestZLevelElement = element;
            }
        }

        System.out.println(highestZLevelElement);

        highestZLevelElement.playPressSound(this.mc.getSoundHandler());
        highestZLevelElement.onMousePress(mouseX, mouseY);
        lastMouseDownElement = highestZLevelElement;
        currentFocus = highestZLevelElement;
    }

    private List<GuiInteractableElement> getMouseDownElements(int mouseX, int mouseY)
    {
        List<GuiInteractableElement> elements = new ArrayList<>();

        for (GuiElement element : childrenMap.values())
        {
//            System.out.println(element.elementName);

            if (element instanceof GuiContainer)
            {
//                elements.addAll(((GuiContainer) element).getMouseDownElements(mouseX, mouseY));
            }
            else if (element instanceof GuiInteractableElement)
            {
                GuiInteractableElement interactableElement = (GuiInteractableElement) element;

                if (interactableElement.wasMousePressed(mouseX, mouseY))
                {
                    elements.add(interactableElement);
                }
            }
        }

        return elements;
    }

    public void handleMouseRelease(int mouseX, int mouseY)
    {
        if (lastMouseDownElement != null)
        {
            lastMouseDownElement.onMouseRelease(mouseX, mouseY);
            lastMouseDownElement = null;
        }
    }

    // TODO: make recursive
    public void handleMouseScroll(int wheelDelta)
    {
        for (GuiInteractableElement element : interactableChildrenMap.values())
        {
            element.onMouseScroll(wheelDelta);
        }
    }

    // TODO: fix awful recursive logic
    public void handleMouseDrag(int mouseX, int mouseY, int clickedMouseButton, long msHeld)
    {
        for (GuiElement element : childrenMap.values())
        {
            if (element instanceof GuiContainer)
            {
                ((GuiContainer) element).handleMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
            }
            else if (element instanceof GuiInteractableElement)
            {
                ((GuiInteractableElement) element).onMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
            }
        }


//        for (GuiElement element : getNestedChildren())
//        {
//            if (element instanceof GuiInteractableElement)
//            {
//                ((GuiInteractableElement) element).onMouseDrag(mouseX, mouseY, clickedMouseButton, msHeld);
//            }
//        }
    }


    @Override
    protected void drawElement(int mouseX, int mouseY, float partialTicks)
    {

    }
}

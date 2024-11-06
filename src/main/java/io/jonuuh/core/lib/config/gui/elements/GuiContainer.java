package io.jonuuh.core.lib.config.gui.elements;

import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.util.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class GuiContainer extends GuiElement
{
    protected final Map<String, GuiElement> childrenMap;
    protected final Map<String, GuiInteractableElement> interactableChildrenMap;
    protected final Map<String, Color> colorMap;
    protected float outerRadius;
    protected float innerRadius;
    protected GuiInteractableElement lastMouseDownElement;

    public GuiContainer(GuiContainer parent, String elementName, int xPos, int yPos, int width, int height, Map<String, Color> colorMap, float outerRadius, float innerRadius, String tooltipStr)
    {
        super(parent, elementName, xPos, yPos, width, height, true, tooltipStr);
        this.childrenMap = new LinkedHashMap<>();
        this.interactableChildrenMap = new LinkedHashMap<>();
        this.colorMap = colorMap;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
    }

    public Map<String, GuiElement> getChildrenMap()
    {
        return childrenMap;
    }

    public Collection<GuiElement> getChildren()
    {
        return childrenMap.values();
    }

    public Map<String, Color> getColorMap()
    {
        return colorMap;
    }

    public float getOuterRadius()
    {
        return outerRadius;
    }

    public float getInnerRadius()
    {
        return innerRadius;
    }

    public void putChild(String elementName, GuiElement element)
    {
        childrenMap.put(elementName, element);

//        int padding = 5;

        element.xPos += this.xPos /*+ padding*/ /*+ (hasParent() ? parent.xPos *//*+ padding*//* : 0)*/;
        element.yPos += this.yPos /*+ padding *//*+ (hasParent() ? parent.yPos *//*+ padding*//* : 0)*/;
        element.xPosInit += this.xPosInit /*+ padding*/ /*+ (hasParent() ? parent.xPosInit *//*+ padding*//* : 0)*/;
        element.yPosInit += this.yPosInit /*+ padding*/ /*+ (hasParent() ? parent.yPosInit *//*+ padding*//* : 0)*/;

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

    @Override
    public void onScreenDraw(int mouseX, int mouseY, float partialTicks)
    {
        super.onScreenDraw(mouseX, mouseY, partialTicks);

        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));

//        for (GuiElement element : childrenMap.values())
//        {
//            if (element instanceof GuiContainer) // GuiScrollContainer
//            {
//                ((GuiContainer) element).handleScreenDraw(mouseX, mouseY, partialTicks);
//            }
//            else
//            {
//                element.onScreenDraw(mouseX, mouseY, partialTicks);
//            }
//        }

//        childrenMap.values().forEach(element -> element.onScreenDraw(mouseX, mouseY, partialTicks));
    }

    public void handleScreenTick()
    {
        this.onScreenTick();
        childrenMap.values().forEach(GuiElement::onScreenTick);
    }

    public void handleKeyTyped(char typedChar, int keyCode)
    {
        for (GuiInteractableElement element : interactableChildrenMap.values())
        {
            element.onKeyTyped(typedChar, keyCode);
        }
    }

    public List<GuiInteractableElement> handleMouseDown(int mouseX, int mouseY)
    {
        List<GuiInteractableElement> mouseDownElements = new ArrayList<>();

        for (GuiElement element : childrenMap.values())
        {
            System.out.println(element.elementName);

            if (element instanceof GuiContainer) // GuiScrollContainer
            {
                mouseDownElements.addAll(((GuiContainer) element).interactableChildrenMap.values());

//                for (GuiInteractableElement child : ((GuiContainer) element).interactableChildrenMap.values())
//                {
//                    if (child.wasMousePressed(mouseX, mouseY)) // TODO: last working here 10/30/24
//                    {
//                        mouseDownElements.add(child);
//                    }
//                    else
//                    {
//                        child.setFocused(false);
//                    }
//                }

//                ((GuiContainer) element).mouseDown(mouseX, mouseY);
                ((GuiContainer) element).handleMouseDown(mouseX, mouseY);
            }
//            System.out.println(element.elementName);

        }

        return mouseDownElements;
    }

    public void mouseDown(int mouseX, int mouseY)
    {
        List<GuiInteractableElement> mouseDownElements = new ArrayList<>();

        for (GuiInteractableElement element : interactableChildrenMap.values())
        {
            if (element.wasMousePressed(mouseX, mouseY))
            {
                mouseDownElements.add(element);
            }
            else
            {
                element.setFocused(false);
            }
        }

        if (mouseDownElements.isEmpty())
        {
            return;
        }

        GuiInteractableElement highestZLevelElement = mouseDownElements.get(0);

        for (GuiInteractableElement element : mouseDownElements)
        {
            if (element.zLevel > highestZLevelElement.zLevel)
            {
                highestZLevelElement = element;
            }
        }

        highestZLevelElement.playPressSound(this.mc.getSoundHandler());
        highestZLevelElement.onMousePress(mouseX, mouseY);
        lastMouseDownElement = highestZLevelElement;
    }

    public void handleMouseRelease(int mouseX, int mouseY)
    {
        if (lastMouseDownElement != null)
        {
            lastMouseDownElement.onMouseRelease(mouseX, mouseY);
            lastMouseDownElement = null;
        }
    }
}

package io.jonuuh.basis.lib.gui.event;

import io.jonuuh.basis.lib.gui.element.GuiElement;
import io.jonuuh.basis.lib.gui.element.container.GuiContainer;
import io.jonuuh.basis.lib.gui.event.input.InputEvent;

import java.util.List;

public final class EventDispatcher
{
    private EventDispatcher()
    {
    }

    public static void dispatchTargeted(GuiTargetedEvent event)
    {
        // root -> ... -> target
        List<GuiElement> path = event.target.getPropagationPath();
        int targetIndex = path.size() - 1;

        for (int i = 0; i <= targetIndex; i++)
        {
            GuiElement element = path.get(i);
            event.currentTarget = element;
            boolean atTarget = (i == targetIndex);

            if (isBlocked(event, element))
            {
                continue;
            }

            element.notifyListeners(event, EventPhase.CAPTURE, atTarget);

            if (event.isPropagationStopped())
            {
                break;
            }
        }

        if (event.bubbles() && !event.isPropagationStopped())
        {
            for (int i = targetIndex - 1; i >= 0; i--)
            {
                GuiElement element = path.get(i);
                event.currentTarget = element;

                if (isBlocked(event, element))
                {
                    continue;
                }

                element.notifyListeners(event, EventPhase.BUBBLE, false);

                if (event.isPropagationStopped())
                {
                    break;
                }
            }
        }

//        if (!event.isDefaultPrevented())
//        {
//            // some kind of default for the element
//        }
    }

    public static void dispatchUntargeted(GuiElement root, GuiUntargetedEvent event)
    {
        visitNode(root, event);
    }

    private static void visitNode(GuiElement element, GuiUntargetedEvent event)
    {
        event.currentTarget = element;

        element.notifyListeners(event, EventPhase.BUBBLE, true); // atTarget=true: every element is its own "target"
//        if (!event.isDefaultPrevented())
//        {
//        }

        // halts the whole broadcast, not just this subtree
        if (event.isPropagationStopped())
        {
            return;
        }

        if (element instanceof GuiContainer)
        {
            for (GuiElement child : ((GuiContainer) element).getChildren())
            {
                visitNode(child, event);

                if (event.isPropagationStopped())
                {
                    return;
                }
            }
        }
    }

    private static boolean isBlocked(GuiEvent event, GuiElement element)
    {
        return event instanceof InputEvent && !element.isEnabled();
    }
}

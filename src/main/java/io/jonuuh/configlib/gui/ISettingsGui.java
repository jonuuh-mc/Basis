package io.jonuuh.configlib.gui;

import io.jonuuh.configlib.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.configlib.util.Color;

public interface ISettingsGui
{
    Color getBaseColor();

    Color getAccentColor();

    Color getDisabledColor();

    float getOuterRadius();

    float getInnerRadius();

    void onChange(GuiInteractableElement element);
}
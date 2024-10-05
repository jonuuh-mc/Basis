package io.jonuuh.configlib.gui;

import io.jonuuh.configlib.gui.elements.GuiInteractableElement;
import io.jonuuh.configlib.util.Color;

public interface ISettingsGui
{
    Color getBaseColor();

    Color getAccentColor();

    Color getDisabledColor();

    void onChange(GuiInteractableElement element);
}
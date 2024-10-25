package io.jonuuh.core.lib.config.gui;

import io.jonuuh.core.lib.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.lib.util.Color;
import net.minecraft.client.Minecraft;

public interface ISettingsGui
{
    Minecraft getMc();

    Color getBaseColor();

    Color getAccentColor();

    Color getDisabledColor();

    float getOuterRadius();

    float getInnerRadius();

    void onChange(GuiInteractableElement element);
}
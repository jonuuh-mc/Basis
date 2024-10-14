package io.jonuuh.core.module.config.gui;

import io.jonuuh.core.module.config.gui.elements.interactable.GuiInteractableElement;
import io.jonuuh.core.module.util.Color;
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
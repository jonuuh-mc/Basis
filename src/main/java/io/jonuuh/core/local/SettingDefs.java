//package io.jonuuh.core.local;
//
//import io.jonuuh.core.module.config.gui.elements.interactable.GuiInteractableElement;
//import io.jonuuh.core.module.config.gui.elements.interactable.GuiSwitch;
//import io.jonuuh.core.module.config.gui.elements.interactable.sliders.GuiSingleSlider;
//import io.jonuuh.core.module.config.setting.SettingType;
//
//public enum SettingDefs
//{
//    DRAW_BACKGROUND(SettingType.BOOLEAN, GuiSwitch.class),
//    BACKGROUND_OPACITY(SettingType.DOUBLE, GuiSingleSlider.class),
//    RENDER_RANGE_MIN(SettingType.INTEGER, GuiSingleSlider.class);
//
//    public final SettingType type;
//    public final Class<? extends GuiInteractableElement> elementClass;
//
//    SettingDefs(SettingType type, Class<? extends GuiInteractableElement> elementClass)
//    {
//        this.type = type;
//        this.elementClass = elementClass;
//    }
//
//    SettingDefs(SettingType type)
//    {
//        this(type, null);
//    }
//
//    public static String[] names()
//    {
//        String[] names = new String[values().length];
//
//        for (int i = 0; i < names.length; i++)
//        {
//            names[i] = values()[i].toString();
//        }
//        return names;
//    }
//}

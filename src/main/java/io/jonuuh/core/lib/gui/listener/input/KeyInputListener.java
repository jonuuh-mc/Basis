package io.jonuuh.core.lib.gui.listener.input;

import io.jonuuh.core.lib.gui.event.input.KeyInputEvent;

public interface KeyInputListener extends InputListener
{
    void onKeyTyped(KeyInputEvent event);
}

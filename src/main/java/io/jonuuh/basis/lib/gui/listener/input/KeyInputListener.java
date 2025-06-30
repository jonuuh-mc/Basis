package io.jonuuh.basis.lib.gui.listener.input;

import io.jonuuh.basis.lib.gui.event.input.KeyInputEvent;

public interface KeyInputListener extends InputListener
{
    void onKeyTyped(KeyInputEvent event);
}

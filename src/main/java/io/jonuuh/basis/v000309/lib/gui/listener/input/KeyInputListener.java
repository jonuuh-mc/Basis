package io.jonuuh.basis.v000309.lib.gui.listener.input;

import io.jonuuh.basis.v000309.lib.gui.event.input.KeyInputEvent;

public interface KeyInputListener extends InputListener
{
    void onKeyTyped(KeyInputEvent event);
}

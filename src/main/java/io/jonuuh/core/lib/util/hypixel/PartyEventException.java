package io.jonuuh.core.lib.util.hypixel;

public class PartyEventException extends RuntimeException
{
    public PartyEventException()
    {
    }

    public PartyEventException(String message)
    {
        super(message);
    }

    public PartyEventException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public class CommandEvent extends Event {
    private final String input;
    public CommandEvent(String input) { this.input = input; }
    /** Teks perintah yang diketik (di NeoForge: getParseResults().getReader().getString()). */
    public String getInput() { return input; }
}

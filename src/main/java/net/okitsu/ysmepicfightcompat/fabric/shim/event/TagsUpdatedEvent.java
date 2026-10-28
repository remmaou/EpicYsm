package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public class TagsUpdatedEvent extends Event {
    public enum UpdateCause { SERVER_DATA_LOAD, CLIENT_PACKET_RECEIVED }
    private final UpdateCause cause;
    public TagsUpdatedEvent(UpdateCause cause) { this.cause = cause; }
    public UpdateCause getUpdateCause() { return cause; }
}

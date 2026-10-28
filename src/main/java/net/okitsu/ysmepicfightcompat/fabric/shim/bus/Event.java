package net.okitsu.ysmepicfightcompat.fabric.shim.bus;

public abstract class Event {
    private boolean canceled;
    public boolean isCanceled() { return canceled; }
    public void setCanceled(boolean canceled) { this.canceled = canceled; }
}

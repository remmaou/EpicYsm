package net.okitsu.ysmmapping.api;

/** Rujukan method YSM: owner (internal name), nama, descriptor. */
public record YsmMethodSymbol(String owner, String name, String descriptor) {
    public String binaryOwner() { return owner.replace('/', '.'); }
}

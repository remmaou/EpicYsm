package net.okitsu.ysmepicfightcompat.fabric.shim.event;

import net.okitsu.ysmepicfightcompat.fabric.shim.bus.Event;

public abstract class ScreenEvent extends Event {
    public abstract static class MouseButtonPressed extends ScreenEvent {
        public static class Post extends MouseButtonPressed {
            public enum Result { DEFAULT, FORCE_HANDLED, FORCE_UNHANDLED }
            private final boolean handled;
            private Result result = Result.DEFAULT;
            public Post(boolean handled) { this.handled = handled; }
            public boolean wasClickHandled() { return handled; }
            public Result getResult() { return result; }
            public void setResult(Result result) { this.result = result; }
        }
    }
}

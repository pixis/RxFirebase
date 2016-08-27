package com.kelvinapps.rxfirebase;

public class RxFirebaseChildEventBuilder<T> {
    private T data;
    private String previousChildName = "";
    private RxFirebaseChildEvent.EventType eventType;

    public RxFirebaseChildEventBuilder setData(T data) {
        this.data = data;
        return this;
    }

    public RxFirebaseChildEventBuilder setPreviousChildName(String previousChildName) {
        this.previousChildName = previousChildName;
        return this;
    }

    public RxFirebaseChildEventBuilder setEventType(RxFirebaseChildEvent.EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public RxFirebaseChildEvent createRxFirebaseChildEvent() {
        return new RxFirebaseChildEvent(data, previousChildName, eventType);
    }
}
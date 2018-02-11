package com.meetup;

import lombok.Data;

@Data
public class ReservationMadeEvent {
    private final String id;
    private final String room;
    private final Long fromTime;
    private final Long toTime;

    public ReservationMadeEvent(final String id, final String room, final Long fromTime, final Long toTime) {
        this.id = id;
        this.room = room;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}

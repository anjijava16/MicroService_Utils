package com.meetup.reservationcommandservice;

import lombok.Data;

@Data
public class MakeReservationCommand {
    private final String id;
    private final String room;
    private final Long fromTime;
    private final Long toTime;

    public MakeReservationCommand(final String id, final String room, final Long fromTime, final Long toTime) {
        this.id = id;
        this.room = room;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }
}

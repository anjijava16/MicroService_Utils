package com.meetup.reservationqueryservice;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class RoomReservation {
    @Id
    private String id;
    private String room;
    private Long fromTime;
    private Long toTime;
}

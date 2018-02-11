package com.meetup.reservationqueryservice;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.meetup.ReservationMadeEvent;

@ProcessingGroup("queryModel")
@Component
public class ReadModelUpdater {

    private final RoomReservationRepository roomReservationRepository;

    @Autowired
    public ReadModelUpdater(final RoomReservationRepository roomReservationRepository) {
        this.roomReservationRepository = roomReservationRepository;
    }

    @EventHandler
    public void on(ReservationMadeEvent rme) {
        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setId(rme.getId());
        roomReservation.setRoom(rme.getRoom());
        roomReservation.setFromTime(rme.getFromTime());
        roomReservation.setToTime(rme.getToTime());

        roomReservationRepository.save(roomReservation);
    }

}
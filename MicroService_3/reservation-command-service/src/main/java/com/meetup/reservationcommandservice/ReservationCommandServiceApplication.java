package com.meetup.reservationcommandservice;

import java.util.Map;
import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.meetup.ReservationMadeEvent;

@SpringBootApplication
public class ReservationCommandServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationCommandServiceApplication.class, args);
    }

    @RestController
    public static class RoomReservationApi {
        private final RoomReservationRepository roomReservationRepository;
        private final CommandGateway commandGateway;

        @Autowired
        public RoomReservationApi(final RoomReservationRepository roomReservationRepository,
                final CommandGateway commandGateway) {
            this.roomReservationRepository = roomReservationRepository;
            this.commandGateway = commandGateway;
        }

        @PostMapping
        public void makeReservation(@RequestBody final Map<String, Object> reservationData) {
            final String id = UUID.randomUUID().toString();
            final Long fromTime = Long.valueOf(reservationData.get("fromTime").toString());
            final Long toTime = Long.valueOf(reservationData.get("toTime").toString());
            commandGateway.send(new MakeReservationCommand(id, reservationData.get("room").toString(), fromTime, toTime));
        }
    }

    @Aggregate
    public static class ReservationAggregate {
        @AggregateIdentifier
        private String aggregateIdentifier;

        @CommandHandler
        public ReservationAggregate(MakeReservationCommand makeReservationCommand) {
            AggregateLifecycle
                    .apply(new ReservationMadeEvent(makeReservationCommand.getId(), makeReservationCommand.getRoom(), makeReservationCommand.getFromTime(), makeReservationCommand
                            .getToTime()));
        }

        @EventSourcingHandler
        public void on(ReservationMadeEvent rme) {
            this.aggregateIdentifier = rme.getId();
        }
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange("Reservations").build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("Reservations").build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin amqpAdmin) {
        amqpAdmin.declareExchange(exchange());
        amqpAdmin.declareQueue(queue());
        amqpAdmin.declareBinding(binding());
    }
}

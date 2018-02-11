package com.meetup.reservationqueryservice;

import java.util.List;

import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.Channel;

@SpringBootApplication
public class ReservationQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationQueryServiceApplication.class, args);
    }

    @RestController
    public static class RoomReservationApi {
        private final RoomReservationRepository roomReservationRepository;

        @Autowired
        public RoomReservationApi(final RoomReservationRepository roomReservationRepository) {
            this.roomReservationRepository = roomReservationRepository;
        }

        @GetMapping
        public List<RoomReservation> findAll() {
            return roomReservationRepository.findAll();
        }

        @GetMapping("/{id}")
        public RoomReservation findOne(@PathVariable final String id) {
            return roomReservationRepository.findOne(id);
        }
    }

    @Bean
    public SpringAMQPMessageSource amqpMessageSource(final Serializer serializer) {
        return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)) {
            @RabbitListener(queues = "Reservations")
            @Override
            public void onMessage(final Message message, final Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }
}

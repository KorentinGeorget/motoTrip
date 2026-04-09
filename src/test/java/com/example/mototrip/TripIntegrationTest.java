package com.example.mototrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TripIntegrationTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepo;

    @Test
    void shouldPersistNewUser() {
        User user = tripService.createUser("user", false);
        assertNotNull(user.getId());
    }

    @Test
    void shouldPersistNewTrip() {
        Trip trip = tripService.createTrip("trip", 10, false);
        assertNotNull(trip.getId());
    }

    @Test
    void shouldPersistNewUserAndTripAndEstablishOneToManyRelationship() {
        User user = tripService.createUser("user", false);
        Trip trip = tripService.createTrip("trip", 10, false);

        assertNotNull(user.getId());
        assertNotNull(trip.getId());

        tripService.joinTrip(trip.getId(), user.getId());

        Trip updatedTrip = tripRepo.findById(trip.getId()).orElseThrow();
        assertEquals(9, updatedTrip.remainingPlaces());
        assertEquals(1, updatedTrip.getParticipants().size());
    }
}

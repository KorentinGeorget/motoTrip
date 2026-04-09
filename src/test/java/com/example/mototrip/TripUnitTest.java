package com.example.mototrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TripUnitTest {

    @Test
    void shouldJoinTrip() {
        Trip trip = new Trip("trip", 2, false);
        User user = new User("John", false);
        trip.join(user);
        assertEquals(1, trip.remainingPlaces());
    }

    @Test
    void shouldThrowExceptionWhenUserTriesToJoinAlreadyStartedTrip() {
        Trip trip = new Trip("trip", 2, false);
        User user = new User("John", false);
        trip.join(user);
        trip.start();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trip.join(new User("Bob", false)));
        assertEquals("Trip already started", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNonPremiumUserTriesToJoinPremiumTrip() {
        Trip trip = new Trip("trip", 2, true);
        User user = new User("John", false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trip.join(user));
        assertEquals("Premium required", exception.getMessage());
    }

    @Test
    void shouldAllowPremiumUserToJoinPremiumTrip() {
        Trip trip = new Trip("trip", 2, true);
        User user = new User("John", true);
        trip.join(user);
        assertEquals(1, trip.remainingPlaces());
    }

    @Test
    void shouldThrowExceptionWhenTryToJoinFullTrip() {
        Trip trip = new Trip("trip", 1, false);
        User user1 = new User("John", false);
        User user2 = new User("Bob", false);
        trip.join(user1);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trip.join(user2));
        assertEquals("Trip full", exception.getMessage());
    }

    @Test
    void shouldStartTripSuccessfullyWhenItHasAtLeastOneParticipant() {
        Trip trip = new Trip("trip", 2, false);
        User user = new User("John", false);
        trip.join(user);
        trip.start();
        assertEquals(true, trip.isStarted());
    }

    @Test
    void shouldThrowExceptionWhenStartingTripWithoutParticipants() {
        Trip trip = new Trip("trip", 2, false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> trip.start());
        assertEquals("No participants", exception.getMessage());
    }

}

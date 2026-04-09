package com.example.mototrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private TripService tripService;

    @Test
    void shouldReturnCreatedTrip() {
        Trip trip = new Trip("trip", 10, true);
        when(tripRepo.save(any(Trip.class))).thenReturn(trip);
        Trip created = tripService.createTrip("trip", 10, true);
        assertNotNull(created);
        verify(tripRepo, times(1)).save(any(Trip.class));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -100 })
    void shouldThrowIllegalArgumentExceptionWhenCreatingTripWithNonPositiveCapacity(int capacity) {
        assertThrows(IllegalArgumentException.class, () -> tripService.createTrip("Invalid", capacity, false));
    }

    @Test
    void shouldReturnCreatedUser() {
        User user = new User("John", true);
        when(userRepo.save(any(User.class))).thenReturn(user);
        User created = tripService.createUser("John", true);
        assertNotNull(created);
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenJoiningTripThatDoesNotExist() {
        when(tripRepo.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> tripService.joinTrip(1L, 1L));
        assertEquals("Trip not found", ex.getMessage());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenJoiningTripWithUserThatDoesNotExist() {
        Trip trip = new Trip("trip", 5, false);
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> tripService.joinTrip(1L, 1L));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void shouldUpdateTripWhenUserSuccessfullyJoinsExistingTrip() {
        Trip trip = new Trip("trip", 5, false);
        User user = new User("John", false);
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(tripRepo.save(trip)).thenReturn(trip);
        Trip result = tripService.joinTrip(1L, 1L);
        assertEquals(4, result.remainingPlaces());
        verify(tripRepo, times(1)).save(trip);
    }

    @Test
    void shouldStartTripSuccessfullyAndPersistTheState() {
        Trip trip = new Trip("trip", 5, false);
        User user = new User("John", false);
        trip.join(user);
        when(tripRepo.findById(1L)).thenReturn(Optional.of(trip));
        when(tripRepo.save(trip)).thenReturn(trip);
        Trip result = tripService.startTrip(1L);
        assertTrue(result.isStarted());
        verify(tripRepo, times(1)).save(trip);
    }

    @Test
    void shouldReturnAllTripsFoundInRepository() {
        List<Trip> trips = Arrays.asList(new Trip("trip 1", 5, false), new Trip("trip 2", 10, true));
        when(tripRepo.findAll()).thenReturn(trips);
        List<Trip> result = tripService.allTrips();
        assertEquals(2, result.size());
        verify(tripRepo, times(1)).findAll();
    }
}

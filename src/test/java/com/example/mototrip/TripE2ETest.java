package com.example.mototrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TripE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TripRepository tripRepo;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setupAndCleanDatabase() {
        tripRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void shouldCompleteFullWorkflowFromUserCreationToTripStartedScenarioSuccessfully() {
        // 1. Create User
        Map<String, Object> userRequest = new HashMap<>();
        userRequest.put("name", "user");
        userRequest.put("premium", false);
        ResponseEntity<Map> userResponse = restTemplate.postForEntity("/api/users", userRequest, Map.class);
        Integer userId = (Integer) userResponse.getBody().get("id");
        assertNotNull(userId);

        // 2. Create Trip
        Map<String, Object> tripRequest = new HashMap<>();
        tripRequest.put("name", "trip");
        tripRequest.put("maxParticipants", 2);
        tripRequest.put("premiumOnly", false);
        ResponseEntity<Map> tripResponse = restTemplate.postForEntity("/api/trips", tripRequest, Map.class);
        Integer tripId = (Integer) tripResponse.getBody().get("id");
        assertNotNull(tripId);

        // 3. Join Trip
        restTemplate.postForEntity("/api/trips/" + tripId + "/join?userId=" + userId, null, Map.class);

        // 4. Start Trip
        ResponseEntity<Map> startResponse = restTemplate.postForEntity("/api/trips/" + tripId + "/start", null,
                Map.class);
        assertEquals(200, startResponse.getStatusCode().value());

        // 5. Verify via list
        ResponseEntity<Map[]> allTripsResponse = restTemplate.getForEntity("/api/trips", Map[].class);
        Map[] trips = allTripsResponse.getBody();
        assertEquals(1, trips.length);
        assertEquals("trip", trips[0].get("name"));
        assertEquals(1, ((java.util.List) trips[0].get("participants")).size());
    }
}

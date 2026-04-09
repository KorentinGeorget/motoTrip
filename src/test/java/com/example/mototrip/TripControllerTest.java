package com.example.mototrip;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    @Test
    void shouldReturnCreatedUserWhenPostingValidUserData() throws Exception {
        User user = new User("Alice", true);
        user.setId(1L);

        given(tripService.createUser(anyString(), anyBoolean())).willReturn(user);

        Map<String, Object> request = new HashMap<>();
        request.put("name", "Alice");
        request.put("premium", true);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.premium").value(true));
    }

    @Test
    void shouldReturnCreatedTripWhenPostingValidTripData() throws Exception {
        Trip trip = new Trip("trip", 10, false);
        trip.setId(1L);

        given(tripService.createTrip(anyString(), anyInt(), anyBoolean())).willReturn(trip);

        Map<String, Object> request = new HashMap<>();
        request.put("name", "trip");
        request.put("maxParticipants", 10);
        request.put("premiumOnly", false);

        mockMvc.perform(post("/api/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("trip"))
                .andExpect(jsonPath("$.maxParticipants").value(10));
    }

    @Test
    void shouldReturnUpdatedTripSuccessfullyWhenUserJoinsTrip() throws Exception {
        Trip trip = new Trip("trip", 5, false);
        trip.setId(1L);

        given(tripService.joinTrip(anyLong(), anyLong())).willReturn(trip);

        mockMvc.perform(post("/api/trips/1/join")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("trip"));
    }

    @Test
    void shouldReturnStartedTripSuccessfullyWhenCallingStartEndpoint() throws Exception {
        Trip trip = new Trip("trip", 5, false);
        trip.setId(1L);
        trip.setStarted(true);

        given(tripService.startTrip(anyLong())).willReturn(trip);

        mockMvc.perform(post("/api/trips/1/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.started").value(true));
    }

    @Test
    void shouldReturnAllTripsSuccessfullyWhenCallingGetAllTripsEndpoint() throws Exception {
        Trip t1 = new Trip("Trip 1", 5, false);
        Trip t2 = new Trip("Trip 2", 10, true);

        given(tripService.allTrips()).willReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

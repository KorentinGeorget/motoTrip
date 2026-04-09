package com.example.mototrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserUnitTest {

    @Test
    void shouldHave0PointsWhenUserIsCreated() {
        User user = new User("John", false);
        assertEquals(0, user.getPoints());
    }

    @Test
    void shouldAddPointsWhenAddingPoints() {
        User user = new User("John", false);
        user.addPoints(10);
        assertEquals(10, user.getPoints());
    }

    @Test
    void shouldReturnTrueWhenUserIsPremium() {
        User user = new User("Alice", true);
        assertTrue(user.canJoinPremium());
    }

    @Test
    void shouldReturnFalseWhenUserIsNotPremium() {
        User user = new User("Bob", false);
        assertFalse(user.canJoinPremium());
    }
}

package com.crudapp.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRequestTest {

    @Test
    public void testAuthRequest() {
        String username = "testUser";
        String password = "testPassword";
        AuthRequest authRequest = new AuthRequest(username, password);

        assertThat(authRequest.getUsername()).isEqualTo(username);
        assertThat(authRequest.getPassword()).isEqualTo(password);
    }

    @Test
    public void testNoArgsConstructor() {
        AuthRequest authRequest = new AuthRequest();
        assertThat(authRequest.getUsername()).isNull();
        assertThat(authRequest.getPassword()).isNull();
    }

    @Test
    public void testSettersAndGetters() {
        AuthRequest authRequest = new AuthRequest();
        String username = "newUser";
        String password = "newPassword";

        authRequest.setUsername(username);
        authRequest.setPassword(password);

        assertThat(authRequest.getUsername()).isEqualTo(username);
        assertThat(authRequest.getPassword()).isEqualTo(password);

        // Test setting null values
        authRequest.setUsername(null);
        authRequest.setPassword(null);
        assertThat(authRequest.getUsername()).isNull();
        assertThat(authRequest.getPassword()).isNull();
    }

    @Test
    public void testToString() {
        AuthRequest authRequest = new AuthRequest("testUser", "testPassword");
        String stringRepresentation = authRequest.toString();

        // Check that the string contains both username and password
        assertThat(stringRepresentation).contains("testUser", "testPassword");
        // You can also verify the exact format if needed
    }

    @Test
    public void testEquals() {
        AuthRequest authRequest1 = new AuthRequest("user1", "password1");
        AuthRequest authRequest2 = new AuthRequest("user1", "password1");
        AuthRequest authRequest3 = new AuthRequest("user2", "password2");
        Object otherObject = new Object(); // Different class type

        // Same object
        assertThat(authRequest1).isEqualTo(authRequest1); // Should be equal

        // Equal objects
        assertThat(authRequest1).isEqualTo(authRequest2); // Should be equal

        // Different objects with different values
        assertThat(authRequest1).isNotEqualTo(authRequest3); // Should not be equal

        // Different class type
        assertThat(authRequest1).isNotEqualTo(otherObject); // Should not be equal

        // Test with null values
        authRequest1 = new AuthRequest(null, null);
        authRequest2 = new AuthRequest(null, null);
        assertThat(authRequest1).isEqualTo(authRequest2);
    }

    @Test
    public void testHashCode() {
        AuthRequest authRequest1 = new AuthRequest("user1", "password1");
        AuthRequest authRequest2 = new AuthRequest("user1", "password1");
        AuthRequest authRequest3 = new AuthRequest("user2", "password2");

        // Same hash code for equal objects
        assertThat(authRequest1.hashCode()).isEqualTo(authRequest2.hashCode());

        // Different hash code for unequal objects
        assertThat(authRequest1.hashCode()).isNotEqualTo(authRequest3.hashCode());
    }

    @Test
    public void testCanEqual() {
        AuthRequest authRequest1 = new AuthRequest("user1", "password1");
        AuthRequest authRequest2 = new AuthRequest("user1", "password1");

        // Typically, you don't need to test this unless extending the class, but it's included for completeness.
        assertThat(authRequest1.canEqual(authRequest2)).isTrue();
    }
}

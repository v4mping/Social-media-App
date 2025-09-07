package com.example.datingappandroidstudio;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.datingappandroidstudio.Model.UserProfile;

import org.junit.Test;

/**
 * Unit tests for the UserProfile model class.
 * Verifies data handling and non-trivial logic including edge cases.
 */
public class UserProfileTest {

    /**
     * Tests that age cannot be set to a negative number.
     */
    @Test
    public void testSetNegativeAge_shouldThrowException() {
        UserProfile user = new UserProfile();
        assertThrows(IllegalArgumentException.class, () -> user.setAge(-5));
    }

    /**
     * Tests default constructor creates a valid empty profile.
     */
    @Test
    public void testDefaultConstructor_createsValidProfile() {
        UserProfile userProfile = new UserProfile();

        // Verify object is not null
        assertNotNull("UserProfile object should not be null", userProfile);

        // Verify all fields are properly initialized
        assertNotNull("Name should not be null", userProfile.getName());
        assertEquals("Default name should be empty string", "", userProfile.getName());

        assertNotNull("Gender should not be null", userProfile.getGender());
        assertEquals("Default gender should be empty string", "", userProfile.getGender());

        assertNotNull("Location should not be null", userProfile.getLocation());
        assertEquals("Default location should be empty string", "", userProfile.getLocation());

        assertEquals("Default age should be 0", 0, userProfile.getAge());

        assertNotNull("Height should not be null", userProfile.getHeight());
        assertEquals("Default height should be empty string", "", userProfile.getHeight());

        assertNotNull("Prompt1 should not be null", userProfile.getPrompt1());
        assertNotNull("Prompt2 should not be null", userProfile.getPrompt2());

        assertEquals("Default rating should be 0", 0, userProfile.getRating());

        assertNotNull("Matches list should not be null", userProfile.getMatches());
        assertTrue("Matches list should be empty", userProfile.getMatches().isEmpty());

        // Add similar assertions for likes, rejects, reviews if needed
    }

    /**
     * Tests setting and getting name properly trims whitespace.
     */
    @Test
    public void testSetName_trimsWhitespace() {
        UserProfile user = new UserProfile();
        user.setName("  Alice  ");
        assertEquals("Alice", user.getName());
    }
}


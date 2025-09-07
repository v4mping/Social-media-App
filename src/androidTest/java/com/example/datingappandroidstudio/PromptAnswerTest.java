package com.example.datingappandroidstudio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.datingappandroidstudio.Model.Prompt;

import java.util.List;

public class PromptAnswerTest {

    private Prompt prompt;
    private static final String TEST_ANSWER = "Test answer";
    private static final int VALID_PROMPT_INDEX = 1;
    private static final int INVALID_PROMPT_INDEX_HIGH = 5;
    private static final int INVALID_PROMPT_INDEX_LOW = -1;

    @Before
    public void setUp() {
        prompt = new Prompt();
    }

    /**
     * Tests default constructor initializes fields correctly
     */
    @Test
    public void testDefaultConstructor() {
        assertEquals("Default prompt index should be 0", 0, prompt.getPromptIndex());
        assertEquals("Default answer should be empty", "", prompt.getAnswer());
    }

    /**
     * Tests parameterized constructor sets values correctly
     */
    @Test
    public void testParameterizedConstructor() {
        Prompt customPrompt = new Prompt(VALID_PROMPT_INDEX, TEST_ANSWER);
        assertEquals("Prompt index should match constructor value",
                VALID_PROMPT_INDEX, customPrompt.getPromptIndex());
        assertEquals("Answer should match constructor value",
                TEST_ANSWER, customPrompt.getAnswer());
    }

    /**
     * Tests getPromptOptions returns all available options
     */
    @Test
    public void testGetPromptOptions() {
        List<String> options = Prompt.getPromptOptions();
        assertNotNull("Prompt options should not be null", options);
        assertEquals("Should return correct number of options", 2, options.size());
        assertTrue("Should contain first prompt",
                options.contains("1. Two truths and a lie"));
        assertTrue("Should contain second prompt",
                options.contains("2. Funny joke"));
    }

    /**
     * Tests getQuestionText returns correct prompt text
     */
    @Test
    public void testGetQuestionText() {
        prompt.setPrompt(0);
        assertEquals("Should return first prompt text",
                "1. Two truths and a lie", prompt.getQuestionText());

        prompt.setPrompt(1);
        assertEquals("Should return second prompt text",
                "2. Funny joke", prompt.getQuestionText());
    }

    /**
     * Tests setting and getting answer
     */
    @Test
    public void testAnswerOperations() {
        prompt.setAnswer(TEST_ANSWER);
        assertEquals("Should return set answer", TEST_ANSWER, prompt.getAnswer());

        prompt.changeAnswer("New answer");
        assertEquals("changeAnswer should update answer",
                "New answer", prompt.getAnswer());
    }

    /**
     * Tests setting valid prompt index
     */
    @Test
    public void testSetValidPromptIndex() {
        prompt.setPrompt(VALID_PROMPT_INDEX);
        assertEquals("Should set valid prompt index",
                VALID_PROMPT_INDEX, prompt.getPromptIndex());

        prompt.setPrompt(VALID_PROMPT_INDEX);
        assertEquals("setPrompt should also work",
                VALID_PROMPT_INDEX, prompt.getPromptIndex());
    }

    /**
     * Tests that invalid high prompt index is ignored
     */
    @Test
    public void testSetInvalidHighPromptIndex() {
        prompt.setPrompt(0); // Set to known valid value first
        prompt.setPrompt(INVALID_PROMPT_INDEX_HIGH);
        assertEquals("High invalid index should be ignored",
                0, prompt.getPromptIndex());
    }

    /**
     * Tests that invalid low prompt index is ignored
     */
    @Test
    public void testSetInvalidLowPromptIndex() {
        prompt.setPrompt(0); // Set to known valid value first
        prompt.setPrompt(INVALID_PROMPT_INDEX_LOW);
        assertEquals("Low invalid index should be ignored",
                0, prompt.getPromptIndex());
    }

    /**
     * Tests edge case of empty answer
     */
    @Test
    public void testEmptyAnswer() {
        prompt.setAnswer("");
        assertEquals("Should handle empty answer", "", prompt.getAnswer());
    }

    /**
     * Tests that getPromptOptions returns a new copy
     */
    @Test
    public void testGetPromptOptionsReturnsCopy() {
        List<String> options1 = Prompt.getPromptOptions();
        List<String> options2 = Prompt.getPromptOptions();
        assertNotSame("Should return new copy each time", options1, options2);
    }
}

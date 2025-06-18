package com.example.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DemoApplicationTests {

    @Test
    void sanityCheck() {
        int expected = 2;
        int actual = 1 + 1;
        assertEquals(expected, actual);
    }
}

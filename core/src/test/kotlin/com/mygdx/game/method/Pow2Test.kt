package com.mygdx.game.method

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Pow2Test {

    @Test
    fun `squares positive doubles`() {
        assertEquals(9.0, pow2.pow2(3.0))
    }

    @Test
    fun `squares negative numbers to a positive result`() {
        assertEquals(16, pow2.pow2(-4))
        assertEquals(16.0f, pow2.pow2(-4.0f))
    }

    @Test
    fun `squares zero`() {
        assertEquals(0.0, pow2.pow2(0.0))
        assertEquals(0, pow2.pow2(0))
        assertEquals(0.0f, pow2.pow2(0.0f))
    }
}

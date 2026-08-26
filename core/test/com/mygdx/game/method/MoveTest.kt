package com.mygdx.game.method

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

private const val DELTA = 1e-9
private const val DELTA_F = 1e-4f

class MoveTest {

    @Test
    fun `zero degrees moves purely along the x axis`() {
        assertEquals(5.0, move.move_cos(5.0, 0.0), DELTA)
        assertEquals(0.0, move.move_sin(5.0, 0.0), DELTA)
    }

    @Test
    fun `ninety degrees moves purely along the y axis`() {
        assertEquals(0.0, move.move_cos(5.0, 90.0), DELTA)
        assertEquals(5.0, move.move_sin(5.0, 90.0), DELTA)
    }

    @Test
    fun `combined x and y components always reconstruct the original speed`() {
        val speed = 7.0
        for (angle in 0..350 step 10) {
            val x = move.move_cos(speed, angle.toDouble())
            val y = move.move_sin(speed, angle.toDouble())
            assertEquals(speed, sqrt(x * x + y * y), DELTA * 10)
        }
    }

    @Test
    fun `radian variants match the degree variants at zero`() {
        assertEquals(move.move_cos(3.0f, 0.0f), move.move_cos2(3.0f, 0.0f), DELTA_F)
        assertEquals(move.move_sin(3.0f, 0.0f), move.move_sin2(3.0f, 0.0f), DELTA_F)
    }
}

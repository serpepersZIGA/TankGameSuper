package com.mygdx.game.MapFunction

import com.mygdx.game.main.Main

/** How wet (puddle/swamp) the ground is at a world position, 0..1 - used by fire particles to decide whether they should keep burning there. */
object WaterCheck {
    private const val EXTINGUISH_THRESHOLD = 0.5f

    @JvmStatic
    fun wetnessAt(x: Float, y: Float): Float {
        val blockSize = Main.width_block
        val cellX = (x / blockSize).toInt()
        val cellY = (y / blockSize).toInt()
        val row = Main.BlockList2D.getOrNull(cellY) ?: return 0f
        val block = row.getOrNull(cellX) ?: return 0f
        return block.wetFactor
    }

    @JvmStatic
    fun isTooWetToBurn(x: Float, y: Float): Boolean = wetnessAt(x, y) > EXTINGUISH_THRESHOLD
}

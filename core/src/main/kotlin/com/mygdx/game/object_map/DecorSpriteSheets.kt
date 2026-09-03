package com.mygdx.game.object_map

import Data.DataImage
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * Cuts individual decor sprites (a rock, a bush, a flower...) out of a
 * handful of shared sheets (Rocks.png, Plants.png, Woods.png, Flowers.png),
 * registered under their own name into the same atlas everything else
 * uses (Data.DataImage.TextureAtl) - so a decor JSON's "Image" field can
 * reference one of these exactly like it would a whole separate file.
 * One shared sheet stays one file; only the region name is new, instead of
 * a pile of tiny hand-cut PNGs for every variant.
 *
 * Coordinates are pixel rects (top-left origin) measured directly off each
 * sheet, one clean rect per variant that reads well on its own.
 */
object DecorSpriteSheets {
    private data class Cut(val name: String, val sheet: String, val x: Int, val y: Int, val w: Int, val h: Int)

    private val cuts = listOf(
        Cut("rock_a", "Rocks", 1, 27, 30, 21),
        // was one 29x61 crop - that rectangle actually held two separate
        // rocks stacked on top of each other, so it always looked like two
        // rocks spawned at once. Split into rock_b (top) and rock_e (bottom).
        Cut("rock_b", "Rocks", 98, 19, 29, 31),
        Cut("rock_e", "Rocks", 98, 49, 29, 31),
        Cut("rock_c", "Rocks", 0, 51, 31, 29),
        Cut("rock_d", "Rocks", 3, 83, 26, 29),

        Cut("plant_a", "Plants", 3, 21, 27, 26),
        Cut("plant_b", "Plants", 16, 57, 32, 22),
        Cut("plant_c", "Plants", 56, 87, 33, 33),
        Cut("plant_d", "Plants", 2, 88, 43, 32),
        Cut("plant_e", "Plants", 149, 58, 37, 24),

        // wood_c (65,7,62,18) was dropped - it was two logs side by side in
        // one crop, always spawning as a pair. wood_a/wood_b are single logs.
        Cut("wood_a", "Woods", 64, 27, 31, 18),
        Cut("wood_b", "Woods", 97, 27, 31, 18),

        Cut("flower_a", "Flowers", 4, 4, 7, 8),
        Cut("flower_b", "Flowers", 20, 4, 7, 8),
        Cut("flower_c", "Flowers", 37, 4, 6, 7),
        Cut("flower_d", "Flowers", 53, 4, 6, 7),
    )

    /** All region names this registers - ProceduralMapGenerator picks decor from here. */
    val rockNames = cuts.filter { it.sheet == "Rocks" }.map { it.name }
    val plantNames = cuts.filter { it.sheet == "Plants" }.map { it.name }
    val woodNames = cuts.filter { it.sheet == "Woods" }.map { it.name }
    val flowerNames = cuts.filter { it.sheet == "Flowers" }.map { it.name }

    fun register() {
        val atlas = DataImage.TextureAtl
        for (cut in cuts) {
            val sheet = atlas.findRegion(cut.sheet) ?: continue
            atlas.addRegion(cut.name, TextureRegion(sheet, cut.x, cut.y, cut.w, cut.h))
        }
    }
}

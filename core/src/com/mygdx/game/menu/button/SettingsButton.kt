package com.mygdx.game.menu.button

import com.mygdx.game.main.ActionGameSettings
import com.mygdx.game.main.Main

/**
 * Opens the in-game Settings screen. Lives on the main menu (ConfigMenu 0)
 * alongside Play/Maps/Exit, following the same pattern every other menu
 * button already uses.
 */
class SettingsButton(x: Int, y: Int, width: Int, height: Int, txt: String, configMenu: Byte) : Button() {

    // One screen instance is reused for the lifetime of the game instead of
    // rebuilding its Stage/Skin (and regenerating fonts) on every visit.
    private val settingsScreen = ActionGameSettings()

    init {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.txt = txt
        this.ConfigMenu = configMenu
        DataRect()
    }

    override fun render(i: Int) {
        super.render(i)
        XYDetectedButtonRect()
        ActionButton1()
        ActionButton()
        RenderButtonRect()
    }

    override fun ActionButton() {
        if (condition) {
            Main.ActionGameMain = settingsScreen
            condition = false
        }
    }
}

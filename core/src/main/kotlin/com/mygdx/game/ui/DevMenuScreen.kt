package com.mygdx.game.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.mygdx.game.main.Main

// debug toggles, not meant for regular players
object DevMenuScreen : MenuScreen() {

    private var returnTo: MenuScreen = MainMenuScreen

    fun openFrom(caller: MenuScreen) {
        returnTo = caller
        Main.ActionGameMain = this
        show()
    }

    init {
        onEscape = { goBack() }
    }

    private fun goBack() {
        Main.ActionGameMain = returnTo
        returnTo.show()
    }

    override fun buildContent(skin: GameSkin): Table {
        val table = Table()
        table.center()

        table.add(Label(Localization.tr("menu.dev.title"), skin.titleLabelStyle)).padBottom(40f).row()

        addToggleRow(table, skin, "menu.dev.godmode", { DevFlags.godMode }, { DevFlags.godMode = it })
        addToggleRow(table, skin, "menu.dev.freezedaynight", { DevFlags.freezeDayNight }, { DevFlags.freezeDayNight = it })
        addToggleRow(table, skin, "menu.dev.showfps", { DevFlags.showFps }, { DevFlags.showFps = it })
        addToggleRow(table, skin, "menu.dev.uncappedparticles", { DevFlags.uncappedParticles }, { DevFlags.uncappedParticles = it })

        val backButton = TextButton(Localization.tr("menu.dev.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                goBack()
            }
        })
        table.add(backButton).width(220f).height(64f).padTop(24f)

        return table
    }

    private fun addToggleRow(table: Table, skin: GameSkin, labelKey: String, get: () -> Boolean, set: (Boolean) -> Unit) {
        val caption = Label(Localization.tr(labelKey), skin.bodyLabelStyle)
        val button = TextButton(stateText(get()), skin.toggleButtonStyle)
        button.isChecked = get()
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                set(button.isChecked)
                button.setText(stateText(button.isChecked))
            }
        })
        table.add(caption).padRight(20f)
        table.add(button).width(120f).height(48f).padBottom(8f).row()
    }

    private fun stateText(on: Boolean) = Localization.tr(if (on) "menu.dev.on" else "menu.dev.off")
}

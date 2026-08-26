package com.mygdx.game.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.mygdx.game.main.Main

/** Options screen: sound volume and interface language, both persisted. */
object SettingsScreen : MenuScreen() {

    // wherever we were opened from - main menu or the pause menu - so Back goes there
    private var returnTo: MenuScreen = MainMenuScreen

    fun openFrom(caller: MenuScreen) {
        returnTo = caller
        Main.ActionGameMain = this
        show()
    }

    override fun buildContent(skin: GameSkin): Table {
        val table = Table()
        table.center()

        val title = Label(Localization.tr("menu.settings.title"), skin.titleLabelStyle)

        val volumeCaption = Label(Localization.tr("menu.settings.volume"), skin.bodyLabelStyle)
        val volumeValueLabel = Label(volumePercentText(GameSettings.soundVolume), skin.bodyLabelStyle)
        val volumeSlider = Slider(0f, 1f, 0.01f, false, skin.sliderStyle)
        volumeSlider.value = GameSettings.soundVolume
        volumeSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameSettings.setSoundVolume(volumeSlider.value)
                volumeValueLabel.setText(volumePercentText(volumeSlider.value))
            }
        })

        val languageCaption = Label(Localization.tr("menu.settings.language"), skin.bodyLabelStyle)
        val languageRow = Table()
        val languageGroup = ButtonGroup<TextButton>()
        languageGroup.setMinCheckCount(1)
        languageGroup.setMaxCheckCount(1)
        for (language in GameLanguage.entries) {
            val button = TextButton(language.displayName, skin.toggleButtonStyle)
            button.isChecked = language == Localization.language
            languageGroup.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) Localization.setLanguage(language)
                }
            })
            languageRow.add(button).width(140f).height(48f).pad(4f)
        }

        val backButton = TextButton(Localization.tr("menu.settings.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Main.ActionGameMain = returnTo
                returnTo.show()
            }
        })

        table.add(title).padBottom(48f).colspan(2).row()
        table.add(volumeCaption).align(Align.right).padRight(20f)
        table.add(volumeValueLabel).width(60f).row()
        table.add(volumeSlider).colspan(2).width(420f).padTop(8f).padBottom(32f).row()
        table.add(languageCaption).align(Align.right).padRight(20f)
        table.add(languageRow).row()
        table.add(backButton).colspan(2).width(220f).height(64f).padTop(40f)

        return table
    }

    private fun volumePercentText(value: Float) = "${Math.round(value * 100)}%"
}

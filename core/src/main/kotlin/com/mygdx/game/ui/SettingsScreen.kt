package com.mygdx.game.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.mygdx.game.main.Main

/**
 * Options screen, split into tabs (Audio / Display / Interface) so a single
 * screen doesn't accumulate every setting in one long list. Buttons whose
 * text length depends on the current language (mode names, language names
 * themselves) are sized to fit their own content instead of a fixed pixel
 * width, since a fixed width tuned for one language clips text in another.
 */
object SettingsScreen : MenuScreen() {

    private enum class Tab { AUDIO, DISPLAY, INTERFACE }
    private var activeTab: Tab = Tab.AUDIO

    // wherever we were opened from - main menu or the pause menu - so Back goes there
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
        val root = Table()
        root.center()

        val title = Label(Localization.tr("menu.settings.title"), skin.titleLabelStyle)
        root.add(title).padBottom(32f).row()

        val tabRow = Table()
        val tabGroup = ButtonGroup<TextButton>()
        tabGroup.setMinCheckCount(1)
        tabGroup.setMaxCheckCount(1)
        val body = Table()

        fun rebuildBody() {
            body.clearChildren()
            when (activeTab) {
                Tab.AUDIO -> buildAudioTab(body, skin)
                Tab.DISPLAY -> buildDisplayTab(body, skin)
                Tab.INTERFACE -> buildInterfaceTab(body, skin)
            }
        }

        for (tab in Tab.entries) {
            val button = TextButton(Localization.tr("menu.settings.tab.${tab.name.lowercase()}"), skin.toggleButtonStyle)
            button.isChecked = tab == activeTab
            tabGroup.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) {
                        activeTab = tab
                        rebuildBody()
                    }
                }
            })
            tabRow.add(button).height(52f).pad(4f)
        }
        root.add(tabRow).padBottom(32f).row()

        rebuildBody()
        root.add(body).width(560f).row()

        val backButton = TextButton(Localization.tr("menu.settings.back"), skin.buttonStyle)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                goBack()
            }
        })
        root.add(backButton).height(64f).padTop(40f)

        return root
    }

    private fun buildAudioTab(table: Table, skin: GameSkin) {
        val volumeCaption = Label(Localization.tr("menu.settings.volume"), skin.bodyLabelStyle)
        val volumeValueLabel = Label(percentText(GameSettings.soundVolume), skin.bodyLabelStyle)
        val volumeSlider = Slider(0f, 1f, 0.01f, false, skin.sliderStyle)
        volumeSlider.value = GameSettings.soundVolume
        volumeSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameSettings.setSoundVolume(volumeSlider.value)
                volumeValueLabel.setText(percentText(volumeSlider.value))
            }
        })

        val ambientCaption = Label(Localization.tr("menu.settings.ambientvolume"), skin.bodyLabelStyle)
        val ambientValueLabel = Label(percentText(GameSettings.proceduralVolume), skin.bodyLabelStyle)
        val ambientSlider = Slider(0f, 1f, 0.01f, false, skin.sliderStyle)
        ambientSlider.value = GameSettings.proceduralVolume
        ambientSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameSettings.setProceduralVolume(ambientSlider.value)
                ambientValueLabel.setText(percentText(ambientSlider.value))
            }
        })

        table.add(volumeCaption).align(Align.right).padRight(20f)
        table.add(volumeValueLabel).width(60f).row()
        table.add(volumeSlider).colspan(2).width(420f).padTop(8f).padBottom(32f).row()
        table.add(ambientCaption).align(Align.right).padRight(20f)
        table.add(ambientValueLabel).width(60f).row()
        table.add(ambientSlider).colspan(2).width(420f).padTop(8f)
    }

    private fun buildDisplayTab(table: Table, skin: GameSkin) {
        val windowModeCaption = Label(Localization.tr("menu.settings.windowmode"), skin.bodyLabelStyle)
        val windowModeRow = Table()
        val windowModeGroup = ButtonGroup<TextButton>()
        windowModeGroup.setMinCheckCount(1)
        windowModeGroup.setMaxCheckCount(1)
        for (mode in WindowMode.entries) {
            val label = Localization.tr("menu.settings.windowmode.${mode.name.lowercase()}")
            val button = TextButton(label, skin.toggleButtonStyle)
            button.isChecked = mode == GameSettings.windowMode
            windowModeGroup.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) GraphicsSettings.apply(mode, GameSettings.resolutionWidth, GameSettings.resolutionHeight)
                }
            })
            windowModeRow.add(button).height(52f).pad(4f)
        }

        // real resolutions of the monitor the window is actually on, not a guessed 16:9 list -
        // a monitor can offer a lot of these (every refresh rate variant), so it scrolls
        val resolutionCaption = Label(Localization.tr("menu.settings.resolution"), skin.bodyLabelStyle)
        val resolutionList = Table()
        val resolutionGroup = ButtonGroup<TextButton>()
        resolutionGroup.setMinCheckCount(1)
        resolutionGroup.setMaxCheckCount(1)
        for ((w, h) in GraphicsSettings.availableResolutions()) {
            val button = TextButton("${w}x$h", skin.toggleButtonStyle)
            button.isChecked = w == GameSettings.resolutionWidth && h == GameSettings.resolutionHeight
            resolutionGroup.add(button)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent?, actor: Actor?) {
                    if (button.isChecked) GraphicsSettings.apply(GameSettings.windowMode, w, h)
                }
            })
            resolutionList.add(button).height(52f).pad(4f).row()
        }
        val resolutionScroll = ScrollPane(resolutionList, skin.scrollPaneStyle)
        resolutionScroll.setScrollingDisabled(true, false)
        resolutionScroll.setFadeScrollBars(false)

        val fpsCaption = Label(Localization.tr("menu.settings.fpslimit"), skin.bodyLabelStyle)
        val fpsValueLabel = Label(frameLimitText(GameSettings.frameLimitMode), skin.bodyLabelStyle)
        val fpsSlider = Slider(0f, (FrameLimitMode.entries.size - 1).toFloat(), 1f, false, skin.sliderStyle)
        fpsSlider.value = GameSettings.frameLimitMode.ordinal.toFloat()
        fpsSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val mode = FrameLimitMode.fromOrdinalSafe(fpsSlider.value.toInt())
                GraphicsSettings.applyFrameLimit(mode)
                fpsValueLabel.setText(frameLimitText(mode))
            }
        })

        table.add(windowModeCaption).align(Align.right).padRight(20f)
        table.add(windowModeRow).row()
        table.add(resolutionCaption).align(Align.right).padRight(20f).padTop(20f)
        table.add(resolutionScroll).width(240f).height(200f).padTop(20f).row()
        table.add(fpsCaption).align(Align.right).padRight(20f).padTop(20f)
        table.add(fpsValueLabel).padTop(20f).row()
        table.add(fpsSlider).colspan(2).width(420f).padTop(8f)
    }

    private fun buildInterfaceTab(table: Table, skin: GameSkin) {
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
            languageRow.add(button).height(48f).pad(4f)
        }

        val showFpsCaption = Label(Localization.tr("menu.settings.showfps"), skin.bodyLabelStyle)
        val showFpsButton = TextButton(stateText(GameSettings.showFps), skin.toggleButtonStyle)
        showFpsButton.isChecked = GameSettings.showFps
        showFpsButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameSettings.setShowFps(showFpsButton.isChecked)
                DevFlags.showFps = showFpsButton.isChecked
                showFpsButton.setText(stateText(showFpsButton.isChecked))
            }
        })

        table.add(languageCaption).align(Align.right).padRight(20f)
        table.add(languageRow).row()
        table.add(showFpsCaption).align(Align.right).padRight(20f).padTop(20f)
        table.add(showFpsButton).height(48f).padTop(20f)
    }

    private fun percentText(value: Float) = "${Math.round(value * 100)}%"
    private fun stateText(on: Boolean) = Localization.tr(if (on) "menu.dev.on" else "menu.dev.off")
    private fun frameLimitText(mode: FrameLimitMode): String = when (mode) {
        FrameLimitMode.VSYNC -> Localization.tr("menu.settings.vsync")
        FrameLimitMode.UNLIMITED -> Localization.tr("menu.settings.unlimited")
        else -> "${mode.fps} FPS"
    }
}

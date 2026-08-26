package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.main.ActionGame

/**
 * A pre-game menu screen built with Scene2D (Stage/Table/Skin), plugged into
 * the existing ActionGame state machine (Main.ActionGameMain) the same way
 * the host/client/gameplay states already are - so nothing about how the
 * game boots into a screen has to change.
 *
 * Using real Scene2D actors (instead of the old hand-rolled Button class with
 * its own mouse-position math) means a widget's clickable area always matches
 * what's drawn, and layout is done with Table instead of hard-coded pixel
 * coordinates, so screens don't end up positioned off-screen on a different
 * resolution.
 */
abstract class MenuScreen : ActionGame() {

    private var stage: Stage? = null
    private var skin: GameSkin? = null

    init {
        Localization.onLanguageChanged { invalidate() }
    }

    final override fun action() {
        update()
        Gdx.gl.glClearColor(0.05f, 0.06f, 0.08f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        val activeStage = stage ?: buildStage()
        // keep the viewport synced to the actual window size every frame -
        // there's no resize() callback wired up for these screens
        activeStage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        activeStage.act(Gdx.graphics.deltaTime)
        activeStage.draw()
        NetworkStatusBanner.render()
    }

    /** Runs every frame before the stage updates. Override for e.g. key shortcuts. */
    protected open fun update() {}

    /** Called whenever this screen becomes active again, to (re)claim input focus. */
    fun show() {
        Gdx.input.setInputProcessor(stage ?: buildStage())
    }

    /** Forces a rebuild next time this screen is shown, e.g. after a language change. */
    protected fun invalidate() {
        skin?.dispose()
        skin = null
        stage = null
    }

    private fun buildStage(): Stage {
        val skin = GameSkin()
        this.skin = skin
        val newStage = Stage(ScreenViewport())
        stage = newStage
        val content = buildContent(skin)
        content.setFillParent(true)
        newStage.addActor(content)
        Gdx.input.setInputProcessor(newStage)
        return newStage
    }

    /** Builds this screen's UI. Called lazily, and again after invalidate(). */
    protected abstract fun buildContent(skin: GameSkin): Table
}

package com.dth.geneticcar.utils;

import com.dth.geneticcar.Core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class ZoomProcessor extends InputAdapter {
    private Core game;

    private Vector2 lastTouch = new Vector2();

    private final int[] zoomLevels = {10, 12, 14, 16, 20, 25, 33, 50, 66, 100, 150, 200, 300, 400, 500, 600, 700, 800, 1000};
    private int zoomLevel = 100;

    public ZoomProcessor(Core game) {
	this.game = game;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
	if (button != Buttons.RIGHT) return false;

	Vector2 p = game.screenToWorld(x, y);
	lastTouch.set(p);
	return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
	if (!Gdx.input.isButtonPressed(Buttons.RIGHT)) return false;

	Vector2 p = game.screenToWorld(x, y);
	Vector2 delta = new Vector2(p).sub(lastTouch);
	game.camera.translate(-delta.x, -delta.y, 0);
	game.camera.update();

	lastTouch.set(game.screenToWorld(x, y));
	return false;
    }

    @Override
    public boolean scrolled(int amount) {
	if (amount > 0 && zoomLevel > zoomLevels[0]) {
	    for (int i = 0; i < zoomLevels.length; i++) {
		if (zoomLevels[i] == zoomLevel) {
		    zoomLevel = zoomLevels[i - 1];
		    break;
		}
	    }
	}

	if (amount < 0 && zoomLevel < zoomLevels[zoomLevels.length - 1]) {
	    for (int i = 0; i < zoomLevels.length; i++) {
		if (zoomLevels[i] == zoomLevel) {
		    zoomLevel = zoomLevels[i + 1];
		    break;
		}
	    }
	}

	game.camera.zoom = 100f / zoomLevel;
	game.camera.update();
	return false;
    }
}

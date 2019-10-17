package com.philosfight.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.philosfight.game.ScreenClasses.MenuAsset;
import com.philosfight.game.ScreenClasses.MenuScreen;
import com.philosfight.game.game.Assets;

public class PhilosofightMain extends Game {
	@Override  public void create () {
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load game assets
		Assets.instance.init(new AssetManager());
		// Load menù assets
//		MenuAsset.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
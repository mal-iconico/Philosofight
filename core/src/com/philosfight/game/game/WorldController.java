package com.philosfight.game.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Effects.Bullet;
import com.philosfight.game.utils.CameraHelper;
import com.philosfight.game.utils.Constants;

/**
 * Classe che si occupa di gestire l'aggiornamento del mondo di gioco.
 */
public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();
	public CameraHelper cameraHelper;
	public Arena arena;
	//Posizione centrale dell'arena:
	public Vector2 centerArena;

	public WorldController() {
		init();
	}

	/**
	 * Metodo generale di inizializzazione degli oggetti del game world
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
		initArena();
		cameraHelper = new CameraHelper();

		//misura il centro dell'arena:
		centerArena = new Vector2(arena.pixmap.getWidth() / 2f, arena.pixmap.getHeight() / 2f);
		cameraHelper.setPosition(centerArena);
		cameraHelper.setZoom(2.5f);
	}

	private void initArena(){
		arena = new Arena(Constants.ARENA_00);

		//Assegnamento dei comandi
		//		GIOCATORE		|		SU			GIU'		SINISTRA		DESTRA		SHOOT
		arena.player1.setControls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D, Input.Keys.NUM_1);
		arena.player2.setControls(Input.Keys.I, Input.Keys.K, Input.Keys.J, Input.Keys.L, Input.Keys.NUM_2);
	}



	/**
	 * Metodo per l'aggiornamento degli oggetti durante l'esecuzione
	 */
	public void update(float deltaTime) {

		//Ricezione dei comandi
		handleDebugInput(deltaTime);

		//Aggiornamento del movimento dei proiettili
		for (Bullet bullet : arena.player1.loader) { bullet.update(deltaTime); }
		for (Bullet bullet : arena.player2.loader) { bullet.update(deltaTime); }

		//Controllo delle collisioni dell'arena (Muri e proiettili)
		arena.checkCollisions(deltaTime);

		//Rimozione dei proiettili
		arena.player1.loader.removeAll(arena.bulletsDump);
		arena.player2.loader.removeAll(arena.bulletsDump);

		//Aggiornamento della telecamera
		cameraHelper.update(deltaTime);
	}

	/**
	 * Metodo contenente la ricezione dei comandi
	 */
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

		//Comandi dei Player
		//Aggiornamento del movimento dei giocatori

		//Player 1
		arena.player1.movementCheck(deltaTime);
		arena.player1.update(deltaTime);
		if (Gdx.input.isKeyJustPressed(arena.player1.key_Shoot)){
			arena.player1.shootAt(arena.player2);
		}

		//Player 2
		arena.player2.movementCheck(deltaTime);
		arena.player2.update(deltaTime);
		if (Gdx.input.isKeyJustPressed(arena.player2.key_Shoot)) {
			arena.player2.shootAt(arena.player1);
		}

		/*Controlli di movimento della telecamera:
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
			cameraHelper.setPosition(0, 0);
		*/

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Input.Keys.F1))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.F2))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA))
			cameraHelper.setZoom(1);
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}


	/**
	 * Metodo per i comandi di debug
	 */
	@Override
	public boolean keyUp(int keycode) {
		//Resetta il mondo di gioco
		if (keycode == Input.Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Input.Keys.ENTER) {
			//Se la camera è puntata al centro
			if (!cameraHelper.hasTarget()) {
				//Attiva il player1
				arena.player1.setMovementEnable(true);
				//Puntala sul primo player
				cameraHelper.setTarget(arena.player1);
				Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
			} else if (cameraHelper.hasTarget() && cameraHelper.getTarget() == arena.player1) {
				//Disattiva il player1
				arena.player1.setMovementEnable(false);
				//Attiva il player2
				arena.player2.setMovementEnable(true);
				//Se è già occupata dal primo player va al successivo
				cameraHelper.setTarget(arena.player2);
			} else  if (cameraHelper.hasTarget() && cameraHelper.getTarget() == arena.player2){
				//Disattiva player2
				arena.player2.setMovementEnable(false);
				//Se è già occupata puntala dal secondo player viene ripuntata al centro
				cameraHelper.setTarget(null);
				cameraHelper.setPosition(centerArena);
			}

		}
		return false;
	}


}



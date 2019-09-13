package com.philosfight.game.game.Effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.philosfight.game.game.Assets;
import com.philosfight.game.game.objects.AbstractGameObject;


public class Bullet extends AbstractGameObject {

	//Velocità proiettili
	public static final float SPEED_BULLET = 2.5f;
	//Vita proiettile
	private float lifeTimer = 0;
	//fine vita proiettile
	private float lifeTime = 5f;
	//Target a cui è inviato
	private Vector2 targetPosition;
	//flag di rimozione
	private boolean remove = false;
	//angolo del bullet
	private float degree;


	public Bullet(Vector2 position, float degree) {
		this.position = position;
		this.degree = degree;
		dimension.set(0.1f, 0.1f);
		ObjectAssets = Assets.instance.bullet.bullet;
	}

	public Vector2 getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Vector2 targetPosition) {
		this.targetPosition = targetPosition;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}



	@Override
	protected void updateMotionX(float deltaTime) {
		//Mantiene la traiettoria su cui è posto (no attrito)
		//Traiettoria calcolata in base al target e alla posizione di spawn
		this.position.x = position.x + SPEED_BULLET * MathUtils.cos(degree) * deltaTime;
	}


	@Override
	protected void updateMotionY(float deltaTime) {
		//Mantiene la traiettoria su cui è posto (no attrito)
		//Traiettoria calcolata in base al target e alla posizione di spawn
		this.position.y = position.y + SPEED_BULLET * MathUtils.sin(degree) * deltaTime;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		//Diminuisce la vita rimasta del proiettile
		lifeTimer += deltaTime;

		//Se è a fine vita deve essere rimosso
		if (lifeTimer > lifeTime) setRemove(true);
	}

	/**
	 * texture rappresenta la texture dell'oggetto e quindi l'immagine da prendere in considerazione
	 * x e y servono a disegnare il rettangolo a determinate coordinate
	 * originX ed originY servono a dichiarare l'origine dell'oggetto, (0,0) implica l'origine nell'angolo in basso a sinistra.
	 * width e height definiscono la dimensione dell'imagine da visualizzare
	 * scaleX e scaleY definiscono la scala del rettangolo intorno all'origine
	 * rotation definisce di quanti gradi ruotare l'immagine
	 * srcX e scrY servono a "tagliare" un rettangolo, dalla texture o dal texture atlas
	 * srcHeight e srcWidth
	 * flipX e flipY specchiano l'immagine sui relativi assi.
	 * */
	@Override
	public void render(SpriteBatch batch) {

		batch.draw(ObjectAssets.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, ObjectAssets.getRegionX(), ObjectAssets.getRegionY(), ObjectAssets.getRegionWidth(), ObjectAssets.getRegionHeight(), flipX, flipY);
	}

}

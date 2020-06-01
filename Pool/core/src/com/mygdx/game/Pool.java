package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Pool extends ApplicationAdapter {
	ShapeRenderer sr;
	
	Ball[] balls;
	Circle[] holes; //for balls to go in
	
	float[] position;
	boolean isMoving = false, lostBall = false;
	int pull = 0;
	
	@Override
	public void create () {
		sr = new ShapeRenderer();
		
		balls = createBalls(4);
		createTable();
	}
	
	public Ball[] createBalls(int count) {
		Ball[] result = new Ball[(count * (count + 1) / 2) + 1];
		
		int x = 1000, y = 540, index = 1;
		float xVal, yVal, radius = 30;
		double increment = (Math.PI / 3);
		result[0] = new Ball(100, y, radius);
	
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < i + 1; j++) {
				xVal = (float) (x + (2 * i * radius * Math.cos((double)Math.PI / 6)));
				yVal = (float) (y + (2 * i * radius * Math.sin(((double)Math.PI / 6) - j * increment)));
				result[index] = new Ball(xVal, yVal, radius);
				index += 1;
			}
			increment = Math.PI / (3 * (i + 1));
		}
		return result;
	}
	
	public void createTable() {
		float radius = 50;
		int t = 50; //t for thickness
		float[][] centres = new float[][] {{t, t}, {t, 1080 - t}, {960, 1080 - t}, {1920 - t, 1080 - t},  {1920 - t, t}, {960, t}};
		
		holes = new Circle[6];
		
		for (int i = 0; i < 6; i++) {
			holes[i] = new Circle(centres[i][0], centres[i][1], radius);
		}
	}
	
	public Ball[] removeBall(Ball[] balls, int index) {
		int length = balls.length;
		Ball[] result = new Ball[length - 1];
		
		for (int i = 0; i < length; i++) {
			if (i > index) {
				result[i - 1] = balls[i];
			}
			else if (i != index) {
				result[i] = balls[i];
			}
		}
		return result.clone();
	}
	
	
	
	
	public float[] mousePosition() {
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		return new float[] {x, 1080 - y};
	}
	
	public void input() {
		float[] mousePos = mousePosition();
		float[] ballPos = balls[0].getPosition();
		Vec2D direction = new Vec2D(mousePos[0] - ballPos[0], mousePos[1] - ballPos[1]);
		direction = direction.scalarMultiply((double)1 / direction.modulus());
		
		if (isMoving == false) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				if (pull < 100) {
					pull += 1;
				}
			}
			else {
				balls[0].setVelocity(direction.scalarMultiply(pull / 2));
				pull = 0;
			}
		}
	}
	
	

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		for (int i = 0; i < balls.length; i++) {
			for (int x = i + 1; x < balls.length; x++) {
				balls[i].collisionDetection(balls[x], false);
			}
			for (int x = 0; x < holes.length; x++) {
				if (balls[i].collisionDetection(holes[x], true) == true) {
					if (i != 0) {
						balls = removeBall(balls.clone(), i);
						i -= 1;
					}
					else {
						lostBall = true;
						balls[0].setPosition(new float[] {1000000, 1000000});
						balls[0].setVelocity(new Vec2D(0, 0));
					}
				}
			}
		}
		
		input();
		
		sr.begin(ShapeType.Filled);
		
		sr.setColor(0, 0, 0, 1);
		for (int i = 0; i < holes.length; i++) {
			position = holes[i].getPosition();
			sr.circle(position[0], position[1], holes[i].getRadius());
		}
		
		sr.setColor(1, 1, 1, 1);
		isMoving = false;
		for (int i = 0; i < balls.length; i++) {
			balls[i].update();
			position = balls[i].getPosition();
			sr.circle(position[0], position[1], balls[i].getRadius());
			
			if (balls[i].getVelocity().modulus() != 0) {
				isMoving = true;
			}
			sr.setColor(1, 0, 0, 1);
		}
		
		if (lostBall == true && isMoving == false) {
			balls[0].setPosition(new float[] {100, 540});
			lostBall = false;
		}
		
		sr.end();
		
		
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}
}

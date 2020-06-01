package com.mygdx.game;

public class Circle {
	protected float[] position;
	protected float radius;
	
	public Circle(float x, float y, float radius) {
		this.position = new float[] {x, y};
		this.radius = radius;
	}
	
	
	
	
	public float[] getPosition() {
		return position.clone();
	}
	
	public void setPosition(float[] value) {
		position = value.clone();
	}
	
	public float getRadius() {
		return radius;
	}
}

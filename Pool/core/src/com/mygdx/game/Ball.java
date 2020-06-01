package com.mygdx.game;

public class Ball extends Circle {
	protected Vec2D velocity;
	
	public Ball(float x, float y, float radius) {
		super(x, y, radius);
		
		this.velocity = new Vec2D(0, 0);
	}
	
	
	public void update() {
		Vec2D newVel = this.getVelocity();
		float radius = this.getRadius();
		float[] position = this.getPosition();
		position[0] += newVel.getI();
		position[1] += newVel.getJ();
		if (position[0] + radius >= 1920 || position[0] - radius <= 0) {
			position[0] -= 2 * newVel.getI();
			newVel.setI(-newVel.getI());
		}
		if (position[1] + radius >= 1080 || position[1] - radius <= 0) {
			position[1] -= 2 * newVel.getJ();
			newVel.setJ(-newVel.getJ());
		}
		this.setPosition(position);
		
		double velMod = newVel.modulus();
		if (velMod > 0.05) {
			this.setVelocity(newVel.scalarMultiply(0.99)); //friction approximation
		}
		else { //if the ball is moving slow enough then it has "stopped"
			this.setVelocity(new Vec2D(0, 0));
		}
	}
	
	public boolean collisionDetection(Circle otherBall, boolean isHole) { //TODO - MAYBE CLEAN UP THE CODE A BIT!!!, the boolean shows whether the ball should be removed from the table
		float[] pos1 = this.getPosition();
		float radius1 = this.getRadius();
		float[] pos2 = otherBall.getPosition();
		Vec2D displacement = new Vec2D(pos2[0] - pos1[0], pos2[1] - pos1[1]);
		
		if (isHole == false) {
			float radius2 = otherBall.getRadius();
			float difference = (float) (radius1 + radius2 - displacement.modulus());
			if (difference > 0) { //if true then colliding
				Vec2D normal = displacement.scalarMultiply(1 / displacement.modulus());
				Vec2D thisVel = this.getVelocity();
				Vec2D otherVel = ((Ball) otherBall).getVelocity();
				
				//using and applying impulse equation
				double impulse = thisVel.vecSub(otherVel).dotProduct(normal);
				
				this.setVelocity(thisVel.vecSub(normal.scalarMultiply(impulse)));
				((Ball) otherBall).setVelocity(otherVel.vecAdd(normal.scalarMultiply(impulse)));
				
				//The following is to move balls out of each other so that they are no longer colliding
				Vec2D translation = normal.scalarMultiply(difference / 2);
				this.setPosition(new float[] {(float) (pos1[0] - translation.getI()), (float) (pos1[1] - translation.getJ())});
				otherBall.setPosition(new float[] {(float) (pos2[0] + translation.getI()), (float) (pos2[1] + translation.getJ())});
			}
		}
		else {
			float difference = (float) (radius1 - displacement.modulus());
			if (difference > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	public Vec2D getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vec2D value) {
		velocity = value;
	}
}

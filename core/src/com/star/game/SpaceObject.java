package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

/**
 * Created by FlameXander on 11.07.2017.
 */

public abstract class SpaceObject {
    TextureAtlas.AtlasRegion texture;
    Vector2 position;
    Vector2 velocity;
    float angle;

    int hp;
    int hpMax;
    int colDamage;
    int life;
    boolean destroyed;

    float rotationSpeed;
    float radius;

    Circle hitArea;
    boolean screenSlider;

    public SpaceObject(TextureAtlas.AtlasRegion textureRegion, Vector2 position, Vector2 velocity, float rotationSpeed, int hpMax, int radius) {
        this.texture = textureRegion;
        this.position = position;
        this.velocity = velocity;
        this.rotationSpeed = rotationSpeed;
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.colDamage = hpMax/4;
        this.life = 0;
        this.destroyed = false;
        this.radius = radius;
        this.hitArea = new Circle(position.x, position.y, radius);
        this.screenSlider = true;
    }

    public abstract void render(SpriteBatch batch);

    public void update(float dt) {
        if (angle < -PI) angle += 2 * PI;
        if (angle > PI) angle -= 2 * PI;

        if (screenSlider) {
            if (position.y > Gdx.graphics.getHeight() + radius) position.y = -radius;
            if (position.y < -radius) position.y = Gdx.graphics.getHeight() + radius;
            if (position.x > Gdx.graphics.getWidth() + radius) position.x = -radius;
            if (position.x < -radius) position.x = Gdx.graphics.getWidth() + radius;
        }

        hitArea.x = position.x;
        hitArea.y = position.y;
    }

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            return true;
        }

        return false;
        if ((hp-=dmg) <0){
            life-=1;
            if (life>=0) hp=hpMax;
            if (life<0) {
                destroyed = true;
            }
    }
    public void decreaseHp (int damage){
        if ((hp-=damage) <0){
            life-=1;
            if (life>=0) hp=hpMax;
            if (life<0) {
                destroyed = true;
            }
        }
    }
}

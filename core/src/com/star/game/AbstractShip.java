package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

/**
 * Created by FlameXander on 11.07.2017.
 */

public abstract class AbstractShip extends SpaceObject {
    float lowEnginePower;
    float currentEnginePower;
    float maxEnginePower;

    float fireRate;
    float fireCounter;


    public AbstractShip(TextureAtlas.AtlasRegion texture, Vector2 position, Vector2 velocity, float rotationSpeed, int hpMax, int radius, float lowEnginePower, float maxEnginePower, float fireRate) {
        super(texture, position, velocity, rotationSpeed, hpMax, radius);
        this.maxEnginePower = maxEnginePower;
        this.lowEnginePower = lowEnginePower;
        this.fireCounter = 0;
        this.fireRate = fireRate;
        this.currentEnginePower = 0;

    }

    public void update(float dt) {
        super.update(dt);
        position.mulAdd(velocity, dt);
        velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
        velocity.scl(0.97f);
    }

    public void tryToFire(float dt) {
        fireCounter += dt;
        if (fireCounter > fireRate) {
            fireCounter = 0;
            fire();
        }
    }

    public void fire() {
        BulletEmitter.getInstance().setupBullet(position.x + (float) Math.cos(angle) * 24, position.y + (float) Math.sin(angle) * 24, angle);
    }


}

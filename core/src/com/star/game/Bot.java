package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

/**
 * Created by FlameXander on 11.07.2017.
 */

public class Bot extends AbstractShip {
    Hero target;
    float time;
    Vector2 out;

    public Bot(TextureAtlas.AtlasRegion texture) {
        super(texture, new Vector2(0, 0), new Vector2(0, 0), 3.14f, 100, 32, 100, 200, 0.750f);
        hitArea = new Circle(position.x, position.y, 25);
        this.out = new Vector2(0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        batch.draw(texture, position.x - radius, position.y - radius, radius, radius, radius * 2, radius * 2, 1, 1, (float) toDegrees(angle));
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void init(Hero target, float x, float y, int hpMax) {
        this.target = target;
        position.set(x, y);
        velocity.set(0, 0);
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.time = 0.0f;
        this.screenSlider = false;
        generateOut();
    }

    public void generateOut() {
        do {
            out.set((float) (Math.random() - 0.5) * 2000 + 640, (float) (Math.random() - 0.5) * 2000 + 360);
        } while (out.x > 0 && out.x < 1280 && out.y > 0 && out.y < 720);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        time += dt;
        float tx, ty;
        if (time < 10) {
            tx = target.position.x;
            ty = target.position.y;
            tryToFire(dt);
        } else {
            tx = out.x;
            ty = out.y;
            currentEnginePower = 1000;
            maxEnginePower = 1000;
        }

        angle = Utils.rotateTo(angle, Utils.getAngle(position.x, position.y, tx, ty), rotationSpeed, dt);
        if (position.dst(tx, ty) > 200) {
            currentEnginePower += 100 * dt;
            if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
        } else {
            currentEnginePower = 0;
        }
    }
}

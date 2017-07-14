package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.*;

/**
 * Created by FlameXander on 04.07.2017.
 */

public class Hero extends AbstractShip {
    int score;
    int money;

    public Hero() {
        super(Glob.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("ship"), new Vector2(640, 360), new Vector2(0, 0), 3.14f, 100, 32, 200, 400, 0.125f);
        hitArea = new Circle(position.x, position.y, 25);
        this.life = 2;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - radius, position.y - radius, radius, radius, radius * 2, radius * 2, 1, 1, (float) toDegrees(angle));
    }

    public void update(float dt) {
        super.update(dt);
        if (StarGame.isAndroid) {
            if (InputHandler.isJustTouched()) {
                currentEnginePower = lowEnginePower;
            }
            if (InputHandler.isTouched()) {
                float tx = InputHandler.getX();
                float ty = InputHandler.getY();

                angle = Utils.rotateTo(angle, Utils.getAngle(position.x, position.y, tx, ty), rotationSpeed, dt);
                currentEnginePower += 100 * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
                velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
            }
        }
        if (!StarGame.isAndroid) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                currentEnginePower = lowEnginePower;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                currentEnginePower += 100 * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
            } else {
                currentEnginePower = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angle += rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angle -= rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                tryToFire(dt);
            }
        }
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font, float x, float y) {
        font.draw(batch, "SCORE: " + score, x, y);
        font.draw(batch, "HP: " + hp + " / " + hpMax, x, y - 30);
        font.draw(batch, "MONEY: " + money, x, y - 60);
        font.draw(batch, "LIFE: " + life, x, y - 90);
    }

    public void fire() {
        BulletEmitter.getInstance().setupBullet(position.x + (float) Math.cos(angle) * 24, position.y + (float) Math.sin(angle) * 24, angle);
    }



}

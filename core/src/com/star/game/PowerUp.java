package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by FlameXander on 11.07.2017.
 */

public class PowerUp {
    enum Type {
        MONEY_25, MONEY_50, REPAIR_KIT
    }

    static Texture texture;

    int type;
    Vector2 position;
    float time;

    public PowerUp(float x, float y) {
        if (texture == null) {
            texture = new Texture("powerUps.png");
        }
        position = new Vector2(x, y);
        type = (int) (Math.random() * 3);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 16, position.y - 16, type * 32, 0, 32, 32);
    }

    public void update(float dt) {
        time += dt;
    }
}

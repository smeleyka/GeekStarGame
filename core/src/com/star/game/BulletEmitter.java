package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by FlameXander on 04.07.2017.
 */

public class BulletEmitter {
    private static final BulletEmitter ourInstance = new BulletEmitter();

    public static BulletEmitter getInstance() {
        return ourInstance;
    }

    final Array<Bullet> activeBullets = new Array<Bullet>();

    final Pool<Bullet> bulletsPool = new Pool<Bullet>(256, 8192) {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    TextureRegion texture;

    public void reset() {
        bulletsPool.clear();
        activeBullets.clear();
    }

    private BulletEmitter() {
        texture = Glob.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("bullet");
    }

    public void update(float dt) {
        Bullet bullet;
        int len = activeBullets.size;
        for (int i = len; --i >= 0; ) {
            bullet = activeBullets.get(i);
            bullet.update(dt);
            if(!bullet.active) {
                activeBullets.removeIndex(i);
                bulletsPool.free(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        Bullet bullet;
        int len = activeBullets.size;
        for (int i = len; --i >= 0; ) {
            bullet = activeBullets.get(i);
            batch.draw(texture, bullet.position.x - 16, bullet.position.y - 16);
        }
    }

    public void setupBullet(Vector2 position, float angle) {
        setupBullet(position.x, position.y, angle);
    }

    public void setupBullet(float x, float y, float angle) {
        Bullet item = bulletsPool.obtain();
        item.setup(x, y, 400 * (float) cos(angle), 400 * (float) sin(angle));
        activeBullets.add(item);
    }
}

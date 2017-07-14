package com.star.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by FlameXander on 04.07.2017.
 */

public class EnemiesEmitter {
    private static final EnemiesEmitter ourInstance = new EnemiesEmitter();

    public static EnemiesEmitter getInstance() {
        return ourInstance;
    }

    final Array<Asteroid> activeAsteroids = new Array<Asteroid>();
    final Array<Bot> activeBots = new Array<Bot>();

    final Pool<Asteroid> asteroidsPool = new Pool<Asteroid>() {
        @Override
        protected Asteroid newObject() {
            return new Asteroid(asteroidTexture);
        }
    };

    final Pool<Bot> botsPool = new Pool<Bot>() {
        @Override
        protected Bot newObject() {
            return new Bot(botTexture);
        }
    };

    TextureAtlas.AtlasRegion asteroidTexture;
    TextureAtlas.AtlasRegion botTexture;

    private EnemiesEmitter() {
        asteroidTexture = Glob.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("asteroid");
        botTexture = Glob.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("ship");
    }

    public void render(SpriteBatch batch) {
        Asteroid item;
        int len = activeAsteroids.size;
        for (int i = len; --i >= 0; ) {
            item = activeAsteroids.get(i);
            item.render(batch);
        }

        Bot bot;
        len = activeBots.size;
        for (int i = len; --i >= 0; ) {
            bot = activeBots.get(i);
            bot.render(batch);
        }
    }

    public void update(float dt) {
        for (Asteroid o : activeAsteroids) {
            o.update(dt);
        }
        for (Bot o : activeBots) {
            o.update(dt);
        }
        Asteroid asteroid;
        int len = activeAsteroids.size;
        for (int i = len; --i >= 0; ) {
            asteroid = activeAsteroids.get(i);
            if (asteroid.hp <= 0) {
                if (asteroid.scl > 0.21f) {
                    for (int q = 0; q < 3; q++) {
                        setupAsteroid(asteroid.position.x, asteroid.position.y, asteroid.scl - 0.2f, (int) (asteroid.hpMax * 0.8f));
                    }
                }
                activeAsteroids.removeIndex(i);
                asteroidsPool.free(asteroid);
            }
        }

        Bot bot;
        len = activeBots.size;
        for (int i = len; --i >= 0; ) {
            bot = activeBots.get(i);
            if (bot.hp <= 0) {
                activeBots.removeIndex(i);
                botsPool.free(bot);
            }
        }
    }

    public void setupAsteroid(float x, float y, float scl, int hpMax) {
        Asteroid item = asteroidsPool.obtain();
        item.init(x, y, scl, hpMax);
        activeAsteroids.add(item);
    }

    public void setupBot(Hero target, float x, float y, int hpMax) {
        Bot item = botsPool.obtain();
        item.init(target, x, y, hpMax);
        activeBots.add(item);
    }
}

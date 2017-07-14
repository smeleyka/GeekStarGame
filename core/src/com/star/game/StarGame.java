package com.star.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;

public class StarGame extends ApplicationAdapter {
    public static boolean isAndroid = false;
    BitmapFont fnt;
    SpriteBatch batch;
    Background background;
    Hero hero;
    ArrayList<PowerUp> powerUps;

    @Override
    public void create() {
        Glob.getInstance().assetManager.load("my.pack", TextureAtlas.class);
        Glob.getInstance().assetManager.finishLoading();
        fnt = new BitmapFont(Gdx.files.internal("font.fnt"));
        batch = new SpriteBatch();
        background = new Background();
        powerUps = new ArrayList<PowerUp>();
        hero = new Hero();
        for (int i = 0; i < 1; i++) {
            EnemiesEmitter.getInstance().setupAsteroid((float) Math.random() * 1280, (float) Math.random() * 720, 1.0f, 200);
        }
        for (int i = 0; i < 2; i++) {
            EnemiesEmitter.getInstance().setupBot(hero, (float) (1280 * Math.random()), (float) (720 * Math.random()), 100);
        }
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        hero.render(batch);
        EnemiesEmitter.getInstance().render(batch);
        BulletEmitter.getInstance().render(batch);
        for (PowerUp o : powerUps) {
            o.render(batch);
        }
        hero.renderHUD(batch, fnt, 20, 700);
        batch.end();
    }

    public void update(float dt) {
        background.update(hero, dt);
        hero.update(dt);
        EnemiesEmitter.getInstance().update(dt);
        BulletEmitter.getInstance().update(dt);
        Iterator<PowerUp> iter = powerUps.iterator();
        while (iter.hasNext()) {
            PowerUp p = iter.next();
            p.update(dt);
            if (hero.hitArea.contains(p.position)) {
                switch (p.type) {
                    case 0:
                        hero.money += 25;
                        break;
                    case 1:
                        hero.money += 50;
                        break;
                    case 2:
                        hero.hp = hero.hpMax;
                        break;
                }
                p.time = 10;
            }
            if (p.time > 5.0f) {
                iter.remove();
            }
        }
        checkCollision();
    }

    @Override
    public void dispose() {
        batch.dispose();
        Glob.getInstance().assetManager.clear();
    }

    final Array<SpaceObject> collisionList = new Array<SpaceObject>();

    public void checkCollision() {
        collisionList.clear();
        collisionList.add(hero);
        for (Asteroid o : EnemiesEmitter.getInstance().activeAsteroids) {
            collisionList.add(o);
        }
        for (Bot o : EnemiesEmitter.getInstance().activeBots) {
            collisionList.add(o);
        }
        Vector2 vt = new Vector2(0,0);
        for (int i = 0; i < collisionList.size; i++) {
            for (int j = i + 1; j < collisionList.size; j++) {
                SpaceObject a = collisionList.get(i);
                SpaceObject b = collisionList.get(j);
                if (a == b || (a instanceof Asteroid && b instanceof Asteroid)) continue;
                if (b.hitArea.overlaps(a.hitArea)) {
                    Vector2 acc = b.position.cpy().sub(a.position).nor();
                    Vector2 l = b.position.cpy().sub(a.position);
                    float sr = a.hitArea.radius + b.hitArea.radius;
                    float raznesti = (sr - l.len()) / 2;

                    b.position.mulAdd(acc, raznesti);
                    a.position.mulAdd(acc, -raznesti);

                    b.velocity.mulAdd(acc, 1);
                    a.velocity.mulAdd(acc, -1);

                    b.decreaseHp(a.colDamage);
                    a.decreaseHp(b.colDamage);

                    if(a instanceof Asteroid || b instanceof Asteroid){
                        System.out.println();
                    }
                }
            }
        }

        for (Bullet b : BulletEmitter.getInstance().activeBullets) {
            for (Asteroid a : EnemiesEmitter.getInstance().activeAsteroids) {
                if (a.hitArea.contains(b.position)) {
                    if (a.takeDamage(20)) {
                        hero.score += 10;
                        if (Math.random() < 0.1) {
                            powerUps.add(new PowerUp(a.position.x, a.position.y));
                        }
                    }
                    b.destroy();
                }
            }
        }
    }
}

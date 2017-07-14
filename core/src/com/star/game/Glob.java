package com.star.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by FlameXander on 07.07.2017.
 */

public class Glob {
    private static final Glob ourInstance = new Glob();

    public static Glob getInstance() {
        return ourInstance;
    }

    AssetManager assetManager;

    private Glob() {
        assetManager = new AssetManager();
    }
}

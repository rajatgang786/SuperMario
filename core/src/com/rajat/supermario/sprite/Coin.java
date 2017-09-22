package com.rajat.supermario.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rajat.supermario.SuperMario;
import com.rajat.supermario.scene.Hud;

/**
 * Created by Boss on 9/22/2017.
 */

public class Coin extends InterActiveTileObject {

    private static TiledMapTileSet tileset;
    private final int BLANK_COIN= 28;
    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        tileset = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.COIN_BIT);
    }

    @Override
    public void onheadHit() {
        Gdx.app.log("Coin","Collision");
        if(getCell().getTile().getId() == BLANK_COIN)
            SuperMario.manager.get("audio/sounds/bump.wav",Sound.class);
        else
            SuperMario.manager.get("audio/sounds/coin.wav",Sound.class);
        getCell().setTile(tileset.getTile(BLANK_COIN));
        Hud.addscore(100);

    }

}

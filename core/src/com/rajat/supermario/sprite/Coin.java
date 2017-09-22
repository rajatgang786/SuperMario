package com.rajat.supermario.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rajat.supermario.SuperMario;

/**
 * Created by Boss on 9/22/2017.
 */

public class Coin extends InterActiveTileObject {


    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.COIN_BIT);
    }

    @Override
    public void onheadHit() {

    }

}

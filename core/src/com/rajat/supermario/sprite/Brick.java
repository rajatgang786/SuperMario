package com.rajat.supermario.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.rajat.supermario.SuperMario;
import com.rajat.supermario.scene.Hud;

/**
 * Created by Boss on 9/22/2017.
 */

public class Brick extends InterActiveTileObject {
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(SuperMario.BRICK_BIT);
    }

    @Override
    public void onheadHit() {
        Gdx.app.log("Brick","Collision");
        setCategoryFilter(SuperMario.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addscore(200);
        SuperMario.manager.get("audio/sounds/breakblock.wav",Sound.class).play();
    }

}

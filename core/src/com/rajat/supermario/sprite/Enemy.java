package com.rajat.supermario.sprite;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rajat.supermario.screen.PlayScreen;

/**
 * Created by Boss on 9/23/2017.
 */

public abstract class Enemy extends Sprite {

    protected World world;
    protected Screen screen;

    public Body b2body;
    public Enemy(PlayScreen screen , float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
    }

    protected  abstract void defineEnemy();
}

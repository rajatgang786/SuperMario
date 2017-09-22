package com.rajat.supermario.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rajat.supermario.SuperMario;
import com.rajat.supermario.Tools.B2WorldCreator;
import com.rajat.supermario.Tools.WorldContactListener;
import com.rajat.supermario.scene.Hud;
import com.rajat.supermario.sprite.Mario;


/**
 * Created by Rajat on 9/21/2017.
 */

public class PlayScreen implements Screen {
   //Classes Created by Rajat
    private SuperMario game;

    private TextureAtlas atlas  ;
    private Hud hud;
    private Mario player;

    //Music Sound variable
    private Music music;

    //Tiled Map Variable
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Viewport gamePort;
    private OrthographicCamera gamecam;

    public PlayScreen(SuperMario game) {
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        gamecam = new OrthographicCamera();

        //create a FitViewPort to maintain virtual aspect ratio despite screen
        gamePort = new FitViewport(SuperMario.V_Width/SuperMario.PPM,SuperMario.V_Height/SuperMario.PPM,gamecam);

        //create our game HUD for scores/timer/level
        hud= new Hud(game.batch);

        //Load our map and setup our renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map1.tmx");
        renderer= new OrthogonalTiledMapRenderer(map,1/SuperMario.PPM);

        //initially set our gamecm to be  centered  correctly at the start
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        //Box2d
        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world,map);
        //Initialise Mario
        player = new Mario(world,this);

        world.setContactListener(new WorldContactListener());

        music = SuperMario.manager.get("audio/music/mario_music.ogg",Music.class);
        music.setLooping(true);
        music.play();

    }

    public  TextureAtlas getAtlas(){

        return atlas;
    }
    @Override
    public void show() {

    }
    public  void handleInput(float dt){
            if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);

            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x<=2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x>=-2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
    }
    public  void update(float dt){
        //handle user input
        handleInput(dt);

        world.step(1/60f,6,2);

        //player .update
            player.update(dt);
        //attach our gamecam to our player.x
        gamecam.position.x = player.b2body.getPosition().x;
        //update our gamecam with correct coordinates after change
        gamecam.update();

        //tell our rendere to draw what our camera can see
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);
        hud.update(delta);
        //clear the game screen with black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2dDebugLines
        b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        //set our batch to draw waht our hud camera see
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
            gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}

package com.rajat.supermario.sprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rajat.supermario.SuperMario;
import com.rajat.supermario.screen.PlayScreen;

/**
 * Created by Boss on 9/22/2017.
 */

public class Mario extends Sprite {

    public enum State{FALLING,JUMPING,STANDING,RUNNING};
    public World world;
    public Body b2body;
    public State currentState;
    public State previousState;
    private Animation marioJump,marioRun;
    private TextureRegion marioStand;
    private boolean runningRight;
    private float stateTimer;

    public Mario(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
       runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i=1;i<4;i++)
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        marioRun = new Animation(0.1f,frames);
        frames.clear();

        for (int i=4;i<6;i++)
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        marioJump = new Animation(0.1f,frames);


        marioStand = new TextureRegion(getTexture(),0,0,16,16);
        defineMario();
        setBounds(0,0,16/SuperMario.PPM,16/SuperMario.PPM);
        setRegion(marioStand);
    }

        public void update(float dt){
            setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y- getHeight()/2);
            setRegion(getFrame(dt));
        }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region=marioStand;
                break;
        }
        if((b2body.getLinearVelocity().x <0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true,false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x >0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState?stateTimer+dt:0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y<0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else  if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else  return State.STANDING;

    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ SuperMario.PPM,32/SuperMario.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body= world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/SuperMario.PPM);

        fdef.filter.categoryBits=SuperMario.MARIO_BIT;
        fdef.filter.maskBits = SuperMario.DEFAULT_BIT |SuperMario.COIN_BIT | SuperMario.BRICK_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head  = new EdgeShape();
        head.set(new Vector2(-2/SuperMario.PPM,6/SuperMario.PPM),new Vector2(2/SuperMario.PPM,6/SuperMario.PPM));
        fdef.shape =head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");


    }
}

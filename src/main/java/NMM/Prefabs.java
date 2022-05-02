package NMM;

import Utils.AssetPool;
import components.*;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObj generateSprObj(Sprite sprite, float sizeX, float sizeY){
        GameObj block = Window.getScene().createGameObj("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObj generatePlayer() {
        SpriteSheet playerSpr = AssetPool.getSpritesheet("assets/images/spritesheet.png");
        GameObj player = generateSprObj(playerSpr.getSprite(0), .25f, .25f);

        AnimationState walk = new AnimationState();
        walk.title = "Walk";
        float defFrameTime = 0.2f;
        walk.addFrame(playerSpr.getSprite(0), defFrameTime);
        walk.addFrame(playerSpr.getSprite(2), defFrameTime);
        walk.addFrame(playerSpr.getSprite(3), defFrameTime);
        walk.addFrame(playerSpr.getSprite(2), defFrameTime);
        walk.setLoop(true);

        StateMachine sM = new StateMachine();
        sM.addState(walk);
        sM.setDefState(walk.title);
        player.addComponent(sM);

        return player;
    }
}

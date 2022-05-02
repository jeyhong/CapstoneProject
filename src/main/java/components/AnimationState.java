package components;

import Utils.AssetPool;

import javax.imageio.spi.ImageTranscoderSpi;
import java.util.ArrayList;
import java.util.List;

public class AnimationState  {
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defSpr = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currSpr = 0;
    public boolean doesLoop = false;

    public void refreshTex() {
        for (Frame frame: animationFrames){
            frame.spr.setTexture(AssetPool.getTexture(frame.spr.getTexture().getFilepath()));
        }
    }
    public void addFrame(Sprite spr, float frameTime){
        animationFrames.add(new Frame(spr, frameTime));
    }

    public void setLoop(boolean doesLoop){
        this.doesLoop = doesLoop;
    }

    public void update(float dt){
        if(currSpr < animationFrames.size()){
            timeTracker -= dt;
            if(timeTracker <= 0){
                if(currSpr != animationFrames.size() - 1 || doesLoop){
                    currSpr = (currSpr + 1) % animationFrames.size();
                }
                timeTracker = animationFrames.get(currSpr).frameTime;
            }
        }
    }

    public Sprite getCurrSpr(){
        if(currSpr < animationFrames.size()){
            return animationFrames.get(currSpr).spr;
        }
        return defSpr;
    }

}

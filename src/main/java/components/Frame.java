package components;

public class Frame {
    public Sprite spr;
    public float frameTime;

    public Frame(){

    }

    public Frame(Sprite spr, float time){
        this.spr = spr;
        this.frameTime = time;
    }
}

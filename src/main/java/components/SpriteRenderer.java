package components;

import NMM.Transform;
import editor.JImGui;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite spr = new Sprite();

    private transient Transform prevTrans;
    private transient boolean isDirty = true;

//    public SpriteRenderer(Vector4f color){
//        this.color = color;
//        this.spr = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite spr){
//        this.spr = spr;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }

    @Override
    public void start(){
        this.prevTrans = gameObj.transform.copy();
    }

    @Override
    public void editorUpdate(float dt) {
        if(!this.prevTrans.equals(this.gameObj.transform)){
            this.gameObj.transform.copy(this.prevTrans);
            isDirty = true;
        }
    }

    @Override
    public void update(float dt) {
        if(!this.prevTrans.equals(this.gameObj.transform)){
            this.gameObj.transform.copy(this.prevTrans);
            isDirty = true;
        }
    }
    @Override
    public void imgui(){
        if(JImGui.colorPick4("Color Picker", this.color)){
            this.isDirty = true;
        }
    }

    public void setDirty() {
        this.isDirty = true;
    }
    public Vector4f getColor()
    {
        return this.color;
    }

    public Texture getTexture(){
        return spr.getTexture();
    }

    public Vector2f[] getTexCoords(){
        return spr.getTexCoords();
    }

    public void setSprite(Sprite spr){
        this.spr = spr;
        this.isDirty = true;
    }

    public void setColor(Vector4f color){
        if(!this.color.equals(color)){
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty(){
        return this.isDirty;
    }

    public void setClean(){
        this.isDirty = false;
    }

    public void setTexture(Texture texture){
        this.spr.setTexture(texture);
    }


}


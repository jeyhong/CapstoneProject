package physics2D.components;

import org.joml.Vector2f;
import renderer.DebugDraw;

public class Box2DCollider extends Collider {
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();


    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize){
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin(){
        return this.origin;
    }

    @Override
    public void editorUpdate(float dt){
        //TODO: change so that x AND y can be changed
        setHalfSize(new Vector2f(0.25f, 0.25f));
        Vector2f center = new Vector2f(this.gameObj.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObj.transform.rotation);
    }
}

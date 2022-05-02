package components;

import NMM.GameObj;
import NMM.KeyListener;
import NMM.MouseListener;
import NMM.Window;
import Utils.Settings;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{
    GameObj heldObj = null;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    public void pickUp(GameObj go){
        if(this.heldObj != null){
           this.heldObj.destroy();
        }
        this.heldObj = go;
        this.heldObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f,0.8f,0.8f,0.5f));
        this.heldObj.addComponent(new NonPickables());
        Window.getScene().addGameObjToScene(go);
    }

    public void place(){
        GameObj go = this.heldObj.copy();
        if (go.getComponent(StateMachine.class) != null) {
            go.getComponent(StateMachine.class).refreshTex();
        }
        this.heldObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        this.heldObj.removeComponent(NonPickables.class);
        Window.getScene().addGameObjToScene(go);
    }

    @Override
    public void editorUpdate(float dt){
        debounce -= dt;
        if(heldObj != null && debounce <= 0){
            heldObj.transform.position.x = MouseListener.getWorldX();
            heldObj.transform.position.y = MouseListener.getWorldY();
            heldObj.transform.position.x = ((int)Math.floor(heldObj.transform.position.x /
                    Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2;
            heldObj.transform.position.y = ((int)Math.floor(heldObj.transform.position.y /
                    Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2;

            if(!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
                debounce = debounceTime;

            }
            if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)){
                heldObj.destroy();
                heldObj = null;
            }
        }
    }
}

package components;

import NMM.Camera;
import NMM.KeyListener;
import NMM.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class editorCam extends Component{

    private float dragDebounce = 0.032f;

    private Camera levelEditorCam;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;

    public editorCam(Camera levelEditorCam){
        this.levelEditorCam = levelEditorCam;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt){
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0){
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
            return;
        } else if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)){
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCam.position.sub(delta.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if(dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)){
            dragDebounce = 0.1f;
        }

        if(MouseListener.getScrollY() != 0.0f){
            float addVal = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCam.getZoom());
            addVal *= -Math.signum(MouseListener.getScrollY());
            levelEditorCam.addZoom(addVal);
        }

        //uses numRow key not key pad 0 as hotkey to re-center
        if(KeyListener.isKeyPressed(GLFW_KEY_0)){
            reset = true;
        }

        if(reset){
            levelEditorCam.position.lerp(new Vector2f(), lerpTime);
            levelEditorCam.setZoom(this.levelEditorCam.getZoom() +
                    (1.0f - levelEditorCam.getZoom()) * lerpTime);
            this.lerpTime += 0.1f * dt;
            if(Math.abs(levelEditorCam.position.x) <= 5.0f &&
                    Math.abs(levelEditorCam.position.y) <= 5.0f){
                this.lerpTime = 0.0f;
                levelEditorCam.position.set(0f, 0f);
                this.levelEditorCam.setZoom(1.0f);
                reset = false;
            }
        }
    }
}

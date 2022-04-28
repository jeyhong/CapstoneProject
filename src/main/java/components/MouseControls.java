package components;

import NMM.GameObj;
import NMM.MouseListener;
import NMM.Window;
import Utils.Settings;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{

    GameObj heldObj = null;

    public void pickUp(GameObj go){
        this.heldObj = go;
        Window.getScene().addGameObjToScene(go);
    }

    public void place(){
        this.heldObj = null;
    }

    @Override
    public void update(float dt){
        if(heldObj != null){
            heldObj.transform.position.x = MouseListener.getOrthoX();
            heldObj.transform.position.y = MouseListener.getOrthoY();
            heldObj.transform.position.x = (int)(heldObj.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            heldObj.transform.position.y = (int)(heldObj.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }
}

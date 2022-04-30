package editor;

import NMM.GameObj;
import NMM.MouseListener;
import components.NonPickables;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.SceneInit;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObj activeGameObj = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;

    }

    public void update(float dt, SceneInit currScene){
        debounce -= dt;

        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjID = pickingTexture.readPixel(x, y);
            GameObj pickedObj =  currScene.getGameObj(gameObjID);
            if(pickedObj != null && pickedObj.getComponent(NonPickables.class) == null){
                activeGameObj = pickedObj;
            } else if (pickedObj == null && !MouseListener.isDragging()){
                activeGameObj = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui(){
        if(activeGameObj != null){
            ImGui.begin("Properties");
            activeGameObj.imGui();
            ImGui.end();
        }
    }

    public GameObj getActiveGo(){
        return this.activeGameObj;
    }
}

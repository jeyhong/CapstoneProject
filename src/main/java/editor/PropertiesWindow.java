package editor;

import NMM.GameObj;
import NMM.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.SceneInit;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObj activeGameObj = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;

    }

    public void update(float dt, SceneInit currScene){
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjID = pickingTexture.readPixel(x, y);
            activeGameObj = currScene.getGameObj(gameObjID);
        }
    }

    public void imgui(){
        if(activeGameObj != null){
            ImGui.begin("Properties");
            activeGameObj.imGui();
            ImGui.end();
        }
    }
}

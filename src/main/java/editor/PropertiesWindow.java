package editor;

import NMM.GameObj;
import NMM.MouseListener;
import components.NonPickables;
import imgui.ImGui;
import physics2D.components.Box2DCollider;
import physics2D.components.CircleCollider;
import physics2D.components.Rigidbody2D;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObj activeGameObj = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currScene){
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

            if(ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObj.getComponent(Rigidbody2D.class) == null) {
                        activeGameObj.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameObj.getComponent(Box2DCollider.class) == null && activeGameObj.getComponent(CircleCollider.class) == null) {
                        activeGameObj.addComponent(new Box2DCollider());
                    }
                }
                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameObj.getComponent(CircleCollider.class) == null && activeGameObj.getComponent(Box2DCollider.class) == null) {
                        activeGameObj.addComponent(new CircleCollider());
                    }
                }
                ImGui.endPopup();
            }
            activeGameObj.imGui();
            ImGui.end();
        }
    }

    public GameObj getActiveGo(){
        return this.activeGameObj;
    }

    public void setActiveGameObj(GameObj go) {
        this.activeGameObj = go;
    }
}

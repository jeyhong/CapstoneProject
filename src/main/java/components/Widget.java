package components;

import NMM.*;
import editor.PropertiesWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class Widget extends Component{
    private Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private Vector4f xHoverColor = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yHoverColor = new Vector4f(0, 1, 0, 1);

    private GameObj xAxisObj;
    private GameObj yAxisObj;
    private SpriteRenderer xSpr;
    private SpriteRenderer ySpr;
    protected GameObj activeGameObj = null;

    private Vector2f xAxisOffset = new Vector2f(24f / 80f, -6f / 80f);
    private Vector2f yAxisOffset = new Vector2f(-7f /80f, 21f / 80f);

    private float widgetWidth = 16f / 80f;
    private float widgetHeight = 48f / 80f;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private boolean inUse = false;

    private PropertiesWindow propWindow;

    public Widget(Sprite arrowSprite, PropertiesWindow propWindow){
        this.xAxisObj = Prefabs.generateSprObj(arrowSprite, widgetWidth, widgetHeight);
        this.yAxisObj = Prefabs.generateSprObj(arrowSprite, widgetWidth, widgetHeight);
        this.xSpr = this.xAxisObj.getComponent(SpriteRenderer.class);
        this.ySpr = this.yAxisObj.getComponent(SpriteRenderer.class);
        this.propWindow = propWindow;

        this.xAxisObj.addComponent(new NonPickables());
        this.yAxisObj.addComponent(new NonPickables());
        Window.getScene().addGameObjToScene(xAxisObj);
        Window.getScene().addGameObjToScene(yAxisObj);
    }

    @Override
    public void start(){
        this.xAxisObj.transform.rotation = 90;
        this.yAxisObj.transform.rotation = 180;
        this.xAxisObj.transform.zIndex = 100;
        this.yAxisObj.transform.zIndex = 100;
        this.xAxisObj.setNoSerialize();
        this.yAxisObj.setNoSerialize();
    }

    @Override
    public void update(float dt){
        if(inUse){
            this.setInactive();
        }
    }

    @Override
    public void editorUpdate(float dt){
        if(!inUse) return;

        this.activeGameObj = this.propWindow.getActiveGo();
        if(this.activeGameObj != null){
            this.setActive();
            if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_D)){
                GameObj newObj = this.activeGameObj.copy();
                Window.getScene().addGameObjToScene(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                this.propWindow.setActiveGameObj(newObj);
                return;
            } else if(KeyListener.keyBeginPress(GLFW_KEY_DELETE)){
                activeGameObj.destroy();
                this.setInactive();
                this.propWindow.setActiveGameObj(null);
                return;
            }
        }else{
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
            yAxisActive = true;
            xAxisActive = false;
        } else{
            yAxisActive = false;
            xAxisActive = false;
        }

        if(this.activeGameObj != null){
            this.xAxisObj.transform.position.set(this.activeGameObj.transform.position);
            this.yAxisObj.transform.position.set(this.activeGameObj.transform.position);
            this.xAxisObj.transform.position.add(this.xAxisOffset);
            this.yAxisObj.transform.position.add(this.yAxisOffset);
        }

    }

    private void setActive(){
        this.xSpr.setColor(xAxisColor);
        this.ySpr.setColor(yAxisColor);
    }

    private void setInactive(){
        this.activeGameObj = null;
        this.xSpr.setColor(new Vector4f(0,0,0,0));
        this.ySpr.setColor(new Vector4f(0,0,0,0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = MouseListener.getWorld();;
        if(mousePos.x <= xAxisObj.transform.position.x + (widgetHeight / 2.0f) &&
                mousePos.x >= xAxisObj.transform.position.x - (widgetWidth / 2.0f) &&
                mousePos.y >= xAxisObj.transform.position.y - (widgetHeight / 2.0f) &&
                mousePos.y <= xAxisObj.transform.position.y + (widgetWidth / 2.0f)){
            xSpr.setColor(xHoverColor);
            return true;
        }

        xSpr.setColor(xAxisColor);
        return false;
    }


    private boolean checkYHoverState() {
        Vector2f mousePos = MouseListener.getWorld();
        if(mousePos.x <= yAxisObj.transform.position.x + (widgetWidth/ 2.0f) &&
                mousePos.x >= yAxisObj.transform.position.x - (widgetWidth / 2.0f) &&
                mousePos.y <= yAxisObj.transform.position.y + (widgetHeight / 2.0f) &&
                mousePos.y >= yAxisObj.transform.position.y -(widgetHeight / 2.0f)){
            ySpr.setColor(yHoverColor);
            return true;
        }

        ySpr.setColor(yAxisColor);
        return false;
    }

    public void setUsing(){
        this.inUse = true;
    }

    public void setNotUsing(){
        this.inUse = false;
        this.setInactive();
    }
}

package components;

import NMM.GameObj;
import NMM.MouseListener;
import NMM.Prefabs;
import NMM.Window;
import editor.PropertiesWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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

    private Vector2f xAxisOffset = new Vector2f(57, -3);
    private Vector2f yAxisOffset = new Vector2f(13, 55);

    private int widgetWidth = 16;
    private int widgetHeight = 48;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private boolean inUse = false;

    private PropertiesWindow propWindow;

    public Widget(Sprite arrowSprite, PropertiesWindow propWindow){
        this.xAxisObj = Prefabs.generateSprObj(arrowSprite, 16, 48);
        this.yAxisObj = Prefabs.generateSprObj(arrowSprite, 16, 48);
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
        if(!inUse) return;

        this.activeGameObj = this.propWindow.getActiveGo();
        if(this.activeGameObj != null){
            this.setActive();
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
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if(mousePos.x <= xAxisObj.transform.position.x &&
                mousePos.x >= xAxisObj.transform.position.x - widgetHeight &&
                mousePos.y >= xAxisObj.transform.position.y &&
                mousePos.y <= xAxisObj.transform.position.y + widgetWidth){
            xSpr.setColor(xHoverColor);
            return true;
        }

        xSpr.setColor(xAxisColor);
        return false;
    }


    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if(mousePos.x <= yAxisObj.transform.position.x &&
                mousePos.x >= yAxisObj.transform.position.x - widgetWidth &&
                mousePos.y <= yAxisObj.transform.position.y &&
                mousePos.y >= yAxisObj.transform.position.y - widgetHeight){
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

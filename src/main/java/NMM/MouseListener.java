package NMM;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, worldX, worldY;
    private boolean mouseButtonPressed[] = new boolean[9];
    private boolean dragging;

    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewPortSize = new Vector2f();

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
    }

    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        if(get().mouseButtonDown >0){
            get().dragging = true;
        }
        get().xPos = xpos;
        get().yPos = ypos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS) {
            get().mouseButtonDown++;

            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW_RELEASE){
            get().mouseButtonDown--;

            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().dragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().dragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
   }

   //screen according to game view point
    public static float getScreenX() {
        return getScreen().x;
    }

    public static float getScreenY() {
        return getScreen().y;
    }

    public static Vector2f getScreen(){
        float currX = getX() - get().gameViewportPos.x;
        currX = (currX / get().gameViewPortSize.x) * 1920.0f;
        float currY = getY() - get().gameViewportPos.y;
        currY = 1080.0f -((currY /get().gameViewPortSize.y) * 1080.0f);
        return new Vector2f(currX, currY);
    }

    public static float getWorldX(){
        return getWorld().x;
    }

    public static float getWorldY(){
        return getWorld().y;
    }

    public static Vector2f getWorld(){
        float currX = getX() - get().gameViewportPos.x;
        currX = (currX / get().gameViewPortSize.x) * 2.0f - 1.0f;
        float currY = getY() - get().gameViewportPos.y;
        currY = -((currY /get().gameViewPortSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(currX, currY, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f inverseView = new Matrix4f(camera.getInverseView());
        Matrix4f inverseProj = new Matrix4f(camera.getInverseProj());
        temp.mul(inverseView.mul(inverseProj));
        return new Vector2f(temp.x, temp.y);
    }

    public static void setGameViewportPos(Vector2f gameViewportPos) {
        get().gameViewportPos.set(gameViewportPos);
    }

    public static void setGameViewPortSize(Vector2f gameViewPortSize) {
        get().gameViewPortSize.set(gameViewPortSize);
    }


}




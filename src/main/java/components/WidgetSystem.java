package components;

import NMM.KeyListener;
import NMM.Window;

import java.security.Key;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class WidgetSystem extends Component{
    private SpriteSheet widgets;
    private int usingWidget = 0;

    public WidgetSystem(SpriteSheet widgetSprites){
        widgets = widgetSprites;
    }

    @Override
    public void start(){
        gameObj.addComponent(new TranslateWidget(widgets.getSprite(1),
                Window.get().getImguiLayer().getPropertiesWindow()));
        gameObj.addComponent(new ScaleWidget(widgets.getSprite(2),
                Window.getImguiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt){
        if(usingWidget == 0){
            gameObj.getComponent(TranslateWidget.class).setUsing();
            gameObj.getComponent(ScaleWidget.class).setNotUsing();
        } else if( usingWidget == 1){
            gameObj.getComponent(TranslateWidget.class).setNotUsing();
            gameObj.getComponent(ScaleWidget.class).setUsing();
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_E)){
            usingWidget = 0;
        } else if(KeyListener.isKeyPressed(GLFW_KEY_R)){
            usingWidget = 1;
        }
    }
}

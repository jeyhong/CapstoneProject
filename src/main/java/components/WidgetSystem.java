package components;

import NMM.KeyListener;
import NMM.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class WidgetSystem extends Component {
    private final Spritesheet Widgets;
    private int usingWidget = 0;

    public WidgetSystem(Spritesheet WidgetSprites) {
        Widgets = WidgetSprites;
    }

    @Override
    public void start() {
        gameObject.addComponent(new TranslateWidget(Widgets.getSprite(1),
                Window.getImguiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleWidget(Widgets.getSprite(2),
                Window.getImguiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt) {
        if (usingWidget == 0) {
            gameObject.getComponent(TranslateWidget.class).setUsing();
            gameObject.getComponent(ScaleWidget.class).setNotUsing();
        } else if (usingWidget == 1) {
            gameObject.getComponent(TranslateWidget.class).setNotUsing();
            gameObject.getComponent(ScaleWidget.class).setUsing();
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            usingWidget = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
            usingWidget = 1;
        }
    }
}

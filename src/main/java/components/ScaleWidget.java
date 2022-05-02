package components;

import NMM.MouseListener;
import editor.PropertiesWindow;

public class ScaleWidget extends Widget{
    public ScaleWidget(Sprite scaleSprite, PropertiesWindow propWindow){
        super(scaleSprite, propWindow);
    }

    @Override
    public void editorUpdate(float dt){
        if(activeGameObj != null){
            if(xAxisActive && !yAxisActive){
                activeGameObj.transform.scale.x -= MouseListener.getWorldX();
            }else if (yAxisActive){
                activeGameObj.transform.scale.y -= MouseListener.getWorldY();
            }
        }
        super.editorUpdate(dt);
    }
}

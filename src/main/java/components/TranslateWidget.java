package components;

import NMM.MouseListener;
import editor.PropertiesWindow;

public class TranslateWidget extends Widget{
       public TranslateWidget(Sprite arrowSprite, PropertiesWindow propWindow){
           super(arrowSprite, propWindow);
    }

    @Override
    public void update(float dt){
           if(activeGameObj != null){
               if(xAxisActive && !yAxisActive){
                   activeGameObj.transform.position.x -= MouseListener.getWorldDx();
               }else if (yAxisActive){
                   activeGameObj.transform.position.y -= MouseListener.getWorldY();
               }
           }
           super.update(dt);
    }
}

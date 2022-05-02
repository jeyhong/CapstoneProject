package components;

import NMM.Camera;
import NMM.Window;
import Utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

public class GridLines extends Component{

    @Override
    public void editorUpdate(float dt){
        Camera cam = Window.getScene().camera();
        Vector2f cameraPos = cam.position;
        Vector2f projSize = cam.getProjSize();

        float firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        float firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) -1) * Settings.GRID_HEIGHT;

        float vertLines = (int)(projSize.x * cam.getZoom()/ Settings.GRID_WIDTH) + 2;
        float horizLInes = (int)(projSize.y * cam.getZoom()/ Settings.GRID_HEIGHT) + 2;

        float height = (int)(projSize.y * cam.getZoom()) + Settings.GRID_HEIGHT * 2;
        float width = (int)(projSize.x * cam.getZoom()) + Settings.GRID_WIDTH * 2;
        Vector3f color = new Vector3f(0.2f, 0.0f, 0.2f);
        float maxLines = Math.max(vertLines, horizLInes);
        for(int i = 0; i < maxLines; i++){
            float x = firstX + (Settings.GRID_WIDTH * i);
            float y = firstY + (Settings.GRID_HEIGHT * i);

            if(i < vertLines){
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }
            if(i < horizLInes){
               DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }

}

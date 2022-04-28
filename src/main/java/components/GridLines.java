package components;

import NMM.Window;
import Utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

public class GridLines extends Component{

    @Override
    public void update(float dt){
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projSize = Window.getScene().camera().getProjSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) -1) * Settings.GRID_HEIGHT;

        int vertLines = (int)(projSize.x / Settings.GRID_WIDTH) + 2;
        int horizLInes = (int)(projSize.y / Settings.GRID_HEIGHT) + 2;

        int height = (int)projSize.y + Settings.GRID_HEIGHT * 2;
        int width = (int)projSize.x + Settings.GRID_WIDTH * 2;
        Vector3f color = new Vector3f(0.2f, 0.0f, 0.2f);
        int maxLines = Math.max(vertLines, horizLInes);
        for(int i = 0; i < maxLines; i++){
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if(i < vertLines){
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }
            if(i < horizLInes){
               DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }

}

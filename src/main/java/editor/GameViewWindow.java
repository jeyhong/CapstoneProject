package editor;

import NMM.MouseListener;
import NMM.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import observers.EventsSystem;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector2f;

public class GameViewWindow {

    private float leftX, rightX, topY, btmY;
    private boolean playing = false;

    public void imgui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse
        | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if(ImGui.menuItem("Play", "", playing, !playing)){
            playing = true;
            EventsSystem.notify(null, new Event(EventType.GameEngineStartPlay));
        }
        if(ImGui.menuItem("Stop", "", !playing, playing)){
            playing = false;
            EventsSystem.notify(null, new Event(EventType.GameEngineStopPlay));
        }
        ImGui.endMenuBar();

        ImGui.setCursorPos(ImGui.getCursorPosX(),ImGui.getCursorPosY());
        ImVec2 windSize = getLargestSizeForViewport();
        ImVec2 windPos = getCenteredPositionForViewport(windSize);
        ImGui.setCursorPos(windPos.x, windPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);

        leftX = topLeft.x;
        btmY = topLeft.y;
        rightX = topLeft.x + windSize.x;
        topY = topLeft.y + windSize.y;


        int texID = Window.getFramebuffer().getTexID();
        ImGui.image(texID, windSize.x, windSize.y, 0, 1, 1, 0);

        MouseListener.setGameViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseListener.setGameViewPortSize(new Vector2f(windSize.x, windSize.y));
        ImGui.end();
    }

    public boolean getWantCaptureMouse() {
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
                MouseListener.getY() >= btmY && MouseListener.getY() <= topY;
    }

    private ImVec2 getLargestSizeForViewport() {
        ImVec2 windSize = new ImVec2();
        ImGui.getContentRegionAvail(windSize);

        float aspectWidth = windSize.x;;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if(aspectHeight > windSize.y){
            aspectHeight = windSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windSize = new ImVec2();
        ImGui.getContentRegionAvail(windSize);

        float viewPortX = (windSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewPortY = (windSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewPortX + ImGui.getCursorPosX(), viewPortY + ImGui.getCursorPosX());
    }

}

package editor;

import imgui.ImGui;
import observers.EventsSystem;
import observers.events.Event;
import observers.events.EventType;

public class MenuBar {

    public void imgui(){
        ImGui.beginMenuBar();

        if(ImGui.beginMenu("File")){
            if(ImGui.menuItem("Save", "Ctrl+S")){
                EventsSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if(ImGui.menuItem("Load", "Ctrl+O")){
                EventsSystem.notify(null, new Event(EventType.LoadLevel));
            }
            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}

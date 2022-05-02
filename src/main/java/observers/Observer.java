package observers;

import NMM.GameObj;
import observers.events.Event;

public interface Observer {
    void onNotify(GameObj gameObj, Event event);
}

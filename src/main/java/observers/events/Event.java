package observers.events;

public class Event {
    public EventType eType;

    public Event(EventType eType){
        this.eType = eType;
    }

    public Event(){
        this.eType = EventType.UserEvent;
    }
}

package NMM;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class GameObjTypeAdapter implements JsonDeserializer<GameObj>{


    @Override
    public GameObj deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String name = jsonObj.get("name").getAsString();
        JsonArray components = jsonObj.getAsJsonArray("components");

        GameObj go = new GameObj(name);
        for(JsonElement e : components){
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }
        go.transform = go.getComponent(Transform.class);
        return go;
    }
}

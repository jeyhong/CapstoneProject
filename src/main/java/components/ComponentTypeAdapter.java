package components;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class ComponentTypeAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement elem = jsonObject.get("properties");

        try{
            return context.deserialize(elem, Class.forName(type));
        }catch(ClassNotFoundException e){
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
         JsonObject res = new JsonObject();
         res.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
         res.add("properties", context.serialize(src, src.getClass()));
         return res;
    }
}

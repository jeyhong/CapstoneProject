package components;

import NMM.GameObj;
import editor.JImGui;
import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    private static int ID_COUNTER = 0;
    private int UID = -1;

    public transient GameObj gameObj = null;
    private int uid;

    public void start() {
    }

    public void editorUpdate(float dt) {

    }
    public void update(float dt) {
    }

    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isTransient = Modifier.isTransient((field.getModifiers()));
                if(isTransient){
                    continue;
                }

                boolean isPrivate = Modifier.isPrivate((field.getModifiers()));
                if (isPrivate) {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {
                    int val = (int) value;
                    int[] imInt = {val};
                    field.set(this, JImGui.dragInt(name, val));
                } else if (type == float.class) {
                    float val = (float) value;
                    field.set(this, JImGui.dragFloat(name, val));
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)) {
                        field.set(this, !val);
                    }
                } else if(type == Vector2f.class){
                  Vector2f val = (Vector2f)value;
                    JImGui.drawVec2Control(name, val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec4 = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec4)) {
                        val.set(imVec4[0], imVec4[1], imVec4[2], imVec4[3]);
                    }
                }else if(type.isEnum()){
                    String[] enumVals = getEnumVals(type);
                    String enumType = ((Enum) value).name();
                    ImInt index = new ImInt(indexOf(enumType, enumVals));
                    if(ImGui.combo(field.getName(), index, enumVals, enumVals.length)){
                        field.set(this, type.getEnumConstants()[index.get()]);
                    }
                }


                if (isPrivate) {
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void generateID(){
        if(this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }

    private <T extends Enum<T>> String[] getEnumVals(Class<T> enumType) {
        String[] enumVals = new String[enumType.getEnumConstants().length];
        int i = 0;
        for(T enumIntVal : enumType.getEnumConstants()){
            enumVals[i] = enumIntVal.name();
            i++;
        }
        return enumVals;
    }

    private int indexOf(String str, String[] arr){
        for(int i = 0; i < arr.length; i++){
            if(str.equals(arr[i])){
                return i;
            }
        }
        return -1;
    }

    public void destroy(){

    }

    public int getUID(){
        return this.uid;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

}

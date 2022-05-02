package NMM;

import Utils.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentTypeAdapter;
import components.SpriteRenderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class GameObj {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean dead = false;

    public GameObj(String name){
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try{
                    return componentClass.cast(c);
                } catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error: Casting component ";
                }
            }
        }
        return null;
    }

    public <T extends  Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(components.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        c.generateID();
        this.components.add(c);
        c.gameObj = this;
    }
    public void editorUpdate(float dt) {
        for(int i = 0; i < components.size(); i++){
            components.get(i).editorUpdate(dt);
        }
    }
    public void update(float dt){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }

    public void start(){
        for(int i =  0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void imGui(){
        for(Component c: components){
            if(ImGui.collapsingHeader(c.getClass().getSimpleName())){
                c.imgui();
            }
        }
    }

    public void destroy() {
        this.dead = true;
        for(int i = 0; i < components.size(); i++){
            components.get(i).destroy();
        }
    }

    public boolean isDead(){
        return this.dead;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUID(){
        return this.uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void generateUID() {
        this.uid = ID_COUNTER++;
    }
    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization(){
        return this.doSerialization;
    }


    public GameObj copy() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentTypeAdapter())
                .registerTypeAdapter(GameObj.class, new GameObjTypeAdapter())
                .create();
        String objAsJson = gson.toJson(this);
        GameObj go = gson.fromJson(objAsJson, GameObj.class);
        go.generateUID();
        for(Component c : go.getAllComponents()){
            c.generateID();
        }

        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null && spr.getTexture() != null){
            spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
        }
        return go;
    }


}

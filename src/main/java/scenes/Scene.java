package scenes;

import NMM.Camera;
import NMM.Transform;
import components.ComponentTypeAdapter;
import NMM.GameObj;
import NMM.GameObjTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import org.joml.Vector2f;
import physics2D.Physics2D;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObj> gameObjs;
    private SceneInit sceneInit;
    private Physics2D physics2D;

    public Scene(SceneInit sceneInit){
        this.sceneInit = sceneInit;
        this.physics2D = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjs = new ArrayList<>();
    }

    public void init(){
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInit.loadResources(this);
        this.sceneInit.init(this);
    }

    public void start(){
        for(int i = 0; i < gameObjs.size(); i++){
            GameObj go = gameObjs.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    public void addGameObjToScene(GameObj go){
        if(!isRunning){
            gameObjs.add(go);
        }else{
            gameObjs.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
    }

    public void destroy(){
        for(GameObj go : gameObjs){
            go.destroy();
        }
    }

    public List<GameObj> getGameObjs(){
        return this.gameObjs;
    }

    public GameObj getGameObj(int gameObjID){
        Optional<GameObj> res = this.gameObjs.stream()
                .filter(gameObj -> gameObj.getUID() == gameObjID)
                .findFirst();
        return res.orElse(null);
    }

    public void editorUpdate(float dt){
        this.camera.adjustProjection();

        for(int i = 0; i < gameObjs.size(); i++){
            GameObj go = gameObjs.get(i);
            go.editorUpdate(dt);

            if(go.isDead()){
                gameObjs.remove(i);
                this.renderer.destroyGameObj(go);
                this.physics2D.destroyGameObj(go);
                i--;
            }
        }
    }

    public void update(float dt){
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for(int i = 0; i < gameObjs.size(); i++){
            GameObj go = gameObjs.get(i);
            go.update(dt);

            if(go.isDead()){
                gameObjs.remove(i);
                this.renderer.destroyGameObj(go);
                this.physics2D.destroyGameObj(go);
                i--;
            }
        }
    }

    public void render(){
        this.renderer.render();
    }
    public Camera camera(){
        return this.camera;
    }

    public void imgui(){
        this.sceneInit.imgui();
    }

    public GameObj createGameObj(String name){
        GameObj go = new GameObj(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentTypeAdapter())
                .registerTypeAdapter(GameObj.class, new GameObjTypeAdapter())
                .create();
        try {
            FileWriter wr = new FileWriter("level.txt");
            List<GameObj> goToSerialize = new ArrayList<>();
            for(GameObj go : this.gameObjs){
                if(go.doSerialization()){
                    goToSerialize.add(go);
                }
            }
            wr.write(gson.toJson(goToSerialize));
            wr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentTypeAdapter())
                .registerTypeAdapter(GameObj.class, new GameObjTypeAdapter())
                .create();

        String inFile = "";
        try{
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch(IOException e){
            e.printStackTrace();
        }

        if(!inFile.equals("")){
            int maxGoId = -1;
            int maxCompId = -1;
            GameObj[] objs = gson.fromJson(inFile, GameObj[].class);
            for(int i = 0; i< objs.length; i++){
                addGameObjToScene(objs[i]);

                for(Component c: objs[i].getAllComponents()){
                    if(c.getUID() > maxCompId){
                        maxCompId = c.getUID();
                    }
                }

                if(objs[i].getUID() > maxGoId){
                    maxGoId = objs[i].getUID();
                }
            }

            maxGoId++;
            maxCompId++;

            GameObj.init(maxGoId);
            Component.init(maxCompId);
        }
    }
}

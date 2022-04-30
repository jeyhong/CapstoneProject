package scenes;

import NMM.Camera;
import components.ComponentTypeAdapter;
import NMM.GameObj;
import NMM.GameObjTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import imgui.ImGui;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SceneInit {

    protected Renderer renderer =  new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObj> gameObjs = new ArrayList<>();
    protected boolean levelLoaded = false;

    public SceneInit(){

    }

    public void init(){

    }

    public void start(){
        for(GameObj go: gameObjs){
            go.start();
            this.renderer.add(go);
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
        }
    }

    public GameObj getGameObj(int gameObjID){
        Optional<GameObj> res = this.gameObjs.stream()
                .filter(gameObj -> gameObj.getUID() == gameObjID)
                .findFirst();
        return res.orElse(null);
    }

    public abstract void update(float dt);
    public abstract void render();
    public Camera camera(){
        return this.camera;
    }

    public void imgui(){

    }
    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentTypeAdapter())
                .registerTypeAdapter(GameObj.class, new GameObjTypeAdapter())
                .create();
        try {
            FileWriter wr = new FileWriter("level.txt");
            wr.write(gson.toJson(this.gameObjs));
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
            this.levelLoaded = true;
        }
    }
}

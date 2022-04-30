package scenes;

import NMM.Camera;
import NMM.GameObj;
import NMM.Prefabs;
import NMM.Transform;
import Utils.AssetPool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.source.tree.AssertTree;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;

public class LevelEditorScene extends SceneInit {
    private GameObj obj1;
    private SpriteSheet sprites;
    SpriteRenderer obj1SprRenderer;
    GameObj levelEditorTools = new GameObj("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f(-250, 0));
        levelEditorTools.addComponent(new MouseControls());
        levelEditorTools.addComponent(new GridLines());
        levelEditorTools.addComponent(new editorCam(this.camera));

        loadResources();
        sprites = AssetPool.getSpritesheet("assets/images/decorationsAndBlocks.png");
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                        16, 16, 76, 0));

        for(GameObj g: gameObjs){
            if(g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
//        AssetPool.getTexture("assets/images/blendImage2.png");

//        AssetPool.getTexture("assets/images/blendImage1.png");
    }

    @Override
    public void update(float dt) {
        levelEditorTools.update(dt);
        this.camera.adjustProjection();

        for(GameObj go : this.gameObjs){
            go.update(dt);
        }

    }

    @Override
    public void render(){
        this.renderer.render();
    }
    @Override
    public void imgui(){
        ImGui.begin("Level Editor");

        ImVec2 windowsPos = new ImVec2();
        ImGui.getWindowPos(windowsPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowsPos.x + windowSize.x;
        for(int i = 0; i< sprites.size(); i++){
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObj obj = Prefabs.generateSprObj(sprite, 32, 32);
                levelEditorTools.getComponent(MouseControls.class).pickUp(obj);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < sprites.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}

package scenes;

import NMM.*;
import Utils.AssetPool;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class LevelEditorSceneInit extends SceneInit {

    private SpriteSheet sprites;
    private GameObj levelEditorTools;

    public LevelEditorSceneInit(){

    }

    @Override
    public void init(Scene scene){
        sprites = AssetPool.getSpritesheet("assets/images/decorationsAndBlocks.png");
        SpriteSheet widget = AssetPool.getSpritesheet("assets/images/widgets.png");

        levelEditorTools = scene.createGameObj("LevelEditor");
        levelEditorTools.setNoSerialize();
        levelEditorTools.addComponent(new MouseControls());
        levelEditorTools.addComponent(new GridLines());
        levelEditorTools.addComponent(new editorCam(scene.camera()));
        levelEditorTools.addComponent(new WidgetSystem(widget));
        scene.addGameObjToScene(levelEditorTools);
    }

    @Override
    public void loadResources(Scene scene){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));

        AssetPool.addSpriteSheet("assets/images/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                        16, 16, 76, 0));

        AssetPool.addSpriteSheet("assets/images/widgets.png", new SpriteSheet(AssetPool.getTexture("assets/images/widgets.png"),
                24, 48, 3, 0));
        for(GameObj g: scene.getGameObjs()){
            if(g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
            if(g.getComponent(StateMachine.class) != null){
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTex();
            }
        }
    }

    @Override
    public void imgui(){
        //this chunk can be removed; for debugging
        ImGui.begin("Level Editor Tools");
        levelEditorTools.imGui();
        ImGui.end();

        ImGui.begin("Game Entities");

        if(ImGui.beginTabBar("Window Tab Bar")) {
            if(ImGui.beginTabItem("Blocks")){
                ImVec2 windowsPos = new ImVec2();
                ImGui.getWindowPos(windowsPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowsPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); i++) {
                    //TODO: Fix this to reflect our sprites properly?
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 4;
                    float spriteHeight = sprite.getHeight() * 4;
                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObj obj = Prefabs.generateSprObj(sprite, 0.25f, 0.25f);
                        levelEditorTools.getComponent(MouseControls.class).pickUp(obj);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Prefabs")){
                SpriteSheet playerSpr = AssetPool.getSpritesheet("assets/images/spritesheet.png");
                Sprite spr = playerSpr.getSprite(0);
                float spriteWidth = spr.getWidth() * 4;
                float spriteHeight = spr.getHeight() * 4;
                int id = spr.getTexId();
                Vector2f[] texCoords = spr.getTexCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObj obj = Prefabs.generatePlayer();
                    levelEditorTools.getComponent(MouseControls.class).pickUp(obj);
                }
                ImGui.sameLine();

                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}

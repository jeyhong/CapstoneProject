package renderer;

import NMM.GameObj;
import components.SpriteRenderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currShader;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObj go){
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr != null){
            add(spr);
        }
    }

    private void add(SpriteRenderer spr){
        boolean added = false;
        for(RenderBatch batch : batches){
            if(batch.hasRoom() && batch.zIndex() == spr.gameObj.transform.zIndex){
                Texture tex = spr.getTexture();
                if(tex != null && (batch.hasTexture(tex) || batch.hasTexRoom())) {
                    batch.addSprite(spr);
                    added = true;
                    break;
                }
            }
        }

        if(!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spr.gameObj.transform.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
            Collections.sort(batches);
        }
    }

    public static void bindShader(Shader shader){
        currShader = shader;
    }

    public static Shader getBoundShader(){
        return currShader;
    }

    public void render(){
        currShader.use();
        for (int i = 0; i < batches.size(); i++) {
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }
}

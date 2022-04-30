package renderer;

import NMM.Window;
import Utils.AssetPool;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch implements Comparable<RenderBatch>{
    //Vertex
    //======
    //Pos               Color                         texCoords        texID
    //float, float,     float, float, float, float    float, float,    float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENT_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENT_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] verticies;
    private int[] texSlots = { 0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex){
        this.zIndex = zIndex;
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        verticies = new float[maxBatchSize * 4 * VERTEX_SIZE];
        this.numSprites = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
    }

    public void start(){
        //Generate & bind vertex array obj
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for verticies
        vboID =  glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticies.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create & upload indices buffer
        int eboID = glGenBuffers();
        int [] indices = generateIndicies();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //Enable buffer attrib pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENT_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENT_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer spr){
        //Get index & add renderObj
        int index = this.numSprites;
        this.sprites[index] =  spr;
        this.numSprites++;

        if(spr.getTexture() != null){
            if(!textures.contains(spr.getTexture())){
                textures.add(spr.getTexture());
            }
        }

        //Add properties to vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    public void render(){
        boolean rebuffer = false;
        for(int i = 0 ; i < numSprites; i++){
            SpriteRenderer spr = sprites[i];
            if(spr.isDirty()){
                loadVertexProperties(i);
                spr.setClean();
                rebuffer = true;
            }
        }
        if(rebuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, verticies);
        }

        //Use shader
        Shader shader = Renderer.getBoundShader();
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        for(int i = 0; i < textures.size(); i ++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        for(int i = 0; i < textures.size(); i ++){
            textures.get(i).unbind();
        }
        shader.detach();
    }

    private void loadVertexProperties(int index){
        SpriteRenderer spr =  this.sprites[index];

        //Find offset in array
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = spr.getColor();
        Vector2f[] texCoords = spr.getTexCoords();

        int texID = 0;
        if(spr.getTexture() != null){
            for(int i = 0; i < textures.size(); i ++){
                if(textures.get(i).equals(spr.getTexture())){
                    texID = i + 1;
                    break;
                }
            }
        }


        //Add vertices with appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0 ; i < 4; i++){
            if(i == 1){
                yAdd = 0.0f;
            }else if (i == 2){
                xAdd = 0.0f;
            }else if (i == 3){
                yAdd = 1.0f;
            }   //load position
            verticies[offset] = spr.gameObj.transform.position.x + (xAdd * spr.gameObj.transform.scale.x);
            verticies[offset + 1] = spr.gameObj.transform.position.y + (yAdd * spr.gameObj.transform.scale.y);

            //Load color
            verticies[offset + 2] = color.x;
            verticies[offset + 3] = color.y;
            verticies[offset + 4] = color.z;
            verticies[offset + 5] = color.w;

            //Load texture coords
            verticies[offset + 6] = texCoords[i].x;
            verticies[offset + 7] = texCoords[i].y;

            //load texture id
            verticies[offset + 8] = texID;

            //load ent id
            verticies[offset + 9] = spr.gameObj.getUID() + 1;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndicies(){
        //6 indicies per quad (3 per triangle)
        int[] elements =  new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void  loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //3,2,0,0,2,1       7,6,4,4,6,5
        //Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        //Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public boolean hasTexRoom(){
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture tex){
        return this.textures.contains(tex);
    }

    public int zIndex(){
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}

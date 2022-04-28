package Utils;

import components.SpriteSheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resrcName){
        File file = new File(resrcName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        }else{
            Shader shader = new Shader(resrcName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader); //holds reference rather than file
            return shader;
        }
    }

    public static Texture getTexture(String resrcName){
        File file = new File(resrcName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }else{
            Texture texture = new Texture();
            texture.init(resrcName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resrcName, SpriteSheet spriteSheet){
        File file = new File(resrcName);
        if(!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
            AssetPool.spritesheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpritesheet(String resrcName){
        File file = new File(resrcName);
        if(!AssetPool.spritesheets.containsKey(file.getAbsolutePath())){
            assert false: "Error: could not access spritesheet '" + resrcName + "' and could not add to asset pool";
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}

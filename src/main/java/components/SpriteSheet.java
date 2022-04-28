package components;

import org.joml.Vector2f;
import renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int padding){
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currX = 0;
        int currY = texture.getHeight() - spriteHeight;
        for(int i = 0; i < numSprites; i++){
            float topY = (currY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currX + spriteWidth)/ (float)texture.getWidth();
            float leftX = currX / (float)texture.getWidth();
            float bottomY = currY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currX += spriteWidth + padding;
            if(currX >= texture.getWidth()){
                currX = 0;
                currY -= spriteHeight + padding;
            }
        }
    }

    public Sprite getSprite(int index){
        return this.sprites.get(index);
    }

    public int size() {
        return sprites.size();
    }
}

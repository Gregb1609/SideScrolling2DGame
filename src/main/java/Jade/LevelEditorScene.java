package Jade;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;


import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;


public class LevelEditorScene extends Scene{

    public LevelEditorScene(){

    }
    @Override
    public void init(){
        loadResources();

        this.camera=new Camera(new Vector2f());

        Spritesheet sprites= AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj1 = new GameObject("Object 1",new Transform(new Vector2f(100,100),new Vector2f(256,256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(4)));
        this.addGameObjectToScene(obj1);
        GameObject obj2 = new GameObject("Object 2",new Transform(new Vector2f(600,100),new Vector2f(256,256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(17)));
        this.addGameObjectToScene(obj2);


    }

    private void loadResources(){

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16,16,26,0));
    }

    @Override
    public void update(float dt){
        //System.out.println("FPS= "+1/dt);
        for(GameObject go: this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
}


package Jade;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.Spritesheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;


import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL20.GL_MAX_TEXTURE_IMAGE_UNITS;


public class LevelEditorScene extends Scene{

    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene(){

    }
    @Override
    public void init(){
        loadResources();

        this.camera=new Camera(new Vector2f());

        sprites= AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1",new Transform(new Vector2f(500,100),new Vector2f(256,256)),1);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        this.addGameObjectToScene(obj1);
        this.activeGameObject=obj1;
        GameObject obj2 = new GameObject("Object 2",new Transform(new Vector2f(600,100),new Vector2f(256,256)),2);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage2.png"))));
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
        //obj1.transform.position.x+=50*dt;

        for(GameObject go: this.gameObjects){
            go.update(dt);
        }

        this.renderer.render();
    }
    @Override
    public void imgui() {
        ImGui.begin("Test Window");
        ImGui.text("Some Random text");
        ImGui.end();
    }
}


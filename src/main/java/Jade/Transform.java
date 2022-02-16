package Jade;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        this.position=new Vector2f();
        this.scale=new Vector2f();
    }

    public Transform(Vector2f position){
        init(position,new Vector2f());
    }

    public Transform(Vector2f position,Vector2f scale){
        this.position=position;
        this.scale=scale;
    }

    public void init(Vector2f position, Vector2f scale){
        this.position=position;
        this.scale=scale;
    }
}
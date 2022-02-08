package Jade;

import Renderer.Shader;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import util.Texture;
import util.Time;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelEditorScene extends Scene{

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray ={
            //position          //colour                    //UV coordinates
            100.0f,0.5f, 0.0f,      1.0f,0.0f,0.0f,1.0f,    1,1, //bottom right  0
            0.5f,100.0f,0.0f,       0.0f,1.0f,0.0f,1.0f,    0,0, //top left      1
            100.0f,100.0f,0.0f,     0.0f,0.0f,1.0f,1.0f,    1,0, //top right     2
            0.5f,0.5f,0.0f,         1.0f,1.0f,0.0f,1.0f,    0,1 //bottom left   3
    };

    //now we use the values in the vertex array as mapping points to draw the shapes needed

    private int[] elementArray = {
            // the shapes must be detailed by declaring the vertexes in reverse order
            //we will draw a square made of two equal triangles
            /*         x=1        x=2


                       x=3        x=0
                       so tringle one is  topright to topleft to botright or 2,1,0
             */
            2,1,0,//triangle 1
            0,1,3
    };

    private int vaoID, eboID, vboID;

    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene(){

    }
    @Override
    public void init(){
        this.camera = new Camera(new Vector2f(-700,-300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture=new Texture("assets/images/testImage.png");

        vaoID=glGenVertexArrays();
        glBindVertexArray((vaoID));

        //create float buffer for vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //use the buffer array as the vbo (vertice buffer object)
        vboID=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID=glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer, GL_STATIC_DRAW);

        //and the vertex attribute "personality"
        int positionsSize=3;
        int colorSize=4;
        int uvSize=2;
        int vertexSizeBytes=(positionsSize+colorSize+uvSize)*Float.BYTES;
        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT,false,vertexSizeBytes,(positionsSize+colorSize)*Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt){
        //camera.position.x=(-1080)/2;
        //camera.position.y=(-720)/2;
        defaultShader.use();

        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}


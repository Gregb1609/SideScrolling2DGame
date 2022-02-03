package Jade;

import Renderer.Shader;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelEditorScene extends Scene{

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray ={
            //position          //colour
            0.5f,-0.5f, 0.0f,   1.0f,0.0f,0.0f,1.0f,//bottom right  0
            -0.5f,0.5f,0.0f,    0.0f,1.0f,0.0f,1.0f,//top left      1
            0.5f,0.5f,0.0f,     0.0f,0.0f,1.0f,1.0f,//top right     2
            -0.5f,-0.5f,0.0f,    1.0f,1.0f,0.0f,1.0f,//bottom left   3
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

    public LevelEditorScene(){

    }
    @Override
    public void init(){

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

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
        int floatSizeBytes=4;
        int vertexSizeBytes=(positionsSize+colorSize)*floatSizeBytes;
        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt){
        defaultShader.use();
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


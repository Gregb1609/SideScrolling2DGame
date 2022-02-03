package Jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout(location=0) in vec3 aPos;\n" +
            "layout(location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor=aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";
    private String fragmentShaderSrc="#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color=fColor;\n" +
            "}";

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

    @Override
    public void init(){
        //first load and configure the the vertex shader
        vertexID=glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source code to the GPU
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);

        //error check the build
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success==0){
            int len= glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false : "";
        }
        fragmentID=glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader source code to the GPU
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);

        //error check the build
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success==0){
            int len= glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false : "";
        }
        //link shaders and fragments in the graphic program
        shaderProgram=glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        //error check linking process
        success=glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success==0){
            int len= glGetShaderi(shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tShader linking failed.");
            System.out.println(glGetShaderInfoLog(shaderProgram,len));
            assert false: "";
        }

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

    public LevelEditorScene(){

    }

    @Override
    public void update(float dt){
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}


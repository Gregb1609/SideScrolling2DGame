package Renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath){
        this.filepath=filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String [] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            //find first #type in default.glsl
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern= source.substring(index,eol).trim();

            //find second #type in default.glsl.  Start search after end of first pattern.
            index = source.indexOf("#type",eol)+6;
            eol = source.indexOf("\r\n", index);
            String secondPattern= source.substring(index,eol).trim();

            if(firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource= splitString[1];
            }else{
                throw new IOException("Unexpected token in "+firstPattern);
            }

            if(secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSource= splitString[2];
            }else{
                throw new IOException("Unexpected token in "+secondPattern);
            }
        } catch (IOException e){
            e.printStackTrace();
            assert false: "Error Could not open the file for shader "+filepath;
        }
    }

    public void compile(){
        //compiles and links
        int vertexID,fragmentID;
        //first load and configure the vertex shader
        vertexID=glCreateShader(GL_VERTEX_SHADER);
        //pass the shader source code to the GPU
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);

        //error check the build
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success==0){
            int len= glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false : "";
        }
        fragmentID=glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader source code to the GPU
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);

        //error check the build
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success==0){
            int len= glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false : "";
        }
        //link shaders and fragments in the graphic program
        shaderProgramID=glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);

        //error check linking process
        success=glGetProgrami(shaderProgramID,GL_LINK_STATUS);
        if(success==0){
            int len= glGetShaderi(shaderProgramID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '"+filepath+"'\n\tShader linking failed.");
            System.out.println(glGetShaderInfoLog(shaderProgramID,len));
            assert false: "";
        }
    }

    public void use(){
        //bind shader program
        glUseProgram(shaderProgramID);
    }
    public void detach(){
        glUseProgram(0);
    }
    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation,false,matBuffer);
    }
}

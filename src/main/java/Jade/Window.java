package Jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width,height;
    private String title;
    private static Window window =null;
    private long glfwWindow=0;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Game Window";
    }

    public static Window get(){
        if(Window.window==null){
            Window.window=new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion()+" !");
        init();
        loop();
    }
    public void init(){
        //error callback
        GLFWErrorCallback.createPrint(System.err).set();
        //initialise GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initilise GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if (glfwWindow==NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
            }
        //Make the open GL contect current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make window visible
        glfwShowWindow(glfwWindow);
        //This line makes the LWJGL capabilities match those available with the active GLFW.It is very important.
        GL.createCapabilities();

    }
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            glClearColor(1.0f,0.0f,0.0f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }

    }
}

package Jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width,height;
    private String title;
    private static Window window =null;
    private long glfwWindow=0;
    public float r,g,b,a;
    private boolean fadeToBlack=false;

    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Game Window";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene=new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene=new LevelScene();
                break;
            default:
                assert false : "Unknown scene '"+newScene+"'";
                break;
        }
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
        //free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        //terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallBack);
        //Make the open GL contect current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make window visible
        glfwShowWindow(glfwWindow);
        //This line makes the LWJGL capabilities match those available with the active GLFW.It is very important.
        GL.createCapabilities();
        Window.changeScene(0);

    }
    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = - 1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }
            currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            endTime=Time.getTime();
            dt=endTime-beginTime;
            beginTime=endTime;
        }

    }
}

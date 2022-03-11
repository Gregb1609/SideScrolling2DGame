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
    private ImGuiLayer imGuiLayer;
    public float r,g,b,a;
    private boolean fadeToBlack=false;

    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Game Window";
        r=1f;
        g=1f;
        b=1f;
        a=1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene=new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene=new LevelScene();
                currentScene.init();
                currentScene.start();
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

    public static Scene getScene(){
        return get().currentScene;
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
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);
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
        glfwSetWindowSizeCallback(glfwWindow, (w,newWidth,newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);

        });
        //Make the open GL contect current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make window visible
        glfwShowWindow(glfwWindow);
        //This line makes the LWJGL capabilities match those available with the active GLFW.It is very important.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE,GL_ONE_MINUS_SRC_ALPHA);
        this.imGuiLayer=new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        Window.changeScene(0);

    }
    public void loop(){
        float beginTime = (float)glfwGetTime();
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
            this.imGuiLayer.update(dt);
            glfwSwapBuffers(glfwWindow);

            endTime= (float)glfwGetTime();
            dt=endTime-beginTime;
            beginTime=endTime;
        }

    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth){
        get().width=newWidth;
    }

    public static void setHeight(int newHeight){
        get().height=newHeight;
    }
}

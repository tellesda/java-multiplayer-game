package object.interactive.computer.engine3d.objectClasses;

import engine.Engine;
import object.interactive.computer.engine3d.mathClasses.*;

public class Camera extends Entity{

    //Input
    private final Engine engine;

    //Movement
    private final float moveSpeed;
    private final float rotateSpeed;
    private float yaw, pitch;

    //Rendering
    private final float verticalFov;
    private final float horizontalFov;
    private final float zFar;
    private final float zNear;
    private final Plane[] clipPlanes; //Near, right, left, up, down

    public Camera(Engine engine, Vector3d location, Rotator3d rotation, Vector3d scale){
        this.engine = engine;
        this.moveSpeed = 0.6f;
        this.rotateSpeed = 0.3f;
        this.location = location;
        this.rotation = rotation;
        this.scale = scale;
        this.horizontalFov = 90;
        this.verticalFov = horizontalToVerticalFov(horizontalFov);
        this.zFar = 1000f;
        this.zNear = 0.1f;

        clipPlanes = new Plane[5];
        clipPlanes[0] = new Plane(null, null);
        clipPlanes[1] = new Plane(null, null);
        clipPlanes[2] = new Plane(null, null);
        clipPlanes[3] = new Plane(null, null);
        clipPlanes[4] = new Plane(null, null);

        this.yaw = 0f;
        this.pitch = 0f;
    }

    public float getVerticalFov() { return verticalFov; }
    public float getHorizontalFov() { return horizontalFov; }
    public float getzFar() { return zFar; }
    public float getzNear() { return zNear; }
    public float getMoveSpeed() { return moveSpeed; }
    public float getRotateSpeed() { return rotateSpeed; }
    public Plane getPlane(int index){ return clipPlanes[index]; }
    public Plane[] getClipPlanes() { return clipPlanes; }

    public void setPlane(int index, Vector3d planeNormal, Vector3d planePoint){
        this.clipPlanes[index].setPlane(planeNormal, planePoint);
    }

    //Methods
    private float adjustSpeed(float speed){
        float scalar = 1 / (float)engine.engineTicks;
        return scalar*speed;
    }
    public void moveForward(float moveSpeed){
        Vector3d forward = new Vector3d(0f, 0f, 1f);
        addWorldLocation(Vector3d.scale(Vector3d.rotateVector(forward, Rotator3d.getRotationByYawPitch(yaw, 0f)), adjustSpeed(moveSpeed)));
    }
    public void moveRight(float moveSpeed){
        addWorldLocation(Vector3d.scale(getRightVector(), adjustSpeed(moveSpeed)));
    }
    public void moveUp(float moveSpeed){
        moveY(adjustSpeed(moveSpeed));
    }
    public void addInputYaw(float rotateSpeed){
        yaw += adjustSpeed(rotateSpeed);
    }
    public void addInputPitch(float rotateSpeed){
        if(rotateSpeed >= 0){
            if(pitch < (3.14159)/2)
                pitch += adjustSpeed(rotateSpeed*0.8f);
        }
        else{
            if(pitch > -(3.14159)/2)
                pitch += adjustSpeed(rotateSpeed*0.8f);
        }
    }

    public void calculatePlanes(){

        Vector3d forwardVector = getForwardVector();
        float deltaRot = ((verticalFov/2) + 90) * 3.14159f/180;
        float deltaRotH = ((horizontalFov/2) + 90) * 3.14159f/180;

        Vector3d leftV = new Vector3d((float)Math.cos(1.570795+deltaRotH), 0f, (float)Math.sin(1.570795+deltaRotH));
        Vector3d rightV = new Vector3d((float)Math.cos(1.570795-deltaRotH), 0f, (float)Math.sin(1.570795-deltaRotH));
        Vector3d downV = new Vector3d(0f, (float)Math.cos(1.570795+deltaRot), (float)Math.sin(1.570795+deltaRot));
        Vector3d upV = new Vector3d(0f, (float)Math.cos(1.570795-deltaRot), (float)Math.sin(1.570795-deltaRot));

        setPlane(0, forwardVector, Vector3d.add(location, Vector3d.scale(forwardVector, zNear)));

        setPlane(1, Vector3d.rotateVector(Vector3d.scale(rightV, -1), rotation), location);

        setPlane(2, Vector3d.rotateVector(Vector3d.scale(leftV, -1), rotation), location);

        setPlane(3, Vector3d.rotateVector(Vector3d.scale(upV, -1), rotation), location);

        setPlane(4, Vector3d.rotateVector(Vector3d.scale(downV, -1), rotation), location);

    }

    private float horizontalToVerticalFov(float horizontalFov){
        float horizontalFovRad = horizontalFov * 3.14159f/180;
        float aspectRatio = (float)engine.height/(float)engine.width;
        float result = 2 * (float)Math.atan( (float)Math.tan(horizontalFovRad/2) * aspectRatio);
        return result*(180/3.14159f);
    }


    //Init (Runs once)
    public void init(){

    }

    //Tick (Runs every frame)
    public void tick(){

        if(engine.getKeyManager().left)
            addInputYaw(-rotateSpeed);
        if(engine.getKeyManager().right)
            addInputYaw(rotateSpeed);
        if(engine.getKeyManager().up)
            addInputPitch(rotateSpeed);
        if(engine.getKeyManager().down)
            addInputPitch(-rotateSpeed);

        setRotation(Rotator3d.getRotationByYawPitch(yaw, pitch));

        if(engine.getKeyManager().keyW)
            moveForward(moveSpeed);
        if(engine.getKeyManager().keyS)
            moveForward(-moveSpeed);
        if(engine.getKeyManager().keyA)
            moveRight(-moveSpeed);
        if(engine.getKeyManager().keyD)
            moveRight(moveSpeed);

        if(engine.getKeyManager().space)
            moveUp(moveSpeed);
        if(engine.getKeyManager().lshift)
            moveUp(-moveSpeed);

        calculatePlanes();
    }
}

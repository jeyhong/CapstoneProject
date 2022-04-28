package NMM;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, inverseProj, inverseView;
    public Vector2f position;
    private Vector2f projSize = new Vector2f(32.0f * 40.0f,  32.0f * 21.0f);

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f(); //defines units world space is
        this.viewMatrix = new Matrix4f(); //defines which way camera looks
        this.inverseProj = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity(); //inits to identity matrix
        projectionMatrix.ortho(0.0f, projSize.x, 0.0f, projSize.y, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProj);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                                            cameraFront.add(position.x, position.y, 0.0f),
                                            cameraUp);
        this.viewMatrix.invert(inverseView);

        return  this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

    public Matrix4f getInverseProj(){
        return this.inverseProj;
    }

    public Matrix4f getInverseView(){
        return this.inverseView;
    }

    public Vector2f getProjSize(){
        return this.projSize;
    }
}

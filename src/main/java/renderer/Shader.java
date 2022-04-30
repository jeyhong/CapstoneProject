package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean inUSe = false;

    private String vertexSrc;
    private String fragmentSrc;
    private String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String src = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = src.split("(#type)( )+([a-zA-Z]+)");

            int index = src.indexOf("#type") + 6;
            int eol = src.indexOf("\r\n", index);
            String firstPattern = src.substring(index, eol).trim();

            index = src.indexOf("#type", eol) + 6;
            eol = src.indexOf("\r\n", index);
            String secondPattern = src.substring(index, eol).trim();

            if(firstPattern.equals("vertex")){
                vertexSrc = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSrc = splitString[1];
            }else{
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if(secondPattern.equals("vertex")){
                vertexSrc = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSrc = splitString[2];
            }else{
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

        } catch(IOException e){
            e.printStackTrace();
            assert false: "Error: Could not open file for shader: '" + filepath + "'";
        }

    }

    public void compile(){
        int vertexID, fragmentID;

        //compile and link shaders
        //first load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader source to GPU
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        //check for error in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\t Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader source to GPU
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        //check for error in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\t Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        //Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\t Linking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use(){
        if(!inUSe){
            //Bind shader program
            glUseProgram(shaderProgramID);
            inUSe = true;
        }
    }

    public void detach(){
        //end shader program
        glUseProgram(0);
        inUSe = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }
    public void uploadVec4f(String varName, Vector4f vec){
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varlocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec){
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varlocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec){
        int varlocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varlocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }
}

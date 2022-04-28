package renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private int fboID = 0;
    private Texture tex = null;
    public Framebuffer(int width, int height){
        //Generating framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        //Create texture to render and attach framebuffer to
        this.tex = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.tex.getId(), 0);

        //Create renderbuffer; stores depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID); //attaches renderbuffer to framebuffer

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            assert false: "Error: Framebuffer is not complete";
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    public int getFboID() {
        return fboID;
    }

    public int getTexID() {
        return tex.getId();
    }
}

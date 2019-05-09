package com.pieisnotpi.engine.rendering.cameras;

import com.pieisnotpi.engine.rendering.Renderable;
import com.pieisnotpi.engine.rendering.mesh.Mesh;
import org.joml.Vector2i;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public interface DrawPassInit
{
    default void init(Camera camera)
    {
        camera.addDrawPass(0, new DrawPass(0, camera)
        {
            @Override
            void draw(List<Renderable> renderables)
            {
                camera.frameBuffer.bind();
                glViewport(0, 0, camera.frameBuffer.res.x, camera.frameBuffer.res.y);
                glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
                
                if(renderables != null) renderables.forEach(r -> { for(Mesh mesh : r.getMeshes()) mesh.draw(r.getTransform(), camera); });
            }
        });
        
        camera.addDrawPass(1, new DrawPass(1, camera)
        {
            @Override
            void draw(List<Renderable> renderables)
            {
                if(renderables != null) renderables.forEach(r -> { for(Mesh mesh : r.getMeshes()) mesh.draw(r.getTransform(), camera); });
            }
        });
        
        camera.addDrawPass(14, new DrawPass(14, camera)
        {
            @Override
            void draw(List<Renderable> renderables)
            {
                camera.shaders.forEach(s ->
                {
                    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
                    s.shader.draw(Camera.blankTransform, camera.mesh, camera);
                });
                
                camera.frameBuffer.unbind();
            }
        });
        
        camera.addDrawPass(15, new DrawPass(15, camera)
        {
            @Override
            void draw(List<Renderable> renderables)
            {
                glClearColor(0, 0, 0, 0);
                glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
                
                Vector2i res = getBufferRes();
                glViewport((int) (camera.viewPos.x*res.x), (int) (camera.viewPos.y*res.y), (int) (camera.viewSize.x*res.x), (int) (camera.viewSize.y*res.y));
                camera.mesh.draw(Camera.blankTransform, camera);
            }
        });
    }
}

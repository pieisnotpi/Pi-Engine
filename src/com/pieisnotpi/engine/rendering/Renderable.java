package com.pieisnotpi.engine.rendering;

import com.pieisnotpi.engine.rendering.mesh.Mesh;
import com.pieisnotpi.engine.rendering.mesh.Transform;

import java.util.Comparator;

public class Renderable
{
    public static final Comparator<Renderable> comparator = Comparator.comparingInt(r -> r.layer);

    private Mesh[] meshes;
    private Transform transform;
    private int pass, layer;

    public Renderable(int pass, Transform transform, Mesh... meshes)
    {
        this(pass, 0, transform, meshes);
    }
    
    public Renderable(int pass, int layer, Transform transform, Mesh... meshes)
    {
        this.pass = pass;
        this.layer = layer;
        this.transform = transform;
        this.meshes = meshes;
    }

    //TODO: ASSIMP Importing
    /*public Renderable(int pass, int layer, Transform transform, String path) throws Exception
    {
        this.pass = pass;
        this.layer = layer;
        this.transform = transform;

        File f = FileUtility.findFile(path);
        if (f == null) throw new FileNotFoundException("Couldn't find file " + path);
        AIScene scene = aiImportFile(f.getAbsolutePath(), aiProcess_JoinIdenticalVertices | aiProcess_Triangulate | aiProcess_FixInfacingNormals);
        if (scene == null) throw new FileNotFoundException("Invalid/nonexistent file provided");

        int numMaterials = scene.mNumMaterials();
        //ADSMaterial[] materials = new ADSMaterial[numMaterials];
        Material[] materials = new TexMaterial[numMaterials];
        PointerBuffer pMaterials = scene.mMaterials();

        for (int i = 0; i < numMaterials; i++)
        {
            AIMaterial m = AIMaterial.create(pMaterials.get(0));
            //materials[i] = new ADSMaterial(m);
            materials[i] = new TexMaterial(Camera.PERSP, Texture.getTextureFile("grass"));
        }

        int numMeshes = scene.mNumMeshes();
        PointerBuffer pMeshes = scene.mMeshes();
        meshes = new Mesh[numMeshes];

        for (int i = 0; i < numMeshes; i++)
        {
            AIMesh m = AIMesh.create(pMeshes.get(i));
            meshes[i] = new Mesh(m, materials);
        }
    }*/

    public int getPass()
    {
        return pass;
    }

    public int getLayer()
    {
        return layer;
    }

    public Mesh[] getMeshes()
    {
        return meshes;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void destroy()
    {
        for (Mesh mesh : meshes)
        {
            mesh.destroy();
        }
    }
}

package com.pieisnotpi.engine.utility;

import com.pieisnotpi.engine.GameInstance;
import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.shaders.ShaderFile;
import com.pieisnotpi.engine.updates.GameUpdate;

import java.io.File;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class ShaderReloadUtility
{
    public static final Logger logger = new Logger("SHADER RELOADER");

    private WatchService service;

    public ShaderReloadUtility(GameInstance instance, Path assetDirectory)
    {
        try
        {
            service = FileSystems.getDefault().newWatchService();
            assetDirectory.register(service, ENTRY_MODIFY);

            String p = assetDirectory.toString() + '/';

            instance.registerUpdate(new GameUpdate(1, timeStep ->
            {
                while(true)
                {
                    final WatchKey key = service.poll();
                    if(key == null) return;

                    for(WatchEvent<?> event : key.pollEvents())
                    {
                        if(event.kind() == ENTRY_MODIFY)
                        {
                            final Path path = (Path) event.context();
                            final String name = path.toString();
                            final File file = new File(p + name);

                            instance.windows.forEach(w ->
                            {
                                ShaderFile f = w.glInstance.getShaderFile(name);
                                if(f != null) f.reload(file);
                            });
                        }
                    }

                    key.reset();
                }
            }));
        }
        catch(Exception e) {e.printStackTrace();}
    }
}

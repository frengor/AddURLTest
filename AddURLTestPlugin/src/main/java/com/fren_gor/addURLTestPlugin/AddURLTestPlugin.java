package com.fren_gor.addURLTestPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class AddURLTestPlugin extends JavaPlugin {

    private final Path dest = new File(getDataFolder(), "addurl-test-1.0.jar").toPath();

    @Override
    public void onLoad() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        try (InputStream in = getResource("addurl-test-1.0.jar")) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "An exception occurred saving jar!", e);
        }
    }

    @Override
    public void onEnable() {
        try {
            Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURLMethod.setAccessible(true);

            URLClassLoader classLoader = (URLClassLoader) getClassLoader();
            addURLMethod.invoke(classLoader, dest.toUri().toURL());
        } catch (InaccessibleObjectException e) {
            getLogger().log(Level.SEVERE, "An exception occurred! Did you add the --add-opens java.base/java.net=ALL-UNNAMED parameter?", e);
            return;
        } catch (Throwable t) {
            getLogger().log(Level.SEVERE, "An exception occurred!", t);
            return;
        }

        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
    }
}

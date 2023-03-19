package me.panda_studios.mcmod.core.recipe;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RecipeListener implements Listener {
	public static void getDataPath(Plugin plugin, String path) {
		try {
			InputStream stream = plugin.getClass().getResourceAsStream(path);
			if (stream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

				Path directory = Path.of(Files.createTempDirectory("temp_" + plugin.getName().toLowerCase()).toUri());
//				Files.copy(stream, directory, StandardCopyOption.REPLACE_EXISTING);
//				System.out.println(directory);

				System.out.println(reader.readLine());

				directory.toFile().deleteOnExit();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

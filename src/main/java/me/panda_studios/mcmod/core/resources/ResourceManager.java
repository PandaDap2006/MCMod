package me.panda_studios.mcmod.core.resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.register.Registries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ResourceManager {
	public static void AddFile(File file, ResourceLocation resourceLocation) {
		try {
			File resourcesPath = new File(Mcmod.plugin.getDataFolder().getCanonicalPath() + "/resources");
			File assetsPath = new File(resourcesPath.getCanonicalPath() + "/assets");
			File newFile = new File(assetsPath.getCanonicalPath() + "/" + resourceLocation.getPath());

			//Pack.mcmeta file creation
			File packMetaFile = new File(resourcesPath + "/pack.mcmeta");
			resourcesPath.mkdirs();
			packMetaFile.createNewFile();

			JsonObject packJson = new JsonObject();
			JsonObject pack = new JsonObject();
			pack.addProperty("pack_format", 13);
			pack.addProperty("description", "Resource assets");
			packJson.add("pack", pack);

			FileWriter writer = new FileWriter(packMetaFile);
			writer.write(new Gson().toJson(packJson));
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void createModelBaseItem() {
		int modelID = 1;
		Registries.ITEM.entries.forEach((key, value) -> {

		});
	}
}

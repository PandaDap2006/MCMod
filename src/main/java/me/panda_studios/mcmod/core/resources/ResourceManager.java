package me.panda_studios.mcmod.core.resources;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import me.clip.placeholderapi.util.FileUtil;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.item.itemtypes.*;
import me.panda_studios.mcmod.core.register.Registries;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourceManager {
	static UUID uuid = UUID.randomUUID();
	static String assetsPath = Mcmod.plugin.getDataFolder().getAbsolutePath() + "/assets";
	static String resourcePath = assetsPath + "/resources";
	static String dataPath = assetsPath + "/data";

	public static ModelBaseItem modelBaseItem = new ModelBaseItem();
	public static ModelBaseItem swordBaseItem = new ModelBaseItem();
	public static ModelBaseItem axeBaseItem = new ModelBaseItem();
	public static ModelBaseItem pickaxeBaseItem = new ModelBaseItem();
	public static ModelBaseItem shovelBaseItem = new ModelBaseItem();
	public static ModelBaseItem hoeBaseItem = new ModelBaseItem();

	static HttpServer server;
	public static byte[] bytes;

	public static void StartResourceCreation() {
		File resources = new File(assetsPath);
		if (!resources.exists()) {
			resources.mkdirs();
		}
		try {
			Files.walkFileTree(resources.toPath(), new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult postVisitDirectory(
						Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(
						Path file, BasicFileAttributes attrs)
						throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		try {
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				ResourceManager.writeAssetsFolder(
						plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
						plugin.getName().toLowerCase().replace(" ", "_")
				);
				ResourceManager.writeDataFolder(
						plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath(),
						plugin.getName().toLowerCase().replace(" ", "_")
				);
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		ResourceManager.createMCMeta();
		ResourceManager.createModelBaseItem();
		DataResources.register();

		ResourceManager.bytes = ResourceManager.zipResourcesToBytes();
	}

	public static void addFile(String file, NamespacedKey resourceLocation) {
		try {
			File resourcesPath = new File(resourcePath);
			File assetsPath = new File(resourcesPath.getAbsolutePath() + "/assets");
			File newFile = new File(assetsPath.getAbsolutePath() + "/" + resourceLocation.getNamespace() + "/" + resourceLocation.getKey());

			if (!newFile.getParentFile().exists())
				newFile.getParentFile().mkdirs();
			newFile.createNewFile();

			FileWriter writer = new FileWriter(newFile);
			writer.write(file);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static JsonObject getJsonFile(Plugin plugin, String path) {
		JsonObject root = null;
		InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream("assets/" + path);
		if (inputStream != null) {
			Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
			String json = scanner.hasNext() ? scanner.next() : "";
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			root = gson.fromJson(json, JsonObject.class);
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return root;
	}

	public static void createModelBaseItem() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Registries.ITEM.entries.forEach((key, value) -> {
			String[] name = key.split(":");
			ModelBaseItem baseItem;
			if (value instanceof SwordItem) {
				baseItem = swordBaseItem;
			} else if (value instanceof AxeItem) {
				baseItem = axeBaseItem;
			} else if (value instanceof PickaxeItem) {
				baseItem = pickaxeBaseItem;
			} else if (value instanceof ShovelItem) {
				baseItem = shovelBaseItem;
			} else if (value instanceof HoeItem) {
				baseItem = hoeBaseItem;
			} else {
				baseItem = modelBaseItem;
			}
			baseItem.addModel(key, name[0], "item/" + name[1]);
		});
		Registries.BLOCK.entries.forEach((key, value) -> {
			String[] name = key.split(":");
			modelBaseItem.addModel(key, name[0], "block/" + name[1]);
		});
		Registries.ENTITY.entries.forEach((key, value) -> {
			JsonObject modelRoot = ResourceManager.getJsonFile(value.plugin, "models/" + value.model.modelLocation() + ".json");
			if (modelRoot.get("format_version").getAsString().equals("1.12.0")) {
				JsonObject geomatryJson = ((JsonObject) modelRoot.getAsJsonArray("minecraft:geometry").get(0));
				//loops all the bones to make the item model for each bone
				for (JsonElement boneElement : geomatryJson.getAsJsonArray("bones")) {
					JsonObject modelBone = (JsonObject) boneElement;

					JsonObject partJson = new JsonObject();
					partJson.addProperty("credit", "Made with " + Mcmod.plugin.getName());
					JsonObject description = geomatryJson.getAsJsonObject("description");
					double uvWidth = description.get("texture_width").getAsDouble();
					double uvHeight = description.get("texture_height").getAsDouble();
					JsonArray textureSize = new JsonArray(2);
					textureSize.add((int) uvWidth);
					textureSize.add((int) uvHeight);
					partJson.add("texture_size", textureSize);

					JsonObject textureJson = new JsonObject();
					textureJson.addProperty("0",
							value.plugin.getName().toLowerCase().replace(" ", "_") + ":item/" + value.model.textureLocation());
					partJson.add("textures", textureJson);
					JsonArray partElements = new JsonArray();

					JsonArray bonePivot = modelBone.getAsJsonArray("pivot");
					if (modelBone.has("cubes")) {
						JsonArray cubesArray = modelBone.getAsJsonArray("cubes");
						for (JsonElement cubeElement : cubesArray) {
							JsonObject modelCube = (JsonObject) cubeElement;
							JsonObject partElement = new JsonObject();

							//Coordinates
							JsonArray boneCubeOrigin = modelCube.getAsJsonArray("origin");
							JsonArray boneCubeSize = modelCube.getAsJsonArray("size");

							JsonArray partFrom = new JsonArray(3);
							JsonArray partTo = new JsonArray(3);

							double x = (boneCubeOrigin.get(0).getAsDouble() - bonePivot.get(0).getAsDouble());
							double y = (boneCubeOrigin.get(1).getAsDouble() - bonePivot.get(1).getAsDouble());
							double z = (boneCubeOrigin.get(2).getAsDouble() - bonePivot.get(2).getAsDouble());

							double xs = boneCubeSize.get(0).getAsDouble();
							double ys = boneCubeSize.get(1).getAsDouble();
							double zs = boneCubeSize.get(2).getAsDouble();

							partFrom.add((x/10) + 8);
							partFrom.add((y/10) + 8);
							partFrom.add((z/10) + 8);

							partTo.add(((x + xs)/10) + 8);
							partTo.add(((y + ys)/10) + 8);
							partTo.add(((z + zs)/10) + 8);

							partElement.add("from", partFrom);
							partElement.add("to", partTo);

							//Inflate
							if (modelCube.has("inflate"))
								partElement.addProperty("inflate", modelCube.get("inflate").getAsDouble());

							//Rotation
							if (modelCube.has("rotation"))
								partElement.add("rotation", modelCube.getAsJsonArray("rotation"));

							//Textures
							JsonObject textureFaces = new JsonObject();

							JsonObject northFace = new JsonObject();
							JsonArray northFaceUV = new JsonArray(4);
							JsonObject southFace = new JsonObject();
							JsonArray southFaceUV = new JsonArray(4);
							JsonObject westFace = new JsonObject();
							JsonArray westFaceUV = new JsonArray(4);
							JsonObject eastFace = new JsonObject();
							JsonArray eastFaceUV = new JsonArray(4);
							JsonObject upFace = new JsonObject();
							JsonArray upFaceUV = new JsonArray(4);
							JsonObject downFace = new JsonObject();
							JsonArray downFaceUV = new JsonArray(4);

							JsonArray uvPos = modelCube.getAsJsonArray("uv");

							double xUV = uvPos.get(0).getAsDouble();
							double yUV = uvPos.get(1).getAsDouble();

							double xSizeUV = boneCubeSize.get(0).getAsDouble();
							double ySizeUV = boneCubeSize.get(1).getAsDouble();
							double zSizeUV = boneCubeSize.get(2).getAsDouble();

							northFaceUV.add((xUV + zSizeUV) / (uvWidth/16));
							northFaceUV.add((yUV + zSizeUV) / (uvHeight/16));
							northFaceUV.add((xUV + zSizeUV + xSizeUV) / (uvWidth/16));
							northFaceUV.add((yUV + zSizeUV + ySizeUV) / (uvHeight/16));

							southFaceUV.add((xUV + zSizeUV*2 + xSizeUV) / (uvWidth/16));
							southFaceUV.add((yUV + zSizeUV) / (uvHeight/16));
							southFaceUV.add((xUV + zSizeUV*2 + xSizeUV*2) / (uvWidth/16));
							southFaceUV.add((yUV + zSizeUV + ySizeUV) / (uvHeight/16));

							eastFaceUV.add((xUV + zSizeUV + xSizeUV) / (uvWidth/16));
							eastFaceUV.add((yUV + zSizeUV) / (uvHeight/16));
							eastFaceUV.add((xUV + zSizeUV*2 + xSizeUV) / (uvWidth/16));
							eastFaceUV.add((yUV + zSizeUV + ySizeUV) / (uvHeight/16));

							westFaceUV.add((xUV) / (uvWidth/16));
							westFaceUV.add((yUV + zSizeUV) / (uvHeight/16));
							westFaceUV.add((xUV + zSizeUV) / (uvWidth/16));
							westFaceUV.add((yUV + zSizeUV + ySizeUV) / (uvHeight/16));

							upFaceUV.add((xUV + zSizeUV) / (uvWidth/16));
							upFaceUV.add((yUV) / (uvHeight/16));
							upFaceUV.add((xUV + zSizeUV + xSizeUV) / (uvWidth/16));
							upFaceUV.add((yUV + zSizeUV) / (uvHeight/16));

							downFaceUV.add((xUV + zSizeUV + xSizeUV) / (uvWidth/16));
							downFaceUV.add((yUV) / (uvHeight/16));
							downFaceUV.add((xUV + zSizeUV + xSizeUV*2) / (uvWidth/16));
							downFaceUV.add((yUV + zSizeUV) / (uvHeight/16));


							northFace.add("uv", northFaceUV);
							southFace.add("uv", southFaceUV);
							westFace.add("uv", westFaceUV);
							eastFace.add("uv", eastFaceUV);
							upFace.add("uv", upFaceUV);
							downFace.add("uv", downFaceUV);

							northFace.addProperty("texture", "#0");
							southFace.addProperty("texture", "#0");
							westFace.addProperty("texture", "#0");
							eastFace.addProperty("texture", "#0");
							upFace.addProperty("texture", "#0");
							downFace.addProperty("texture", "#0");

							textureFaces.add("north", northFace);
							textureFaces.add("south", southFace);
							textureFaces.add("west", westFace);
							textureFaces.add("east", eastFace);
							textureFaces.add("up", upFace);
							textureFaces.add("down", downFace);

							partElement.add("faces", textureFaces);
							partElements.add(partElement);
						}
						partJson.add("elements", partElements);
						String[] location = value.name.split(":");
						String name = location[1] + "_" + modelBone.get("name").getAsString();
						NamespacedKey path = new NamespacedKey(location[0], "entity_parts/" + name + ".modelpart");
						ResourceManager.addFile(gson.toJson(partJson), new NamespacedKey(path.getNamespace(), "models/" + path.getKey() + ".json"));
						ResourceManager.modelBaseItem.addModel(location[0] + ":" + name, path.getNamespace(), path.getKey());
					}
				}
			}
		});

		ResourceManager.addFile(gson.toJson(modelBaseItem.GenerateJson()), new NamespacedKey("minecraft", "models/item/paper.json"));


		ResourceManager.addFile(gson.toJson(swordBaseItem.GenerateJson("minecraft:item/handheld", "minecraft:item/wooden_sword")),
				new NamespacedKey("minecraft", "models/item/wooden_sword.json"));
		ResourceManager.addFile(gson.toJson(axeBaseItem.GenerateJson("minecraft:item/handheld", "minecraft:item/wooden_axe")),
				new NamespacedKey("minecraft", "models/item/wooden_axe.json"));
		ResourceManager.addFile(gson.toJson(pickaxeBaseItem.GenerateJson("minecraft:item/handheld", "minecraft:item/wooden_pickaxe")),
				new NamespacedKey("minecraft", "models/item/wooden_pickaxe.json"));
		ResourceManager.addFile(gson.toJson(shovelBaseItem.GenerateJson("minecraft:item/handheld", "minecraft:item/wooden_shovel")),
				new NamespacedKey("minecraft", "models/item/wooden_shovel.json"));
		ResourceManager.addFile(gson.toJson(hoeBaseItem.GenerateJson("minecraft:item/handheld", "minecraft:item/wooden_hoe")),
				new NamespacedKey("minecraft", "models/item/wooden_hoe.json"));
	}
	public static void createMCMeta() {
		try {
			File resourcesPath = new File(resourcePath);
			File newFile = new File(resourcesPath.getAbsolutePath() + "/pack.mcmeta");

			if (!newFile.getParentFile().exists())
				newFile.getParentFile().mkdirs();
			newFile.createNewFile();

			JsonObject packJson = new JsonObject();
			JsonObject pack = new JsonObject();
			pack.addProperty("pack_format", 13);
			pack.addProperty("description", "Resource assets");
			packJson.add("pack", pack);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			FileWriter writer = new FileWriter(newFile);
			writer.write(gson.toJson(packJson));
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeAssetsFolder(String jarpath, String namespace) {
		File resourcesPath = new File(resourcePath + "/assets/" + namespace);
		String assetsPathJar = "assets/";
		try {
			resourcesPath.mkdirs();
			JarFile jarFile = new JarFile(jarpath);
			for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
				if (jarEntry.getName().startsWith(assetsPathJar)) {
					String path = resourcesPath.getAbsolutePath() + "/" + jarEntry.getName().substring(assetsPathJar.length());
					File file = new File(path);

					if (jarEntry.isDirectory()) {
						file.mkdirs();
					} else {
						try (InputStream inputStream = jarFile.getInputStream(jarEntry);
							 OutputStream outputStream = new FileOutputStream(file)) {

							byte[] buffer = new byte[4096];
							int bytesRead;

							while ((bytesRead = inputStream.read(buffer)) != -1) {
								outputStream.write(buffer, 0, bytesRead);
							}
						}
					}
				}
			}
			jarFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void writeDataFolder(String jarpath, String namespace) {
		File resourcesPath = new File(dataPath + "/" + namespace);
		String assetsPathJar = "data/";
		try {
			resourcesPath.mkdirs();
			JarFile jarFile = new JarFile(jarpath);
			for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
				if (jarEntry.getName().startsWith(assetsPathJar)) {
					String path = resourcesPath.getAbsolutePath() + "/" + jarEntry.getName().substring(assetsPathJar.length());
					File file = new File(path);

					if (jarEntry.isDirectory()) {
						file.mkdirs();
					} else {
						try (InputStream inputStream = jarFile.getInputStream(jarEntry);
							 OutputStream outputStream = new FileOutputStream(file)) {

							byte[] buffer = new byte[4096];
							int bytesRead;

							while ((bytesRead = inputStream.read(buffer)) != -1) {
								outputStream.write(buffer, 0, bytesRead);
							}
						}
					}
				}
			}
			jarFile.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] zipResourcesToBytes() {
		try {
			String sourceFolder = resourcePath;
			Path sourceFolderPath = Paths.get(sourceFolder);
			new File(sourceFolder).mkdirs();

			String outputFilePath = sourceFolder + ".zip";
			File outputFile = new File(outputFilePath);

			// Create the output stream for the zip file
			OutputStream outputStream = new FileOutputStream(outputFile);
			ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(outputStream);

			// Traverse the contents of the folder and add them to the zip file
			Files.walk(sourceFolderPath).forEach(filePath -> {
				try {
					// Ignore the folder itself
					if (filePath.equals(sourceFolderPath)) {
						return;
					}

					// Create the zip entry for the file or directory
					ZipArchiveEntry entry = new ZipArchiveEntry(sourceFolderPath.relativize(filePath).toString());

					// Set the Unix mode (Unix permissions) for directories
					if (Files.isDirectory(filePath)) {
						entry.setUnixMode(0755); // rwxr-xr-x
					}

					// Add the entry to the output stream
					zipArchiveOutputStream.putArchiveEntry(entry);

					// If the entry is a file, write its contents to the output stream
					if (Files.isRegularFile(filePath)) {
						InputStream inputStream = new FileInputStream(filePath.toFile());
						IOUtils.copy(inputStream, zipArchiveOutputStream);
						inputStream.close();
					}

					// Close the current entry
					zipArchiveOutputStream.closeArchiveEntry();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			// Close the output stream
			zipArchiveOutputStream.close();

			return Files.readAllBytes(Paths.get(outputFilePath));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static String startResourcesDownload() {
		try {
			int port = 8080;
			InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port);
			server = HttpServer.create(address, 0);

			server.createContext("/" + uuid, exchange -> {
				exchange.sendResponseHeaders(200, 0);
				OutputStream os = exchange.getResponseBody();
				os.write(bytes);
				os.close();
			});
			server.setExecutor(null);

			server.start();
			return "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "/" + uuid;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void stopResourcesDownload() {
		if (server != null)
			server.stop(0);
	}
}

package me.panda_studios.mcmod.core.resources;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataResources {
	static Path dataPath = Path.of(Mcmod.plugin.getDataFolder().getPath(), "assets/data");

	static Map<String, List<ItemStack>> itemTags = new HashMap<>();

	public static void register() {
		try {
			registerItemTags();
			registerRecipes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void registerItemTags() throws IOException {
		File dataFile = new File(dataPath.toUri());
		for (File data : dataFile.listFiles()) {
			String namespace = data.getName();
			Path tagPath = Path.of(data.getPath(), "tags");
			if (tagPath.toFile().isDirectory()) {
				Files.walkFileTree(tagPath, new SimpleFileVisitor<>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						if (file.toFile().getName().endsWith(".json")) {
							List<ItemStack> items = new ArrayList<>();
							for (JsonElement values : new Gson().fromJson(Files.readString(file), JsonObject.class).getAsJsonArray("values")) {
								items.add(Mcmod.getItemstack(values.getAsString(), 1));
							}
							itemTags.put(namespace + ":" + file.toFile().getName().replace(".json", ""), items);
						}
						return super.visitFile(file, attrs);
					}
				});
			}
		}
	}

	public static void registerRecipes() throws IOException {
		File dataFile = new File(dataPath.toUri());
		for (File data : dataFile.listFiles()) {
			String namespace = data.getName();
			Path tagPath = Path.of(data.getPath(), "recipes");
			if (tagPath.toFile().isDirectory()) {
				Files.walkFileTree(tagPath, new SimpleFileVisitor<>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						if (file.toFile().getName().endsWith(".json")) {
							JsonObject recipeRoot = new Gson().fromJson(Files.readString(file), JsonObject.class);
							String type = recipeRoot.get("type").getAsString();

							String itemID = recipeRoot.getAsJsonObject("result").get("item").getAsString();
							int amount = 1;
							if (recipeRoot.getAsJsonObject("result").has("count"))
								amount = recipeRoot.getAsJsonObject("result").get("count").getAsInt();
							ItemStack result = Mcmod.getItemstack(itemID, amount);
							String name = file.toFile().getName().replace(".json", "");
							switch (type) {
								case "minecraft:crafting_shaped", "crafting_shaped" -> {
									JsonArray pattern = recipeRoot.getAsJsonArray("pattern");
									ShapedRecipe shapedRecipe = new ShapedRecipe(
											new NamespacedKey(namespace, name),
											result
									);

									List<String> shape = new ArrayList<>();
									pattern.forEach(jsonElement -> shape.add(jsonElement.getAsString()));
									shapedRecipe.shape(shape.toArray(String[]::new));

									recipeRoot.getAsJsonObject("key").asMap().forEach((k, v) -> {
										JsonObject value = v.getAsJsonObject();
										if (value.has("tag")) {
											shapedRecipe.setIngredient(k.charAt(0),
													new RecipeChoice.ExactChoice(DataResources.getItemTags(value.get("tag").getAsString()))
											);
										} else {
											shapedRecipe.setIngredient(k.charAt(0),
													new RecipeChoice.ExactChoice(Mcmod.getItemstack(value.get("item").getAsString(), 1))
											);
										}
									});

									Bukkit.addRecipe(shapedRecipe);
								}
								case "minecraft:crafting_shapeless", "crafting_shapeless" -> {
									ShapelessRecipe recipe = new ShapelessRecipe(
											new NamespacedKey(namespace, name),
											result
									);
									recipeRoot.getAsJsonArray("ingredients").forEach(v -> {
										if (v.isJsonObject()) {
											JsonObject value = v.getAsJsonObject();
											if (value.has("tag")) {
												recipe.addIngredient(
														new RecipeChoice.ExactChoice(DataResources.getItemTags(value.get("tag").getAsString())));
											} else {
												recipe.addIngredient(
														new RecipeChoice.ExactChoice(Mcmod.getItemstack(value.get("item").getAsString(), 1)));
											}
										} else {
											v.getAsJsonArray().forEach(v2 -> {
												JsonObject value = v2.getAsJsonObject();
												if (value.has("tag")) {
													recipe.addIngredient(
															new RecipeChoice.ExactChoice(DataResources.getItemTags(value.get("tag").getAsString())));
												} else {
													recipe.addIngredient(
															new RecipeChoice.ExactChoice(Mcmod.getItemstack(value.get("item").getAsString(), 1)));
												}
											});
										}
									});
								}
							}
						}
						return super.visitFile(file, attrs);
					}
				});
			}
		}
	}

	public static ItemStack[] getItemTags(String name) {
		return itemTags.get(name).toArray(ItemStack[]::new);
	}
}

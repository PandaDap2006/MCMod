package me.panda_studios.mcmod.core.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ModelBaseItem {
	public final Map<String, Integer> modelIDs = new HashMap<>();

	JsonObject itemJson;
	JsonArray overridesJson;

	int modelID = 1;

	public ModelBaseItem() {
		itemJson = new JsonObject();
		JsonObject textureJson = new JsonObject();
		overridesJson = new JsonArray();
		itemJson.addProperty("parent", "minecraft:item/generated");
		textureJson.addProperty("layer0", "minecraft:item/paper");
		itemJson.add("textures", textureJson);
	}

	public void addModel(String name, String namespace, String path) {
		JsonObject model = new JsonObject();
		JsonObject predicate = new JsonObject();
		predicate.addProperty("custom_model_data", modelID);

		model.add("predicate", predicate);
		model.addProperty("model", namespace + ":" + path);
		modelIDs.put(name, modelID++);
		overridesJson.add(model);
	}

	public JsonObject GenerateJson() {
		itemJson.add("overrides", overridesJson);
		return itemJson;
	}
}

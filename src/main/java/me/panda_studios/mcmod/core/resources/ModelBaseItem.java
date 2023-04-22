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
		overridesJson = new JsonArray();
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
		itemJson.addProperty("parent", "minecraft:item/generated");
		itemJson.add("overrides", overridesJson);
		return GenerateJson("minecraft:item/generated", "minecraft:item/paper");
	}

	public JsonObject GenerateJson(String parent, String texture) {
		itemJson.addProperty("parent", parent);

		JsonObject textureJson = new JsonObject();
		textureJson.addProperty("layer0", texture);
		itemJson.add("textures", textureJson);

		itemJson.add("overrides", overridesJson);
		return itemJson;
	}
}

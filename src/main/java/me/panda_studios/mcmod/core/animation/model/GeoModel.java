package me.panda_studios.mcmod.core.animation.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.*;

public record GeoModel(
	String version,
	Description description,
	Map<String, Bone> bones
) {
	record Description(
		String identifier,
		Vector2i texture_size,
		Vector2f visible_bounds,
		Vector3f visible_bounds_offset
	) {}

	public record Bone(
		String name,
		String parent,
		Vector3f pivot,
		Vector3f rotation,
		Collection<Cube> cubes,
		Collection<Bone> children
	) {
		record Cube(
			Vector3f origin,
			Vector3f rotation,
			Vector3f size,
			float inflate,
			Vector2i uv
		) {}
	}

	public static final Map<String, GeoModel> models = new HashMap<>();

	public static GeoModel of(Plugin plugin, String namespace) {
		JsonObject jsonRoot = ResourceManager.getJsonFile(plugin,
				"models/" + namespace + ".json");
		JsonObject geometryJson = jsonRoot.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject();
		JsonObject description = geometryJson.getAsJsonObject("description");

		Map<String ,Bone> bones = new HashMap<>();
		for (JsonElement jsonElement : geometryJson.getAsJsonArray("bones")) {
			JsonObject bone = jsonElement.getAsJsonObject();

			String name = bone.get("name").getAsString();
			String parent = bone.has("parent") ? bone.get("parent").getAsString() : "";
			Vector3f pivot = bone.has("pivot") ? new Vector3f(
					bone.getAsJsonArray("pivot").get(0).getAsFloat(),
					bone.getAsJsonArray("pivot").get(1).getAsFloat(),
					bone.getAsJsonArray("pivot").get(2).getAsFloat()
			) : new Vector3f();
			Vector3f rotation = bone.has("rotation") ? new Vector3f(
					bone.getAsJsonArray("rotation").get(0).getAsFloat(),
					bone.getAsJsonArray("rotation").get(1).getAsFloat(),
					bone.getAsJsonArray("rotation").get(2).getAsFloat()
			) : new Vector3f();

			List<Bone.Cube> cubes = new ArrayList<>();
			if (bone.has("cubes")) {
				for (JsonElement jsonElement1 : bone.getAsJsonArray("cubes")) {
					JsonObject cube = jsonElement1.getAsJsonObject();

					Vector3f origin = cube.has("origin") ? new Vector3f(
							cube.getAsJsonArray("origin").get(0).getAsFloat(),
							cube.getAsJsonArray("origin").get(1).getAsFloat(),
							cube.getAsJsonArray("origin").get(2).getAsFloat()
					) : new Vector3f();
					Vector3f cRotation = cube.has("rotation") ? new Vector3f(
							cube.getAsJsonArray("rotation").get(0).getAsFloat(),
							cube.getAsJsonArray("rotation").get(1).getAsFloat(),
							cube.getAsJsonArray("rotation").get(2).getAsFloat()
					) : new Vector3f();
					Vector3f size = cube.has("size") ? new Vector3f(
							cube.getAsJsonArray("size").get(0).getAsFloat(),
							cube.getAsJsonArray("size").get(1).getAsFloat(),
							cube.getAsJsonArray("size").get(2).getAsFloat()
					) : new Vector3f();
					float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : 0;
					Vector2i uv = cube.has("uv") ? new Vector2i(
							cube.getAsJsonArray("uv").get(0).getAsInt(),
							cube.getAsJsonArray("uv").get(1).getAsInt()
					) : new Vector2i();

					cubes.add(new Bone.Cube(origin, cRotation, size, inflate, uv));
				}
			}

			Bone newBone = new Bone(name, parent, pivot, rotation, cubes, new ArrayList<>());

			if (!parent.isEmpty())
				bones.get(parent).children.add(newBone);

			bones.put(name, newBone);
		}

		return new GeoModel(
				jsonRoot.get("format_version").getAsString(),
				new Description(
						description.get("identifier").getAsString(),
						new Vector2i(description.get("texture_width").getAsInt(), description.get("texture_height").getAsInt()),
						new Vector2f(description.get("visible_bounds_width").getAsFloat(), description.get("visible_bounds_height").getAsFloat()),
						new Vector3f(description.getAsJsonArray("visible_bounds_offset").get(0).getAsFloat(),
								description.getAsJsonArray("visible_bounds_offset").get(1).getAsFloat(),
								description.getAsJsonArray("visible_bounds_offset").get(2).getAsFloat())
				),
				bones
		);
	}

	public static void register(Plugin plugin, String namespace) {
		models.put(namespace, of(plugin, namespace));
	}
}

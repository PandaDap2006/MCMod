package me.panda_studios.mcmod.core.resources;

public class ResourceLocation {
	private final String namespace;
	private final String path;

	public ResourceLocation(String namespace, String path) {
		this.namespace = namespace;
		this.path = path;
	}

	public ResourceLocation(String path) {
		this.namespace = null;
		this.path = path;
	}

	public String getPath() {
		return "/" + (namespace != null ? namespace + "/" : "") + path;
	}
}

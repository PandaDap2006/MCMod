package me.panda_studios.mcmod.core.datarecords;

import com.google.gson.Gson;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class DataTypes {
	public static final DataType<EntityData> Entity = new DataType<>(EntityData.class);
	public static final DataType<BlockData> Block = new DataType<>(BlockData.class);
	public static final DataType<ItemData> Item = new DataType<>(ItemData.class);

	static class DataType<T extends Record> implements PersistentDataType<String, T> {
		private final Class<T> dataType;

		DataType(Class<T> dataType) {
			this.dataType = dataType;
		}

		@Override
		public @NotNull Class<String> getPrimitiveType() {
			return String.class;
		}

		@Override
		public @NotNull Class<T> getComplexType() {
			return this.dataType;
		}

		@Override
		public @NotNull String toPrimitive(@NotNull T data, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
			return new Gson().toJson(data);
		}

		@Override
		public @NotNull T fromPrimitive(@NotNull String s, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
			return new Gson().fromJson(s, dataType);
		}
	}
}

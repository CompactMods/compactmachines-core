package dev.compactmods.machines.data;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CodecBackedSavedData<T extends SavedData> extends SavedData {

    private static final Logger LOGS = LogManager.getLogger();

    protected final CodecWrappedFactory<T> factory;

    public CodecBackedSavedData(CodecWrappedFactory<T> factory) {
        this.factory = factory;
    }

    public static <T extends SavedData> CodecWrappedFactory<T> codecFactory(Codec<T> codec, Supplier<T> factory) {
        return new CodecWrappedFactory<>(codec, factory);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        return factory.save(compoundTag, (T) this);
    }

    public record CodecWrappedFactory<T extends SavedData>(Codec<T> codec, Supplier<T> factory) {

        public SavedData.Factory<T> asSDFactory() {
            return new SavedData.Factory<>(factory, this::load, null);
        }

        @NotNull
        public CompoundTag save(@NotNull CompoundTag compoundTag, @NotNull T instance) {
            final var data = codec.encodeStart(NbtOps.INSTANCE, instance)
                    .getOrThrow(false, LOGS::error);

            if (data instanceof CompoundTag dataTag) {
                compoundTag.merge(dataTag);
            }

            return compoundTag;
        }

        public T load(CompoundTag tag) {
            return codec.parse(NbtOps.INSTANCE, tag).getOrThrow(false, LOGS::error);
        }
    }
}

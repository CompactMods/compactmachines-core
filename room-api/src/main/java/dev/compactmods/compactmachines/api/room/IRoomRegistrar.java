package dev.compactmods.compactmachines.api.room;

import dev.compactmods.compactmachines.api.room.registration.IRoomBuilder;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IRoomRegistrar {

    default RoomInstance createNew(RoomTemplate template, UUID owner) {
        return createNew(template, owner, override -> override);
    }

    default RoomInstance createNew(RoomTemplate template, UUID owner, Function<IRoomBuilder, IRoomBuilder> override) {
        return createNew(builder -> builder.defaultMachineColor(template.color())
                .owner(owner)
                .dimensions(template.dimensions()));
    }

    RoomInstance createNew(Function<IRoomBuilder, IRoomBuilder> build);

    boolean isRegistered(String room);

    Optional<RoomInstance> get(String room);

    long count();

    Stream<String> allRoomCodes();

    Stream<RoomInstance> allRooms();
}

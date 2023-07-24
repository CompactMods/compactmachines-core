package dev.compactmods.compactmachines.api.room;

import java.util.Optional;
import java.util.UUID;

// capability stuff, maybe redo in 1.20
public interface IPlayerRoomMetadataProvider {
    Optional<IPlayerRoomMetadata> currentRoom();
    Optional<String> roomCode();
    Optional<UUID> owner();
    void clearCurrent();
    void setCurrent(IPlayerRoomMetadata current);
}

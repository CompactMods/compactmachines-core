import dev.compactmods.compactmachines.api.room.Rooms;
import dev.compactmods.compactmachines.api.room.exceptions.NonexistentRoomException;

public class RoomOwnersTests {

    static void example() {

        final var owners = Rooms.OWNER_LOOKUP.get();

        try {
            final var roomOwnerId = owners.getRoomOwner("MY-ROOM-CODE");
            // do things
        } catch (NonexistentRoomException e) {
            // room not found
        }
    }
}

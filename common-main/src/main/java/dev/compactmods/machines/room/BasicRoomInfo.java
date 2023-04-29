package dev.compactmods.machines.room;

import dev.compactmods.machines.api.room.registration.IBasicRoomInfo;

public record BasicRoomInfo(String code, int color) implements IBasicRoomInfo {
}

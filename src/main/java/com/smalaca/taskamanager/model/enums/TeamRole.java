package com.smalaca.taskamanager.model.enums;

import java.util.stream.Stream;

public enum TeamRole {
    DEVELOPER,
    BUSINESS_ANALYSIS,
    TESTER,
    UNDEFINED;

    public static boolean isSupported(String teamRole) {
        return Stream.of(TeamRole.values())
                .anyMatch(role -> role.name().equals(teamRole));
    }
}

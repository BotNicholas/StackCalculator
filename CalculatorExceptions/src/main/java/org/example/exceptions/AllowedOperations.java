package org.example.exceptions;

import java.util.List;

public class AllowedOperations {
    private static final List<String> allowedOperations = List.of("+", "-", "*", "/", "(", ")");

    public static String getAllowedOperations() {
        return allowedOperations.stream().reduce((o1, o2)->o1.concat("\\" + o2)).orElse("unknown");
    }
}

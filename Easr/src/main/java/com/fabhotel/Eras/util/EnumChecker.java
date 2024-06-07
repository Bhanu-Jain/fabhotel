package com.fabhotel.Eras.util;

public class EnumChecker {
	public static <T extends Enum<T>> T getEnumFromString(Class<T> enumClass, String value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        String upperCaseValue = value.toUpperCase();
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(upperCaseValue)) {
                return enumValue;
            }
        }

        // If the enum value is not found, return the default value
        return defaultValue;
    }


}

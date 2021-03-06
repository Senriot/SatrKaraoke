package com.ktvdb.allen.satrok.model;

import java.util.Locale;

/**
 * Created by Allen on 15/7/14.
 */
public enum Direction
{
    ASC, DESC;

    /**
     * Returns the {@link Direction} enum for the given {@link String} value.
     *
     * @param value
     * @return
     * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
     */
    public static Direction fromString(String value)
    {

        try
        {
            return Direction.valueOf(value.toUpperCase(Locale.US));
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(String.format(
                    "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).",
                    value), e);
        }
    }

    /**
     * Returns the {@link Direction} enum for the given {@link String} or null if it cannot be parsed into an enum
     * value.
     *
     * @param value
     * @return
     */
    public static Direction fromStringOrNull(String value)
    {

        try
        {
            return fromString(value);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}

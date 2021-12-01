package org.pyruz.api.shop.utility;

public class TypesHelper {

    public static Integer tryParseInt(Object obj) {
        try {

            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long tryParseLong(Object obj) {
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

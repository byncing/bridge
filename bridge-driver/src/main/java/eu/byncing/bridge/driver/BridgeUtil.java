package eu.byncing.bridge.driver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ThreadLocalRandom;

public class BridgeUtil {

    public static final Gson STATIC_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public static String generatingString(int length, int numeral, int letter) {
        return ThreadLocalRandom.current().ints(numeral, letter + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static StringHelper builder(String... string) {
        return new StringHelper(string);
    }

    public static String[] chatEncode(boolean split, String... strings) {
        StringHelper helper = new StringHelper(strings).replace("&", "§");
        if (split) helper.replace(" ", "_").build();
        return helper.build();
    }

    public static String[] chatDecode(boolean split, String... strings) {
        StringHelper helper = new StringHelper(strings).replace("§", "&");
        if (split) helper.replace(" ", "_").build();
        return helper.build();
    }

    public static class StringHelper {

        private final String[] string;

        public StringHelper(String... string) {
            this.string = string;
        }

        public StringHelper replace(Object replacement, String... targets) {
            for (int i = 0; i < string.length; i++) {
                for (Object c1 : targets) {
                    string[i] = string[i].replace(String.valueOf(c1), String.valueOf(replacement));
                }
            }
            return this;
        }

        public String[] build() {
            return string;
        }

        public String buildIndex(int index) {
            return string[index];
        }

        public String buildLine() {
            StringBuilder builder = new java.lang.StringBuilder();
            for (String s : string) {
                builder.append(s).append(System.lineSeparator());
            }
            return builder.toString();
        }
    }
}
package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils;

/**
 * The type String manupulator.
 */
public class StringManipulator {


    /**
     * Replace special characters from input String.
     *
     * @param input the input
     * @return the string
     */
    public static String replaceSpecialChars(String input) {
        input = input.replaceAll("ä", "ae");
        input = input.replaceAll("ö", "oe");
        input = input.replaceAll("ü", "ue");
        input = input.replaceAll("Ä", "Ae");
        input = input.replaceAll("Ü", "Ue");
        input = input.replaceAll("Ö", "Oe");
        input = input.replaceAll("ß", "ss");
        input = input.replaceAll("[^a-zA-Z0-9 ]", "");
        return input.trim();
    }
}

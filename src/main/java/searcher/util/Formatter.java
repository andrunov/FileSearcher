package searcher.util;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Provides helper methods for formatting and splitting strings used by the application.
 */
public class Formatter {

   /**
    * Formats a numeric value using the provided pattern.
    *
    * @param pattern the decimal format pattern
    * @param value the numeric value to format
    * @return the formatted string
    */
   public static String doubleFormat(String pattern, double value ) {
       DecimalFormat myFormatter = new DecimalFormat(pattern);
       return myFormatter.format(value);
   }

   /**
    * Splits a sentence into words using a stricter tokenization strategy.
    *
    * @param sentence the source sentence
    * @param minWordLength the minimum word length to keep
    * @return the extracted words in their original order
    */
   public static List<String> splitStringHard(String sentence, int minWordLength){
        String[] arr = sentence.split("([0-9])|([\\s])|(\\.)|(,)|(\\()|(\\))|(-)|(_)|(\\?)|(!)|(:)|(;)|(&)");
        List<String> list = new ArrayList<>();
        for (String word : arr){
            if (word.length()>=minWordLength){
                list.add(word.toLowerCase().replace('ё','е'));
            }
        }
        return list;
    }

    /**
     * Splits a sentence into words using a lighter tokenization strategy.
     *
     * @param sentence the source sentence
     * @param wordLength the minimum word length to keep
     * @return the extracted and normalized words
     */
    public static List<String> splitStringLight(String sentence, int wordLength){
        String[] arr = sentence.split("([\\s])|(\\()|(\\))|(\\?)|(!)|(:)|(;)|(&)");
        List<String> list = new ArrayList<>();
        for (String word : arr){
            if (word.length()>=wordLength){
                list.add(word.toLowerCase().replace('ё','е'));
            }
        }
        Collections.sort(list);
        return removeDuplications(list);
    }

    /**
     * Removes consecutive duplicate words from a list.
     *
     * @param list the list to normalize
     * @return the normalized list without duplicates
     */
    private static List<String> removeDuplications(List<String> list){
        Iterator<String> iterator = list.iterator();
        String last = null;
        while (iterator.hasNext()){
           String current = iterator.next();
           if (current.equals(last)){
               iterator.remove();
           }else {
               last = current;
           }
        }
        return list;
    }

    /**
     * Converts an array of strings into a single space-delimited string.
     *
     * @param strings the array to join
     * @return the joined string
     */
    public static String getArrayAsString(String[] strings){
        StringBuilder sb = new StringBuilder();
        for (String s : strings){
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * Checks whether a string is {@code null}, empty, or whitespace-only.
     *
     * @param string the string to inspect
     * @return {@code true} when the string is effectively empty
     */
    public static boolean stringIsEmpty(String string){
        if (string == null) return true;
        if (string.isEmpty()) return true;
        String stringTrimmed = string.replaceAll("([\\s])","");
        return stringTrimmed.equals("");
    }

    /**
     * Splits a long string into rows of a fixed length.
     *
     * @param string the string to split
     * @param rowLength the maximum row length
     * @return a list of row strings
     */
    public static List<String> splitStringInRows(String string, int rowLength){
        List<String> result = new ArrayList<>();
        int counter = string.length()/rowLength;
        for (int i = 0; i <counter; i++){
            result.add(string.substring(i*rowLength,(i+1)*rowLength));
        }
        result.add(string.substring(counter*rowLength));
        return result;
    }

    /**
     * Returns a shortened display path for a file.
     *
     * @param filePath the full file path
     * @return a shortened path representation
     */
    public static String getShortFilePath(String filePath){
        return filePath.substring(0,3) + "..." + filePath.substring(filePath.lastIndexOf('\\'));
    }

    /**
     * Calculates a font size based on the window height.
     *
     * @param height the window height
     * @return the recommended text size
     */
    public static double getTextSize(Double height){
        double defaultSize = 8.0; //default size
        double sizeByHeight = 0.02 * height;
        return sizeByHeight > defaultSize ? sizeByHeight : defaultSize ;
    }

}

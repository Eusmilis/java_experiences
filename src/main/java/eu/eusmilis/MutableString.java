/**
 * Paquet de définition
 **/
package eu.eusmilis;

import java.lang.reflect.Field;

/**
 * Description: Merci de donner une description du service rendu par cette classe
 */
public class MutableString {
  public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
    String str1 = "immutable";
    String str2 = "ou pas ??";
    Class strClass = String.class;
    Field strValueField = strClass.getDeclaredField("value");
    strValueField.setAccessible(true);
    char value[]= (char[])strValueField.get(str1);
    for(int i = 0 ; i < value.length ; i++) {
      char c = str2.toCharArray()[i];
      value[i] = c;
    }
    System.out.println(str1);
    System.out.println("immutable"); // Vive la StringPool
  }
}
 

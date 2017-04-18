/**
 * Paquet de définition
 **/
package eu.eusmilis;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Description: Merci de donner une description du service rendu par cette classe
 */
public class IntegerCacheEntropy {
  public static void main(String[] args) throws Exception {
    Class clazz = Class.forName("java.lang.Integer$IntegerCache");
    Field cacheField = clazz.getDeclaredField("cache");
    cacheField.setAccessible(true);
    Integer[] cache = (Integer[])cacheField.get(clazz);
    for(int i = 0 ; i < cache.length ; i++) {
      cache[i] = new Integer(new Random().nextInt(cache.length));
    }
    for(int i = 0 ; i < 10 ; i++) {
      System.out.println((Integer)i);
    }
  }
}
 

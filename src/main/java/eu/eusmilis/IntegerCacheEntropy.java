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
  private static void fuckUpIntegers() throws Exception {
    Class clazz = Class.forName("java.lang.Integer$IntegerCache");
    Field cacheField = clazz.getDeclaredField("cache");
    cacheField.setAccessible(true);
    Integer[] cache = (Integer[])cacheField.get(clazz);
    for(int i = 0 ; i < cache.length ; i++) {
      cache[i] = new Integer(new Random().nextInt(cache.length));
    }
    // Set some values giving interesting results in the test
    cache[2 + 128] = new Integer(27);
    cache[3 + 128] = new Integer(12);
    cache[4 + 128] = new Integer(2);
    cache[5 + 128] = new Integer(42);
  }

  private static void testFuckedUpIntegers() {
    Integer two = 2;
    Integer three = 3;
    Integer four = 4;
    Integer five = 5;

    System.out.println("two : " + two);
    System.out.println("three : " + three);
    System.out.println("four : " + four);
    System.out.println("five : " + five);

    int twoTimesTwoInt = two * two;
    Integer twoTimesTwoInteger = two * two;
    int twoTimesThreeInt = two * three;
    Integer twoTimesThreeInteger = two * three;
    int funfInt = 2+3;
    Integer funfInteger = 2+3;
    int threeTimesFourInt = three * four;
    Integer threeTimesFourInteger = three * four;

    System.out.println("twoTimesTwoInt : " + twoTimesTwoInt);
    System.out.println("twoTimesTwoInteger : " + twoTimesTwoInteger);
    System.out.println("twoTimesThreeInt : " + twoTimesThreeInt);
    System.out.println("twoTimesThreeInteger : " + twoTimesThreeInteger);
    System.out.println("funfInt : " + funfInt);
    System.out.println("funfInteger : " + funfInteger);
    System.out.println("threeTimesFourInt : " + threeTimesFourInt);
    System.out.println("threeTimesFourInteger : " + threeTimesFourInteger);
  }

  public static void main(String[] args) throws Exception {
    fuckUpIntegers();
    testFuckedUpIntegers();
  }
}
 

/**
 * Paquet de définition
 **/
package eu.eusmilis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description: Merci de donner une description du service rendu par cette classe
 */
public class DuckTyping {
  static class Parente {
  }

  static class Enfant extends Parente {
  }

  static class Class1 {
    public String getClassName() {
      return "Class1";
    }
    public String getIdentity(String str) {
      return str;
    }
    public String getClassOfArg(Parente parente) {
      return parente.getClass().getName();
    }
    public String getClassOfArgs(Parente parente, Object object) {
      return parente.getClass().getName() + " - " + object.getClass().getName();
    }
  }

  static class Class2 {
    public String getClassName() {
      return "Class2";
    }
    public String getIdentity(String str) {
      return str;
    }
  }

  static class ListListIterator<K> implements Iterator<List<K>> {
    List<List<K>> list;
    List<Iterator<K>> iteratorList = new ArrayList<Iterator<K>>();
    List<K> currentRes;

    public ListListIterator(List<List<K>> l) {
      list = l;
      for(List<K> subList:list) {
        iteratorList.add(subList.iterator());
      }
      // Initialisation du resultat
      currentRes = new ArrayList<K>();
      for(Iterator<K> it:iteratorList) {
        currentRes.add(it.next());
      }
    }

    @Override
    public boolean hasNext() {
      boolean res = false;
      for(Iterator it:iteratorList) {
        res = res || it.hasNext();
      }
      return res;
    }

    @Override
    public List<K> next() {
      // Création du résultat pour cet appel
      List<K> res = new ArrayList<K>(currentRes);

        /* Construction du prochain résultat */
      // Vérification du hasNext
      if(!hasNext()) {
        throw new RuntimeException();
      }
      // Réinitialisation des itérateurs consommés et mise à jour du résultat
      for(int i=0;i < currentRes.size();i++) {
        if(!iteratorList.get(i).hasNext()) {
          iteratorList.set(i, list.get(i).iterator());
          currentRes.set(i, iteratorList.get(i).next());
        } else {
          currentRes.set(i, iteratorList.get(i).next());
          break;
        }
      }

      return res;
    }

    @Override
    public void remove() {

    }
  }

  private static <T> T invokeMethod(String methodName, Object instance, Object...args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class clazz = instance.getClass();
    // Construction de la liste des types d'arguments
    List<Class> argumentTypesList = new ArrayList<Class>();
    for(Object arg:args) {
      argumentTypesList.add(arg.getClass());
    }
    Class[] classArray = new Class[argumentTypesList.size()];
    Method method = clazz.getMethod(methodName, argumentTypesList.toArray(classArray));

    return (T)method.invoke(instance, args);
  }

  private static <T> T invokeMethodWithArgumentInheritance(String methodName, Object instance, Object...args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class clazz = instance.getClass();
    // Construction de la liste des types d'arguments
    List<Class> argumentTypesList = new ArrayList<Class>();
    for(Object arg:args) {
      argumentTypesList.add(arg.getClass());
    }
    // Recherche des classes et interfaces parentes des arguments
    List<List<Class>> argumentTypesInheritance = new ArrayList<List<Class>>();
    for(Class argClass:argumentTypesList) {
      List<Class> argTypeInheritance = new ArrayList<Class>();
      argTypeInheritance.add(argClass);
      // Héritage
      Class currentClass = argClass;
      while(!currentClass.equals(Object.class)) {
        currentClass = currentClass.getSuperclass();
        argTypeInheritance.add(currentClass);
      }
      // TODO : Interfaces
      argumentTypesInheritance.add(argTypeInheritance);
    }
    // Recherche de la méthode
    Method method = null;
    ListListIterator<Class> argTypesIterator = new ListListIterator<Class>(argumentTypesInheritance);
    while(argTypesIterator.hasNext()) {
      Class[] argTypes = new Class[argumentTypesInheritance.size()];
      argTypes = argTypesIterator.next().toArray(argTypes);
      try {
        method = clazz.getMethod(methodName, argTypes);
        break;
      } catch (NoSuchMethodException e) {
        // Ignorée
      }
    }
    if(method == null) {
      throw new NoSuchMethodException();
    }

    return (T)method.invoke(instance, args);
  }

  public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    Class1 instance1 = new Class1();
    Class2 instance2 = new Class2();

    System.out.println("==================== getClassName ====================");
    String res1 = invokeMethod("getClassName", instance1);
    String res2 = invokeMethod("getClassName", instance2);
    System.out.println(res1);
    System.out.println(res2);
    System.out.println("==================== getIdentity ====================");
    String id1 = invokeMethod("getIdentity", instance1, "identity");
    String id2 = invokeMethod("getIdentity", instance2, "identity");
    System.out.println(id1);
    System.out.println(id2);

    System.out.println("==================== Test héritage ====================");
    try {
      Enfant e = new Enfant();
      String className = invokeMethod("getClassOfArg", instance1, e);
      System.out.println(className);
    } catch(Exception ex) {
      System.out.println("Erreur : " + ex);
    }
    System.out.println("==================== Test héritage cast ====================");
    try {
      Enfant e = new Enfant();
      String className = invokeMethod("getClassOfArg", instance1, (Parente)e);
      System.out.println(className);
    } catch(Exception ex) {
      System.out.println("Erreur : " + ex);
    }
    System.out.println("==================== Test héritage qui ne va jamais marcher ====================");
    try {
      Enfant e = new Enfant();
      String className = invokeMethodWithArgumentInheritance("getClassOfArg", instance1, (Parente) e);
      System.out.println(className);
      className = invokeMethodWithArgumentInheritance("getClassOfArgs", instance1, (Parente) e, "Tagada");
      System.out.println(className);
      System.out.println("Contre toute attente, ça a marché !");
    } catch(Exception ex) {
      System.out.println("Erreur : " + ex);
    }
  }
}
 

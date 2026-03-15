package it.jugtorino.chalk;

import com.google.common.base.Predicate;
import org.hamcrest.Matcher;

public class Predicates {

  private Predicates() {
    // Private constructor for utility class
  }

  public static <T> boolean checkThat(T actual, Predicate<? super T> predicate) {
    return predicate.apply(actual);
  }

  public static <T> Predicate<T> predicate(final Matcher<T> matcher) {
    return new Predicate<T>() {
      @Override
      public boolean apply(T item) {
        return matcher.matches(item);
      }
    };
  }
}

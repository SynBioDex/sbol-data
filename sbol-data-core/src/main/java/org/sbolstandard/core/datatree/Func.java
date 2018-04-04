package org.sbolstandard.core.datatree;

/**
* A pure function from F to R.
 *
 * <p>
 * Implement to encapsulate some code as a function.
 * </p>
 *
 * @author Matthew Pocock
*/
public interface Func<F, R> {
  /**
   * Apply this function
   *
   * @param f   the input value
   * @return    the result
   */
  public R apply(F f);
}

package uk.ac.ncl.intbio.core.datatree;

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

    public static final class impl {
        public static <F> Func<F, String> _toString() {
            return new Func<F, String>() {
                @Override
                public String apply(F f) {
                    return f.toString();
                }
            };
        }
    }
}

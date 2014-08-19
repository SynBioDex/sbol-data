package uk.ac.ncl.intbio.core.datatree;

/**
* A pure function from F to R.
 *
 * @author Matthew Pocock
*/
public interface Func<F, R> {
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

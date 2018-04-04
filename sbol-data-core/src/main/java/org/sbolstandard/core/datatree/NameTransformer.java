package org.sbolstandard.core.datatree;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform a datatree into a new one, rewriting all names.
 *
 * <p>
 *   To use this, subclass and implement {@link #transformName(Object)}.
 * </p>
 *
 * <p>
 *   This is intended for advanced users. In most cases, there will be canned implementations provided by libraries
 *   where needed.
 * </p>
 *
 * @author Matthew Pocock
 * @param <From>    the original name type
 * @param <To>      the new name type
 */
public abstract class NameTransformer<From, To> {
  /**
   * The name transformation function.
   *
   * @param f name to transform
   * @return  the transformed name
   */
  public abstract To transformName(From f);

  public DocumentRoot<To> mapDR(DocumentRoot<From> f) {
    return Datatree.DocumentRoot(
            Datatree.NamespaceBindings(f.getNamespaceBindings()),
            Datatree.TopLevelDocuments(mapTLDs(f.getTopLevelDocuments())));
  }

  public List<TopLevelDocument<To>> mapTLDs(List<TopLevelDocument<From>> fs) {
    return mapL(fs, new Func<TopLevelDocument<From>, TopLevelDocument<To>>() {
      public TopLevelDocument<To> apply(TopLevelDocument<From> f) {
        return mapTLD(f);
      }
    });
  }

  public TopLevelDocument<To> mapTLD(TopLevelDocument<From> f) {
    return Datatree.TopLevelDocument(
            transformName(f.getType()),
            f.getIdentity(),
            Datatree.NamedProperties(mapVPs(f.getProperties())));
  }

  public List<NamedProperty<To>> mapVPs(List<NamedProperty<From>> fs) {
    return mapL(fs, new Func<NamedProperty<From>, NamedProperty<To>>() {
      @Override
      @SuppressWarnings("unchecked")
      public NamedProperty<To> apply(NamedProperty<From> f) {
        return mapVP(f);
      }
    });
  }

  public NamedProperty<To> mapVP(NamedProperty<From> f) {
    return Datatree.NamedProperty(transformName(f.getName()), mapV(f.getValue()));
  }

  public PropertyValue<To> mapV(PropertyValue<From> f) {
    if(f instanceof NestedDocument) {
      return mapND((NestedDocument<From>) f);
    } else {
      return (Literal<To>) f; // a naughty little cast
    }
  }

  public NestedDocument<To> mapND(NestedDocument<From> f) {
    return Datatree.NestedDocument(
            transformName(f.getType()),
            f.getIdentity(),
            Datatree.NamedProperties(mapVPs(f.getProperties())));
  }


  public <A, B> List<B> mapL(List<A> as, Func<A, B> f) {
    final List<B> bs = new ArrayList<>(as.size());

    for(A a: as) {
      bs.add(f.apply(a));
    }

    return bs;
  }

}

package uk.ac.ncl.intbio.core.datatree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Pocock
 */
public abstract class NameTransformer<From, To> {
  public abstract To transformName(From f);

  public DocumentRoot<To> mapDR(DocumentRoot<From> f) {
    return Datatree.DocumentRoot(
            Datatree.NamespaceBindings(f.getNamespaceBindings()),
            Datatree.TopLevelDocuments(mapTLDs(f.getTopLevelDocuments())),
            Datatree.LiteralProperties(mapLPs(f.getProperties())));
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


  public List<NamedProperty<To, Literal>> mapLPs(List<NamedProperty<From, Literal>> fs) {
    return mapL(fs, new Func<NamedProperty<From, Literal>, NamedProperty<To, Literal>>() {
      @Override
      public NamedProperty<To, Literal> apply(NamedProperty<From, Literal> f) {
        return mapLP(f);
      }
    });
  }

  public NamedProperty<To, Literal> mapLP(NamedProperty<From, Literal> f) {
    return Datatree.NamedLiteralProperty(transformName(f.getName()), f.getValue());
  }


  public List<NamedProperty<To, PropertyValue>> mapVPs(List<NamedProperty<From, PropertyValue>> fs) {
    return mapL(fs, new Func<NamedProperty<From, PropertyValue>, NamedProperty<To, PropertyValue>>() {
      @Override
      @SuppressWarnings("unchecked")
      public NamedProperty<To, PropertyValue> apply(NamedProperty<From, PropertyValue> f) {
        return mapVP(f);
      }
    });
  }

  public NamedProperty<To, PropertyValue> mapVP(NamedProperty<From, PropertyValue> f) {
    return Datatree.NamedProperty(transformName(f.getName()), mapV(f.getValue()));
  }

  public PropertyValue mapV(PropertyValue f) {
    if(f instanceof NestedDocument) {
      return mapND((NestedDocument<From>) f);
    } else {
      return f;
    }
  }

  private NestedDocument<To> mapND(NestedDocument<From> f) {
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

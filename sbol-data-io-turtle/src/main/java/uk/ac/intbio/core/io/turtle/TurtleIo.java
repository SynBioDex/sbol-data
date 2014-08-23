package uk.ac.intbio.core.io.turtle;

import uk.ac.ncl.intbio.core.datatree.*;
import uk.ac.ncl.intbio.core.io.CoreIoException;
import uk.ac.ncl.intbio.core.io.IoReader;
import uk.ac.ncl.intbio.core.io.IoWriter;
import static uk.ac.ncl.intbio.core.datatree.Datatree.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by caroline on 20/08/2014.
 */
public class TurtleIo {
  public IoWriter<QName> createIoWriter(final PrintWriter writer) {
    return new IoWriter<QName>() {
      private void writeIndent() {
        for(int i = 0; i < depth; i++) {
          writer.print(anIndent);
        }
      }
      private int depth = 0;
      private String anIndent = "  ";
      private void pushIndent() {
        depth = depth + 1;
      }
      private void popIndent() {
        depth = depth - 1;
      }

      private Deque<List<NamespaceBinding>> bindingStack = new LinkedList<>();
      private void pushBindings(List<NamespaceBinding> bindings) {
        bindingStack.push(bindings);
      }
      private void popBindings() {
        bindingStack.pop();
      }

      @Override
      public void write(DocumentRoot<QName> document) throws CoreIoException {
        write(document.getNamespaceBindings());
        pushBindings(document.getNamespaceBindings());

        for (TopLevelDocument<QName> doc : document.getTopLevelDocuments()) {
          write(doc);
        }

        popBindings();
      }

      private void write(IdentifiableDocument<QName> doc) {
        writeURI(doc.getIdentity());
        writer.println();
        pushIndent();
        write(doc.getNamespaceBindings());
        pushBindings(doc.getNamespaceBindings());
        writeType(doc.getType());

        for (NamedProperty<QName> prop : doc.getProperties()) {
          writer.println(";");
          writeIndent();
          writeQName(prop.getName());
          writer.print(" ");
          write(prop.getValue());
        }

        writer.println(".");
        popIndent();
        popBindings();
      }

      private void write(PropertyValue<QName> value) {
        new PropertyValue.Visitor<QName>() {
          @Override
          public void visit(NestedDocument<QName> v) throws Exception {
            writer.println("{");
            pushIndent();
            writeIndent();
            write((IdentifiableDocument<QName>) v);
            popIndent();
            writeIndent();
            writer.print("} ");
          }

          @Override
          public void visit(Literal<QName> v) throws Exception {
            write(v);
          }
        }.visit(value);
      }

      private void write(Literal<QName> literal) {
        new Literal.Visitor<QName>() {
          @Override
          public void visit(Literal.StringLiteral<QName> l) throws Exception {
            writer.print("\"");
            writer.print(l.getValue());
            writer.print("\"");
          }

          @Override
          public void visit(Literal.UriLiteral<QName> l) throws Exception {
            writeURI(l.getValue());
          }

          @Override
          public void visit(Literal.IntegerLiteral<QName> l) throws Exception {
            writer.write(l.getValue().toString());
          }

          @Override
          public void visit(Literal.DoubleLiteral<QName> l) throws Exception {
            writer.write(l.getValue().toString());
          }

          @Override
          public void visit(Literal.TypedLiteral<QName> l) throws Exception {
            writer.write(l.getValue() + "^^" + l.getType().getPrefix() + ":" + l.getType().getLocalPart());
          }

          @Override
          public void visit(Literal.BooleanLiteral<QName> l) throws Exception {
            writer.write(l.getValue().toString());
          }
        }.visit(literal);
        writer.print(" ");
      }

      private void write(List<NamespaceBinding> namespaceBindings) {
        for(NamespaceBinding nb : namespaceBindings) {
          writeIndent();
          writer.print("@prefix ");
          writer.print(nb.getPrefix());
          writer.print(": ");
          writeURI(nb.getNamespaceURI());
          writer.println(" .");
        }
      }

      private void writeURI(URI uri) {
        writeURI(uri.toString());
      }

      private void writeURI(String uri) {
        for(List<NamespaceBinding> nss : bindingStack) {
          for(NamespaceBinding ns : nss) {
            if (uri.startsWith(ns.getNamespaceURI())) {
              writer.print(ns.getPrefix());
              writer.print(":");
              writer.print(uri.substring(ns.getNamespaceURI().length()));

              return;
            }
          }
        }

        writer.print("<");
        writer.print(uri);
        writer.print(">");
      }

      private void writeQName(QName q) {
        writer.print(q.getPrefix() + ":" + q.getLocalPart());
      }

      private void writeType(QName type) {
        writeIndent();
        writer.print("a ");
        writeQName(type);
        writer.print(" ");
      }
    };
  }

  public IoReader<QName> createIoReader(final Reader reader) {
    final Pattern spaces = Pattern.compile("\\s+");

    final Pattern stop = Pattern.compile("\\.");
    final Pattern semicolon = Pattern.compile(";");
    final Pattern open_bracket = Pattern.compile("\\{");
    final Pattern close_bracket = Pattern.compile("\\}");

    final Pattern a = Pattern.compile("a");

    final Pattern trueV = Pattern.compile("true");
    final Pattern falseV = Pattern.compile("false");
    final Pattern prefix_keyword = Pattern.compile("@prefix");
    final Pattern prefix_colon = Pattern.compile("\\S*:");

    final Pattern quoted_uri = Pattern.compile("<\\S*>");
    final Pattern prefixed_uri = Pattern.compile("\\w*:\\S+");
    final Pattern dblQuoted = Pattern.compile("\"[^\"]*\"");

    final Pattern itg = Pattern.compile("[+-]?\\d+");
    final Pattern dbl = Pattern.compile("[+-]?\\d+\\.\\d*(\\e[+-]\\d*)?");
    final Pattern typed = Pattern.compile("\"[^\"]*\"^^\\S+");

    final WSScanner scanner = new WSScanner(reader);

    return new IoReader<QName>() {
      private Deque<List<NamespaceBinding>> bindingStack = new LinkedList<>();

      private void pushBindings(List<NamespaceBinding> bindings) {
        bindingStack.push(bindings);
      }

      private void popBindings() {
        bindingStack.pop();
      }

      private NamespaceBinding resolveByPrefix(String pfx) {
        for (List<NamespaceBinding> nbs : bindingStack) {
          for (NamespaceBinding nb : nbs) {
            if (nb.getPrefix().equals(pfx)) {
              return nb;
            }
          }
        }

        return null;
      }

      @Override
      public DocumentRoot<QName> read() throws CoreIoException {
        NamespaceBindings bindings = readBindings();
        pushBindings(bindings.getBindings());
        TopLevelDocuments<QName> docs = readTLDs();
        popBindings();

        return DocumentRoot(
                bindings,
                docs
        );
      }

      private TopLevelDocuments<QName> readTLDs() {
        List<TopLevelDocument<QName>> docs = new ArrayList<>();

        while (scanner.hasNext()) {
          URI identity = readPfxURI();
          NamespaceBindings bindings = readBindings();
          pushBindings(bindings.getBindings());

          List<NamedProperty<QName>> properties = new ArrayList<>();
          QName type = readType();
          while (scanner.hasNext(semicolon)) {
            scanner.next(); // discard `;`
            properties.add(readProperty());
          }
          if (scanner.hasNext(stop)) {
            scanner.next(); // discard `.`
          } else {
            throw new IllegalStateException("Expecting `.` but got `" + scanner.next() + "`");
          }
          docs.add(TopLevelDocument(bindings, type, identity, NamedProperties(properties)));

          popBindings();
        }

        return TopLevelDocuments(docs);
      }

      private NestedDocument<QName> readND() {
        URI identity = readPfxURI();
        if (identity == null) {
          throw new IllegalStateException("Expecting identity.");
        }
        NamespaceBindings bindings = readBindings();
        pushBindings(bindings.getBindings());

        List<NamedProperty<QName>> properties = new ArrayList<>();
        QName type = readType();
        while (scanner.hasNext(semicolon)) {
          scanner.next(); // discard `;`
          properties.add(readProperty());
        }
        if (scanner.hasNext(stop)) {
          scanner.next(); // discard `.`
        } else {
          throw new IllegalStateException("Expecting `.` but got `" + scanner.next() + "`");
        }

        popBindings();

        return NestedDocument(bindings, type, identity, NamedProperties(properties));
      }

      private NamedProperty<QName> readProperty() {
        QName name = readQName();
        if(scanner.hasNext(open_bracket)) {
          scanner.next(); // consume `{`
          NestedDocument<QName> nd = readND();
          if(scanner.hasNext(close_bracket)) {
            scanner.next(); // consume `}`
            return NamedProperty(name, nd);
          } else {
            throw new IllegalStateException("Expection `}` but got `" + scanner.next() + "`");
          }
        } else {
          return NamedProperty(name, readLiteral());
        }
      }

      private Literal<QName> readLiteral() {
        if(scanner.hasNext(quoted_uri)) {
          return Literal(readQtdURI());
        } else if(scanner.hasNext(prefixed_uri)) {
          return Literal(readPfxURI());
        } else if(scanner.hasNext(dblQuoted)) {
          String dqs = scanner.next();
          return Literal(dqs.substring(1, dqs.length()-1));
        } else if(scanner.hasNext(itg)) {
           return Literal(Integer.parseInt(scanner.next()));
        } else if(scanner.hasNext(dbl)) {
          return Literal(Double.parseDouble(scanner.next()));
        } else if(scanner.hasNext(typed)) {
          String[] value_type = scanner.next().split("\\^\\^");
          String[] pfx_lcl = value_type[1].split(":", 1);
          return Literal(value_type[0], QName(null, pfx_lcl[1], pfx_lcl[0]));
        } else if(scanner.hasNext(trueV)) {
          return Literal(true);
        } else if(scanner.hasNext(falseV)) {
          return Literal(false);
        } else {
          throw new IllegalStateException("Unknown literal `" + scanner.next() + "`");
        }
      }

      private QName readType() {
        scanner.next(a);
        return readQName();
      }

      private URI readQtdURI() {
        String quri = scanner.next(quoted_uri);
        return URI.create(quri.substring(1, quri.length()-1));
      }

      private URI readPfxURI() {
        String[] pfx_lcl = scanner.next(prefixed_uri).split(":");
        NamespaceBinding binding = resolveByPrefix(pfx_lcl[0]);
        if(binding == null) {
          throw new IllegalStateException("Unable to resolve prefix `" + pfx_lcl[0] + "` to a URI.");
        }

        return URI.create(binding.getNamespaceURI() + pfx_lcl[1]);
      }

      private QName readQName() {
        String[] pfx_lcl = scanner.next(prefixed_uri).split(":");
        NamespaceBinding binding = resolveByPrefix(pfx_lcl[0]);
        if(binding == null) {
          throw new IllegalStateException("Unable to resolve prefix `" + pfx_lcl[0] + "` to a URI.");
        }

        return binding.withLocalPart(pfx_lcl[1]);
      }

      private NamespaceBindings readBindings() {
        List<NamespaceBinding> nss = new ArrayList<>();
        while(scanner.hasNext(prefix_keyword)) {
          scanner.next(); // discard `@prefix`
          String pfx_colon = scanner.next(prefix_colon);
          String pfx = pfx_colon.substring(0, pfx_colon.length() - 1);
          String quotedUri = scanner.next(quoted_uri);
          String uri = quotedUri.substring(1, quotedUri.length() - 1);
          scanner.next(stop); // discard `.`
          nss.add(NamespaceBinding(uri, pfx));
        }
        return NamespaceBindings(nss);
      }
    };
  }

  private static final class WSScanner {
    private final Reader reader;

    private String nextToken;
    private StringBuilder tokenBuilder;

    {
      this.nextToken = null;
      this.tokenBuilder = new StringBuilder();
    }

    public WSScanner(Reader reader) {
      this.reader = reader;
    }

    private boolean readNextIfNeeded() {
      if(nextToken != null) {
        return true;
      }

      return readNextToken();
    }

    private boolean readNextToken() {
      boolean inQuotes = false;

      while(true) {
        int ni = 0;
        try {
          ni = reader.read();
        } catch (IOException e) {
          throw new IllegalStateException("Not expecting an IO exception", e);
        }
        if(ni < 0) {
          if(tokenBuilder.length() == 0) {
            nextToken = null;
            return false;
          } else {
            String nt = tokenBuilder.toString();
            nextToken = nt;
            return true;
          }
        } else {
          char nc = (char) ni;
          if(isWhitespace(nc) && !inQuotes) {
            if(tokenBuilder.length() == 0) {
              // eat leading whitespace
            } else {
              break; // get out of the loop - we are going to return a token
            }
          } else if(nc == '"') {
            tokenBuilder.append(nc);
            inQuotes = !inQuotes;
          } else {
            tokenBuilder.append(nc);
          }
        }
      }

      nextToken = tokenBuilder.toString();
      tokenBuilder.delete(0, tokenBuilder.length()); // empty the builder
      return true;
    }

    private boolean isWhitespace(char c) {
      switch (c) {
        case ' ':
        case '\t':
        case '\n':
        case '\r':
          return true;
        default:
          return false;
      }

    }

    public boolean hasNext() {
      readNextIfNeeded();
      return nextToken != null;
    }

    public boolean hasNext(Pattern p) {
      if(!readNextIfNeeded()) {
        return false;
      }
      return p.matcher(nextToken).matches();
    }

    public String next() {
      readNextIfNeeded();
      if(nextToken == null) {
        throw new NoSuchElementException("There is no next token");
      }
      String nt = nextToken;
      nextToken = null;
      return nt;
    }

    public String next(Pattern p) {
      if(!hasNext(p)) {
        if(nextToken == null) {
          throw new NoSuchElementException("No more tokens");
        } else {
          throw new IllegalArgumentException("Next token does not match expected pattern at `" + nextToken + "`");
        }
      }

      return next();
    }
  }
}

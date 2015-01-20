sbol-data
=========

> *APIs to manage SBOL-shaped data documents.*

## Introduction

SBOL is a data model and data exchange standard for synthetic biology.
It captures various information about the design and function of DNA, and how DNA has been enginered.
The native serialization is in RDF.
However, to support tools that don't have access to a full RDF stack, the serialization is highly constrained.
**sbol-data** provides a data model that exactly captures these constraints upon how the RDF is serialized.

Although designed for use with SBOL, the **sbol-data** data model is schema-agnostic.
It can be used to encode a wide range of data structures.

Through the *IO* providers framework, **sbol-data** supports a range of human- and machine-readable input and output
formats, including:

* RDF
* JSON
* Mock-Turtle (almost RDF-Turtle)
* Graphviz (export only)

## Overview

The **sbol-data** structures form a forest of trees. *DocumentRoot* is the top-level element, and it contains a list
of *TopLevelDocument* children.
Each of these is a self-contained data record.
These have a collection of named properties, which can have literal values (e.g. strings or ints), or can point to a
*NestedDocument* child.
These can also have named properties that may themselves point to nested documents, and so on.

Cyclic references are not allowed.
If you use the *Datatree* factory object, they are not possible.

Each document (TopLevelDocument or NestedDocument) has an associated URI that is its *identity*.
This uniquely identifies the document.
If one document needs to refer to another one, then this is done by having a named property were the value is a URI
referencing the other document.

## Use

```java
import uk.ac.ncl.intbio.core.datatree.*;
import static uk.ac.ncl.intbio.core.datatree.Datatree;
import uk.ac.ncl.intbio.examples.Sbol2Terms;

...

TopLevelDocument<QName> modelLacIInverter = TopLevelDocument(
  NamespaceBindings(utah),
  Sbol2Terms.model.model,
  sbolExample.namespacedUri("model/LacI_inverter"),
  NamedProperties(
    NamedProperty(Sbol2Terms.documented.name, "LacI Inverter Model"),
    NamedProperty(Sbol2Terms.model.source,URI.create("http://www.async.ece.utah.edu/LacI_Inverter.xml")),
    NamedProperty(Sbol2Terms.model.language,"SBML"),
    NamedProperty(Sbol2Terms.model.framework,"ODE"),
    NamedProperty(Sbol2Terms.model.role,"simulation")
  )
);

DocumentRoot<QName> root = DocumentRoot(
  NamespaceBindings(RdfTerms.rdf, SbolTerms.sbol2),
  TopLevelDocuments(moduleLacIInverter,modelLacIInverter),
  Datatree.LiteralProperties()
);

XMLStreamWriter xmlWriter = new IndentingXMLStreamWriter(
  XMLOutputFactory.newInstance().createXMLStreamWriter(
    System.out));
RdfIo rdfIo = new RdfIo();
rdfIo.createIoWriter(xmlWriter).write(root);
xmlWriter.close();
```

##Change Log
v0.1.0, 29/08/2014
Initial release

v0.1.1, 20/01/2015
Fixed the RDF prefix mapping to read  SBOL1 files 

package org.sbolstandard.examples;

import java.net.URI;

import javax.xml.namespace.QName;

import org.sbolstandard.core.datatree.DocumentRoot;
import org.sbolstandard.core.datatree.NamespaceBinding;
import org.sbolstandard.core.datatree.NestedDocument;
import org.sbolstandard.core.datatree.TopLevelDocument;
import org.sbolstandard.core.io.rdf.RdfTerms;
import org.sbolstandard.core.schema.SchemaCatalog;

import static org.sbolstandard.core.datatree.Datatree.*;
import static org.sbolstandard.core.schema.Schema.*;

/**
 * Provides methods to create example Datatrees.

 * @author goksel
 *
 */
public class DataTreeCreator {

  private static final NamespaceBinding partsRegistry = NamespaceBinding("http://partsregistry.org/", "pr");
  private static final NamespaceBinding sbolExample = NamespaceBinding("http://sbolstandard.org/example#", "example");
  private static final NamespaceBinding obo = NamespaceBinding("http://purl.obolibrary.org/obo/", "obo");
  private static final NamespaceBinding utah = NamespaceBinding("http://www.async.ece.utah.edu/", "utah");

//  private static String dctermsNS = "http://purl.org/dc/terms/";
//  private static String dctermsPF = "dcterms";

  /**
   * Creates an example {@link DocumentRoot} object with data from the SBOL2.0 proposed data model.
   * Included SBOL objects are Module, Interaction, Participation, ComponentInstantiation and Model.
   * @return {@link DocumentRoot}
   */
  public static DocumentRoot<QName> makeSBOL2Document()
  {
    NestedDocument<QName> instantiationLacI=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,
            sbolExample.namespacedUri("module_LacI_inverter/LacI_instantiation"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "LacI")
            ));

    NestedDocument<QName> instantiationIPTG=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,

            sbolExample.namespacedUri("module_LacI_inverter/IPTG"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "IPTG")
            ));

    NestedDocument<QName> instantiationIPTGLacI=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,
            sbolExample.namespacedUri("module_LacI_inverter/IPTG_LacI_complex"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "IPTG LacI complex")
            ));
    NestedDocument<QName> instantiationpLac=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,

            sbolExample.namespacedUri("module_LacI_inverter/pLac_instantiation"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "pLac promoter")
            ));
    NestedDocument<QName> instantiationcTetR=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,

            sbolExample.namespacedUri("module_LacI_inverter/cTetR_instantiation"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "cTetR")
            ));
    NestedDocument<QName> instantiationTetR=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,

            sbolExample.namespacedUri("module_LacI_inverter/TetR_instantiation"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "TetR")
            ));


    NestedDocument<QName> interactionIPTGBinding=NestedDocument(
            Sbol2Terms.module.interaction,

            sbolExample.namespacedUri("module_LacI_inverter/interaction/IPTG_binding"),

            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "IPTG Binding"),
                    NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/non_covalent_binding")),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/IPTG_Binding/LacI_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role,URI.create("http://purl.obolibrary.org/obo/reactant")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationLacI.getIdentity())
                                    )
                            )
                    ),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/IPTG_Binding/IPTGLacI_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role, URI.create("http://purl.obolibrary.org/obo/product")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationIPTGLacI.getIdentity())
                                    )
                            )),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/IPTG_Binding/IPTG_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role, URI.create("http://purl.obolibrary.org/obo/reactant")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationIPTG.getIdentity())
                                    )
                            )
                    )
            )
    );



    NestedDocument<QName> interactionLacIRepression=NestedDocument(
            Sbol2Terms.module.interaction,
            sbolExample.namespacedUri("module_LacI_inverter/interaction/LacI_repression"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "LacI Repression"),
                    NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/repression")),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/LacI_repression/LacI"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role,URI.create("http://purl.obolibrary.org/obo/repressor")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationLacI.getIdentity())
                                    )
                            )
                    ),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/LacI_repression/pLac"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role, URI.create("http://purl.obolibrary.org/obo/repressed")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationpLac.getIdentity())
                                    )
                            )
                    )
            )
    );


    NestedDocument<QName> interactionTetRTranscriptionTranslation=NestedDocument(
            Sbol2Terms.module.interaction,

            sbolExample.namespacedUri("module_LacI_inverter/interaction/TetR_transcription_translation"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "TetR Transcription Translation"),
                    NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/genetic_production")),
                    NamedProperty(RdfTerms.rdfType, URI.create("http://made.up.terms.org/unicorns")),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/TetR_transcription_translation/TetR_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role,URI.create("http://purl.obolibrary.org/obo/product")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationTetR.getIdentity())
                                    )
                            )
                    ),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/TetR_transcription_translation/cTetR_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role, URI.create("http://purl.obolibrary.org/obo/transcribed")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationcTetR.getIdentity())
                                    )
                            )
                    ),
                    NamedProperty(Sbol2Terms.module.hasParticipation,
                            NestedDocument(
                                    Sbol2Terms.module.participation,
                                    partsRegistry.namespacedUri("module_LacI_inverter/interaction/TetR_transcription_translation/pLac_participation"),
                                    NamedProperties(
                                            NamedProperty(Sbol2Terms.module.role, URI.create("http://purl.obolibrary.org/obo/modifier")),
                                            NamedProperty(Sbol2Terms.module.participant, instantiationpLac.getIdentity())
                                    )
                            )
                    )
            )
    );



    TopLevelDocument<QName> modelLacIInverter=TopLevelDocument(
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

    TopLevelDocument<QName> moduleLacIInverter=TopLevelDocument(
            Sbol2Terms.module.module,
            sbolExample.namespacedUri("module/LacI_inverter"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "LacI Inverter"),
                    NamedProperty(Sbol2Terms.module.hasInteraction, interactionIPTGBinding),
                    NamedProperty(Sbol2Terms.module.hasInteraction, interactionLacIRepression),
                    NamedProperty(Sbol2Terms.module.hasInteraction, interactionTetRTranscriptionTranslation),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationLacI),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationIPTG),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationIPTGLacI),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationpLac),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationcTetR),
                    NamedProperty(Sbol2Terms.instantiation.hasComponentInstantiation, instantiationTetR),
                    NamedProperty(Sbol2Terms.module.hasModel,modelLacIInverter.getIdentity())
            )
    );

    return DocumentRoot(
            NamespaceBindings(RdfTerms.rdf, SbolTerms.sbol2, partsRegistry, sbolExample, obo),
            TopLevelDocuments(moduleLacIInverter,modelLacIInverter));
  }

  /**
   * Creates an example {@link DocumentRoot} object with data from the SBOL2.0 proposed data model.
   * Included SBOL objects from the data model are SequenceComponent, ComponentInstantiation, SequenceAnnotation and SequenceComponent.
   * @return {@link DocumentRoot}
   */
  public static DocumentRoot<QName> makeSBOL2SequenceComponent()
  {
    TopLevelDocument<QName> pLac=TopLevelDocument(
            Sbol2Terms.component.sequenceComponent,
            sbolExample.namespacedUri("sequenceComponent/pLac"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "pLac"),
                    NamedProperty(Sbol2Terms.documented.displayId, "pLac"),
                    NamedProperty(RdfTerms.rdfType, URI.create("DNA")),
                    NamedProperty(Sbol2Terms.component.sequenceType, URI.create("http://purl.org/obo/owl/SO#SO_0000167"))
            ));


    NestedDocument<QName> instantiationpLac=NestedDocument(
            Sbol2Terms.instantiation.componentInstantiation,
            sbolExample.namespacedUri("sequenceComponent/pLac/instantiation"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "pLac"),
                    NamedProperty(Sbol2Terms.component.component, pLac.getIdentity())
            ));

    NestedDocument<QName> pLacAnnotation=NestedDocument(
            Sbol2Terms.component.sequenceAnnotation,
            sbolExample.namespacedUri("sequenceComponent/UU_002/pLac_annotation"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.component.orientation, "inline"),
                    NamedProperty(Sbol2Terms.instantiation.subComponentInstantiation, instantiationpLac)
            ));


    TopLevelDocument<QName> lacIRepressibleGeneSequence=TopLevelDocument(
            Sbol2Terms.component.sequence,
            sbolExample.namespacedUri("sequenceComponent/UU_002/sequence"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.component.elements, "atg")
            ));

    TopLevelDocument<QName> lacIRepressibleGene=TopLevelDocument(
            Sbol2Terms.component.sequenceComponent,
            sbolExample.namespacedUri("sequenceComponent/UU_002"),
            NamedProperties(
                    NamedProperty(Sbol2Terms.documented.name, "LacI Repressible Gene"),
                    NamedProperty(Sbol2Terms.documented.displayId, "UU_002"),
                    NamedProperty(RdfTerms.rdfType, URI.create("DNA")),
                    NamedProperty(Sbol2Terms.component.sequenceType, URI.create("http://purl.org/obo/owl/SO#SO_0000774")),
                    NamedProperty(Sbol2Terms.component.annotation, pLacAnnotation),
                    NamedProperty(Sbol2Terms.component.hasSequence, lacIRepressibleGeneSequence.getIdentity())
            ));



    return DocumentRoot(
            NamespaceBindings(SbolTerms.sbol2),
            TopLevelDocuments(lacIRepressibleGene,pLac,lacIRepressibleGeneSequence)
    );
  }


  /**
   * Creates an example {@link DocumentRoot} object with data from the SBOL1.1 data model.
   * Included SBOL objects are DnaComponent, SequenceAnnotation and DnaSequence.
   * @return {@link DocumentRoot}
   */
  public static DocumentRoot<QName> makeDocument()
  {
    return DocumentRoot(
            NamespaceBindings(SbolTerms.sbol2),
            TopLevelDocuments(
                    TopLevelDocument(
                            SbolTerms.dnaComponent,
                            partsRegistry.namespacedUri("Part:BBa_I0462"),
                            NamedProperties(
                                    NamedProperty(SbolTerms.name, "I0462"),
                                    NamedProperty(SbolTerms.description, "LuxR protein generator"),
                                    NamedProperty(SbolTerms.dnaSequence, partsRegistry.namespacedUri("Part:BBa_I0462/sequence")),
                                    NamedProperty(
                                            SbolTerms.sequenceAnnotation,
                                            NestedDocument(
                                                    SbolTerms.annotation,
                                                    partsRegistry.namespacedUri("Part:BBa_I0462/anot/1234567"),
                                                    NamedProperties(
                                                            NamedProperty(SbolTerms.bioStart, 1),
                                                            NamedProperty(SbolTerms.bioEnd, 12),
                                                            NamedProperty(SbolTerms.strand, "+"),
                                                            NamedProperty(
                                                                    SbolTerms.subComponent,
                                                                    partsRegistry.namespacedUri("Part:BBa_B0034")
                                                            )
                                                    )
                                            )
                                    ),
                                    NamedProperty(
                                            SbolTerms.annotation,
                                            NestedDocument(
                                                    SbolTerms.sequenceAnnotation,
                                                    partsRegistry.namespacedUri("Part:BBa_I0462/annotation/2345678"),
                                                    NamedProperties(
                                                            NamedProperty(SbolTerms.bioStart, 19),
                                                            NamedProperty(SbolTerms.bioEnd, 774),
                                                            NamedProperty(SbolTerms.subComponent, partsRegistry.namespacedUri("Part:BBa_C0062"))
                                                    )
                                            )
                                    )
                            )
                    ),
                    TopLevelDocument(
                            SbolTerms.dnaSequence,
                            partsRegistry.namespacedUri("Part:BBa_I0462/sequence"),
                            NamedProperties(NamedProperty(
                                    SbolTerms.nucleotides,
                                    "aaagaggagaaatactagatgaaaaacataaatgccgacgacacatacagaataattaataaaattaaagcttgtagaagcaataatgatattaatcaatgcttatctgatatgactaaaatggtacattgtgaatattatttactcgcgatcatttatcctcattctatggttaaatctgatatttcaatcctagataattaccctaaaaaatggaggcaatattatgatgacgctaatttaataaaatatgatcctatagtagattattctaactccaatcattcaccaattaattggaatatatttgaaaacaatgctgtaaataaaaaatctccaaatgtaattaaagaagcgaaaacatcaggtcttatcactgggtttagtttccctattcatacggctaacaatggcttcggaatgcttagttttgcacattcagaaaaagacaactatatagatagtttatttttacatgcgtgtatgaacataccattaattgttccttctctagttgataattatcgaaaaataaatatagcaaataataaatcaaacaacgatttaaccaaaagagaaaaagaatgtttagcgtgggcatgcgaaggaaaaagctcttgggatatttcaaaaatattaggttgcagtgagcgtactgtcactttccatttaaccaatgcgcaaatgaaactcaatacaacaaaccgctgccaaagtatttctaaagcaattttaacaggagcaattgattgcccatactttaaaaattaataacactgatagtgctagtgtagatcactactagagccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata"
                            ))
                    ),

                    TopLevelDocument(
                            SbolTerms.dnaComponent,
                            partsRegistry.namespacedUri("Part:BBa_B0034"),
                            NamedProperties(
                                    NamedProperty(SbolTerms.name, "I0462"),
                                    NamedProperty(SbolTerms.displayId, "BBa_B0034"),
                                    NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/SO_0000139"))
                            )),
                    TopLevelDocument(
                            SbolTerms.dnaComponent,
                            partsRegistry.namespacedUri("Part:BBa_C0062"),
                            NamedProperties(
                                    NamedProperty(SbolTerms.name, "luxR"),
                                    NamedProperty(SbolTerms.displayId, "BBa_C0062"),
                                    NamedProperty(RdfTerms.rdfType, URI.create("http://purl.obolibrary.org/obo/SO_0000316"))
                            )
                    )
            ));
  }

  /**
   * Creates a {@link SchemaCatalog} for the SBOL20 core objects
   * @return {@link SchemaCatalog}
   */
  public static SchemaCatalog makeCoreSchemaCatalog()
  {
    return SchemaCatalog(
            Sbol2Terms.sbol2.namespacedUri("/schemaexample/core"),
            ImportedSchemas(),
            DocumentSchemas(
                    DocumentSchema(
                            Sbol2Terms.sbol2.namespacedUri("/schema/identified"),
                            Extends(),
                            IdentifierSchemas(),
                            TypeSchemas(),
                            PropertySchemas()
                    ),
                    DocumentSchema(
                            Sbol2Terms.sbol2.namespacedUri("/schema/documented"),
                            Extends(Sbol2Terms.sbol2.namespacedUri("/schema/identified")),
                            IdentifierSchemas(),
                            TypeSchemas(),
                            PropertySchemas(
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.documented.displayId)
                                            ),
                                            cardinality.required,
                                            PropertyValueSchemas(propertyType.string)
                                    ),
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.documented.name)
                                            ),
                                            cardinality.optional,
                                            PropertyValueSchemas(propertyType.string)
                                    ),
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.documented.description)
                                            ),
                                            cardinality.optional,
                                            PropertyValueSchemas(propertyType.string)
                                    )
                            )
                    )
            )
    );
  }


  /**
   * Creates a {@link SchemaCatalog} for the SBOL20 instantiation objects
   * @return {@link SchemaCatalog}
   */
  public static SchemaCatalog makeInstantiationSchemaCatalog()
  {
    return SchemaCatalog(
            Sbol2Terms.sbol2.namespacedUri("/schemaexample/instantiation"),
            ImportedSchemas(),
            DocumentSchemas(
                    DocumentSchema(
                            Sbol2Terms.sbol2.namespacedUri("/schema/component_instantiation"),
                            Extends(),
                            IdentifierSchemas(),
                            TypeSchemas(
                                    TypeSchema(Sbol2Terms.instantiation.componentInstantiation)
                            ),
                            PropertySchemas(
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.instantiation.hasComponentInstantiation)
                                            ),
                                            cardinality.required,
                                            PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/sequence_component")))
                                    )
                            )
                    )
            )
    );
  }


  /**
   * Creates a {@link SchemaCatalog} for the SBOL20 component objects
   * @return {@link SchemaCatalog}
   */
  public static SchemaCatalog makeComponentSchemaCatalog()
  {
    return SchemaCatalog(
            Sbol2Terms.sbol2.namespacedUri("/schemaexample/component"),
            ImportedSchemas(
                    Sbol2Terms.sbol2.namespacedUri("/schema/core"),
                    Sbol2Terms.sbol2.namespacedUri("/schema/instantiation")
            ),
            DocumentSchemas(
                    DocumentSchema(
                            Sbol2Terms.sbol2.namespacedUri("/schema/sequence"),
                            Extends(),
                            IdentifierSchemas(),
                            TypeSchemas(
                                    TypeSchema(Sbol2Terms.component.sequence)
                            ),
                            PropertySchemas(
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.component.elements)
                                            ),
                                            cardinality.required,
                                            PropertyValueSchemas(propertyType.string)
                                    )
                            )
                    ),
                    DocumentSchema(
                            Sbol2Terms.sbol2.namespacedUri("/schema/sequence_component"),
                            Extends(Sbol2Terms.sbol2.namespacedUri("/schema/documented")),
                            IdentifierSchemas(),
                            TypeSchemas(
                                    TypeSchema(Sbol2Terms.component.sequenceComponent)
                            ),
                            PropertySchemas(
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.component.hasSequence)
                                            ),
                                            cardinality.optional,
                                            PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/sequence")))
                                    ),
                                    PropertySchema(
                                            TypeSchemas(
                                                    TypeSchema(Sbol2Terms.component.annotation)
                                            ),
                                            cardinality.many,
                                            PropertyValueSchemas(
                                                    DocumentValue(
                                                            DocumentSchema(
                                                                    Sbol2Terms.sbol2.namespacedUri("/schema/sequence_annotation"),
                                                                    Extends(Sbol2Terms.sbol2.namespacedUri("/schema/documented")),
                                                                    IdentifierSchemas(),
                                                                    TypeSchemas(
                                                                            TypeSchema(Sbol2Terms.component.sequenceComponent)
                                                                    ),
                                                                    PropertySchemas(
                                                                            PropertySchema(
                                                                                    TypeSchemas(
                                                                                            TypeSchema(Sbol2Terms.instantiation.subComponentInstantiation)
                                                                                    ),
                                                                                    cardinality.required,
                                                                                    PropertyValueSchemas(ReferenceValue(Sbol2Terms.sbol2.namespacedUri("/schema/component_instantiation")))
                                                                            ),
                                                                            PropertySchema(
                                                                                    TypeSchemas(
                                                                                            TypeSchema(Sbol2Terms.component.orientation)
                                                                                    ),
                                                                                    cardinality.required,
                                                                                    PropertyValueSchemas(propertyType.oneOf("inline","reverse_compliment"))
                                                                            ),
                                                                            PropertySchema(
                                                                                    TypeSchemas(
                                                                                            TypeSchema(Sbol2Terms.component.start)
                                                                                    ),
                                                                                    cardinality.optional,
                                                                                    PropertyValueSchemas(propertyType.integer)
                                                                            ),
                                                                            PropertySchema(
                                                                                    TypeSchemas(
                                                                                            TypeSchema(Sbol2Terms.component.end)
                                                                                    ),
                                                                                    cardinality.optional,
                                                                                    PropertyValueSchemas(propertyType.integer)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )


                            )

                    )
            )
    );
  }
}

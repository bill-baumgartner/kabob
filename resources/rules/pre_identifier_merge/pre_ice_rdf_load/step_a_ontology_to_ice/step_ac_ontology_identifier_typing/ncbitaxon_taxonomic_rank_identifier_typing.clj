;; -----------------------------------------------
;; --------- ncbitaxon identifier typing ---------
;; -----------------------------------------------
`{:name "ncbitaxon-taxonomic-rank-identifier-typing"
  :description "This rule specifically the ncbi taxon ontology identifier"
  :head ((?/id rdfs/subClassOf ccp/IAO_EXT_0001833)) ; CCP:ncbi_taxonomy_taxonomic_rank_ontology_identifier
  :body "prefix franzOption_chunkProcessingAllowed: <franz:yes>
                  prefix ccp: <http://ccp.ucdenver.edu/obo/ext/>
                  prefix obo: <http://purl.obolibrary.org/obo/>
                  prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
                  prefix oboInOwl: <http://www.geneontology.org/formats/oboInOwl#>
                  select ?id {
                  ?id rdfs:subClassOf ccp:IAO_EXT_0000088 . # CCP:ontology_concept_identifier
                  ?id obo:IAO_0000219 ?c . # IAO:denotes
                  ?c oboInOwl:hasOBONamespace 'ncbi_taxonomy' .
                  ?c rdfs:subClassOf* <http://purl.obolibrary.org/obo/NCBITaxon#_taxonomic_rank> .
                  }"
  }
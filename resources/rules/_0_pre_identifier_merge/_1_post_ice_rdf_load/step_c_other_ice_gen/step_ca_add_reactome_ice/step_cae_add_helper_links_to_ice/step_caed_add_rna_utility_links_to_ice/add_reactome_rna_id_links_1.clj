`{:description "Auxiliary rule to connect ensembl, embl, mirbase, or ncbi nucleotide ids and reactome rna ids.",
 :name "add_reactome_rna_id_links_1",
 :head ((?/reactome_id_field ccp/ekws_temp_id_connector_relation ?/external_id_field)
        ),
 :body "#add_reactome_rna_id_links_1
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?reactome_id_field ?external_id_field
WHERE {
?rna_record rdf:type ccp:IAO_EXT_0001558 .  # rna record

?rna_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .
?xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?reactome_identifier .
?reactome_identifier rdfs:subClassOf ccp:IAO_EXT_0001643 . # Reactome ID

?rna_record obo:BFO_0000051 ?this_entity_record .
?this_entity_record rdfs:subClassOf ?entity_record .
?entity_record rdf:type ccp:IAO_EXT_0001561 .  # rna reference record
?entity_record obo:BFO_0000051 ?this_entity_xref_record .
?this_entity_xref_record rdfs:subClassOf ?entity_xref_record .
?entity_xref_record rdf:type ccp:IAO_EXT_0001572 .
?entity_xref_record obo:BFO_0000051 ?external_id_field .
?external_id_field rdf:type ?external_ice .
filter (contains (str (?external_ice), \"ccp.ucdenver.edu/kabob/ice/\")) .
}",
   :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}


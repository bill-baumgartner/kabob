`{:description "Auxiliary rule to connect small molecule reactome ids and GO-CC locations directly.",
 :name "add_reactome_small_molecule_location_links_2",
 :head ((?/reactome_id_field ccp/ekws_temp_loc_connector_relation ?/go_cc_id_field)
        ),
 :body "#add_reactome_uniprot_to_reactome_links_2
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT ?reactome_id_field ?go_cc_id_field
WHERE {
?small_molecule_record rdf:type ccp:IAO_EXT_0001514 .  # small molecule record

?small_molecule_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .
?xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?reactome_identifier .
?reactome_identifier rdfs:subClassOf ccp:IAO_EXT_0001643 . # Reactome ID

?small_molecule_record obo:BFO_0000051 ?this_cell_loc_record .
?this_cell_loc_record rdfs:subClassOf ?cell_loc_record .
?cell_loc_record rdf:type ccp:IAO_EXT_0001584 . 
?cell_loc_record obo:BFO_0000051 ?this_go_cc_xref .
?this_go_cc_xref rdfs:subClassOf ?go_cc_xref .
?go_cc_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref  
?go_cc_xref obo:BFO_0000051 ?go_cc_id_field .
?go_cc_id_field rdf:type ?go_cc_ice .
?go_cc_ice obo:IAO_0000219 ?go_cc_bio .
filter (!contains (str (?go_cc_bio), \"/purl.obolibrary.org\"))

}",
   :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}


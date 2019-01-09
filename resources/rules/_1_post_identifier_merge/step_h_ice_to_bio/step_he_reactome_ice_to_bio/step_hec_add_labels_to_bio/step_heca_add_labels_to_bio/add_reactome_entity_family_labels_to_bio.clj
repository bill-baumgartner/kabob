`{:description "This rule finds any functional family described in Reactome and labels it on the bio side.",
 :name "add_reactome_entity_family_labels_to_bio",
 :head ((?/union_class rdfs/label ?/display_name_label)
        ),
 :body "#add_reactome_entity_family_labels_to_bio.clj
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?union_class ?display_name_label 
WHERE {
?this_member_record rdf:type ccp:IAO_EXT_0001528 . # member physical entity field
?this_member_record rdfs:subClassOf ?member_record .
?family_record obo:BFO_0000051 ?this_member_record .
?family_record obo:BFO_0000051 ?this_family_xref_record .
?this_family_xref_record rdfs:subClassOf ?family_xref_record .
?family_xref_record rdf:type ccp:IAO_EXT_0001572 . # unification xref
?family_xref_record obo:BFO_0000051 ?family_xref_id_field .
?family_xref_id_field rdf:type ?family_reactome_ice .
?family_reactome_ice rdfs:subClassOf ccp:IAO_EXT_0001643 . # Reactome identifier
?family_reactome_ice obo:IAO_0000219 ?union_class .
?family_record obo:BFO_0000051 ?display_name_field .
?display_name_field rdf:type ccp:IAO_EXT_0001526 .
?display_name_field rdfs:label ?display_name_label .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

        

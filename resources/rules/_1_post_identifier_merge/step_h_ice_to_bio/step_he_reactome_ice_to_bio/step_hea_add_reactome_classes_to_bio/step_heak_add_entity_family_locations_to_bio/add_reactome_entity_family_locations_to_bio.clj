`{:description "This rule finds any functional family described in Reactome and places the top-level entity list in a location on the BIO side.",
 :name "add_reactome_entity_family_locations_to_bio",
 :reify ([?/gocc_loc {:ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_", :ln (:sha-1 ?/gocc_bio ?/union_class)}]
         [?/trans_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/target_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/localization_sc {:ln (:sha-1 "GO_0051179" ?/target_restriction ?/trans_restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "LOC_"}]
         ),
 :head ((?/gocc_loc rdfs/subClassOf ?/gocc_bio)
        (?/trans_restriction owl/someValuesFrom ?/union_class)
        (?/trans_restriction rdf/type owl/Restriction)
        (?/trans_restriction owl/onProperty ?/transports_bio)
        (?/target_restriction owl/someValuesFrom ?/gocc_loc)
        (?/target_restriction rdf/type owl/Restriction)
        (?/target_restriction owl/onProperty ?/targets_bio)
        (?/localization_sc rdfs/subClassOf ?/target_restriction)
        (?/localization_sc rdfs/subClassOf ?/trans_restriction)
        (?/localization_sc rdfs/subClassOf ?/localization_bio)
        ),
 :body "#add_reactome_entity_family_locations_to_bio.clj
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?union_class ?targets_bio ?transports_bio ?localization_bio ?gocc_bio
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
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002313\")) AS ?transports) .
?transports obo:IAO_0000219 ?transports_bio .
filter (?transports_bio != obo:RO_0002313) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002339\")) AS ?targets) .
?targets obo:IAO_0000219 ?targets_bio .
filter (?targets_bio != obo:RO_0002339) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/GO_0051179\")) AS ?localization) .
?localization obo:IAO_0000219 ?localization_bio .
filter (?localization_bio != obo:GO_0051179) .
?family_record obo:BFO_0000051 ?this_cell_loc_record .
?this_cell_loc_record rdfs:subClassOf ?cell_loc_record .
?cell_loc_record rdf:type ccp:IAO_EXT_0001584. 
?cell_loc_record obo:BFO_0000051 ?this_go_cc_xref .
?this_go_cc_xref rdfs:subClassOf ?go_cc_xref .
?go_cc_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref  
?go_cc_xref obo:BFO_0000051 ?go_cc_id_field .
?go_cc_id_field rdf:type ?go_cc_ice .
?go_cc_ice obo:IAO_0000219 ?gocc_bio .
filter (!contains (str (?gocc_bio), \"/purl.obolibrary.org\"))
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

        

`{:description "This rule finds any degradation record described in Reactome with a single location and puts it on the bio side.",
 :name "add_reactome_degradation_locations_to_bio_1",
 :reify ([?/this_input_go_cc_bio {:ln (:sha-1 ?/input_go_cc_bio ?/degr_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "B_"}]
         [?/occurs_in_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         ),
 :head
 ((?/degr_bio rdfs/subClassOf ?/occurs_in_restriction)
  (?/this_input_go_cc_bio rdfs/subClassOf ?/input_go_cc_bio)
  (?/occurs_in_restriction rdf/type owl/Restriction)
  (?/occurs_in_restriction owl/onProperty ?/occurs_in_bio)
  (?/occurs_in_restriction owl/someValuesFrom ?/this_input_go_cc_bio)
  ),
 :body "#add_reactome_degradation_locations_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT ?degr_bio ?input_go_cc_bio ?occurs_in_bio
WHERE {   
?degr_record rdf:type ccp:IAO_EXT_0001585 . # biochemical reaction
?degr_record obo:BFO_0000051 ?this_input_record .
?this_input_record rdf:type ccp:IAO_EXT_0001549 . # left field, input
?this_input_record rdfs:subClassOf ?input_record .
?input_record obo:BFO_0000051 ?this_input_xref .
?this_input_xref rdfs:subClassOf ?input_xref .
?input_xref rdf:type ccp:IAO_EXT_0001572 .
?input_xref obo:BFO_0000051 ?input_xref_react_id_field .
?input_xref_react_id_field ccp:ekws_temp_loc_connector_relation ?input_go_cc_id_field .
?input_go_cc_id_field rdf:type ?input_go_cc_ice .
filter (contains (str(?input_go_cc_ice), \"http://ccp.ucdenver.edu/kabob/ice/\")) .
?input_go_cc_ice obo:IAO_0000219 ?input_go_cc_bio .
filter (contains (str(?input_go_cc_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .
?degr_record obo:BFO_0000051 ?this_degr_xref_record .
?this_degr_xref_record rdfs:subClassOf ?degr_xref_record .
?degr_xref_record rdf:type ccp:IAO_EXT_0001572 .  # unification xref
?degr_xref_record obo:BFO_0000051 ?degr_react_id .
?degr_react_id rdf:type ?degr_react_ice .
filter (contains (str (?degr_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?degr_react_ice obo:IAO_0000219 ?degr_bio .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000066\") AS ?occurs_in_ice) .
?occurs_in_ice obo:IAO_0000219 ?occurs_in_bio .
filter (?occurs_in_bio != obo:BFO_0000066) .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

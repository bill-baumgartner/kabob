`{:description "This rule finds any biochemical reaction record described in Reactome with a single location and puts it on the bio side.",
 :name "add_reactome_biochemical_reaction_locations_to_bio_1",
 :reify ([?/this_input_go_cc_bio {:ln (:sha-1 ?/input_go_cc_bio ?/bcr_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "B_"}]
         [?/occurs_in_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         ),
 :head
 ((?/bcr_bio rdfs/subClassOf ?/occurs_in_restriction)
  (?/this_input_go_cc_bio rdfs/subClassOf ?/input_go_cc_bio)
  (?/occurs_in_restriction rdf/type owl/Restriction)
  (?/occurs_in_restriction owl/onProperty ?/occurs_in_bio)
  (?/occurs_in_restriction owl/someValuesFrom ?/this_input_go_cc_bio)
  ),
 :body "#add_reactome_biochemical_reaction_locations_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT ?bcr_bio ?input_go_cc_bio ?occurs_in_bio
WHERE {   
?bcr_record rdf:type ccp:IAO_EXT_0001554 . # biochemical reaction
?bcr_record obo:BFO_0000051 ?this_input_record .
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
OPTIONAL {
?bcr_record obo:BFO_0000051 ?this_output_record .
?this_output_record rdf:type ccp:IAO_EXT_0001550 . # right field, output
?this_output_record rdfs:subClassOf ?output_record .
?output_record obo:BFO_0000051 ?this_output_xref .
?this_output_xref rdfs:subClassOf ?output_xref .
?output_xref rdf:type ccp:IAO_EXT_0001572 .
?output_xref obo:BFO_0000051 ?output_xref_react_id_field .
?output_xref_react_id_field ccp:ekws_temp_loc_connector_relation ?output_go_cc_id_field .
?output_go_cc_id_field rdf:type ?output_go_cc_ice .
filter (contains (str(?output_go_cc_ice), \"http://ccp.ucdenver.edu/kabob/ice/\")) .
?output_go_cc_ice obo:IAO_0000219 ?output_go_cc_bio .
filter (contains (str(?output_go_cc_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .
filter (?output_go_cc_bio != ?input_go_cc_bio) .
}
filter (!bound (?output_go_cc_bio)) .
?bcr_record obo:BFO_0000051 ?this_bcr_xref_record .
?this_bcr_xref_record rdfs:subClassOf ?bcr_xref_record .
?bcr_xref_record rdf:type ccp:IAO_EXT_0001572 .  # unification xref
?bcr_xref_record obo:BFO_0000051 ?bcr_react_id .
?bcr_react_id rdf:type ?bcr_react_ice .
filter (contains (str (?bcr_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?bcr_react_ice obo:IAO_0000219 ?bcr_bio .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000066\") AS ?occurs_in_ice) .
?occurs_in_ice obo:IAO_0000219 ?occurs_in_bio .
filter (?occurs_in_bio != obo:BFO_0000066) .

}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

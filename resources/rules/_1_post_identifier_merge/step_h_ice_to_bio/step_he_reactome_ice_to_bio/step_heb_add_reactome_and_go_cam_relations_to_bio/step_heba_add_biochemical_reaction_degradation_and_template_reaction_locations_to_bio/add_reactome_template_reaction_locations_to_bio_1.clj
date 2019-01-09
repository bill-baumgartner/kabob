`{:description "This rule finds any template reaction record described in Reactome with a double location (i. e., a transport) and puts it on the bio side.",
 :name "add_reactome_template_reaction_locations_to_bio_1",
 :reify ([?/this_output_go_cc_bio {:ln (:sha-1 ?/output_go_cc_bio ?/tmplr_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "B_"}]
         [?/occurs_in_restriction_2 {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         ),
 :head
 ((?/tmplr_bio rdfs/subClassOf ?/occurs_in_restriction_2)
  (?/this_output_go_cc_bio rdfs/subClassOf ?/output_go_cc_bio)
  (?/occurs_in_restriction_2 rdf/type owl/Restriction)
  (?/occurs_in_restriction_2 owl/onProperty ?/occurs_in_bio)
  (?/occurs_in_restriction_2 owl/someValuesFrom ?/this_output_go_cc_bio)
  ),
 :body "#add_reactome_template_reaction_locations_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT ?tmplr_bio ?output_go_cc_bio ?occurs_in_bio
WHERE {   
?tmplr_record rdf:type ccp:IAO_EXT_0001580 . # template reaction
?tmplr_record obo:BFO_0000051 ?this_output_record .
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
?tmplr_record obo:BFO_0000051 ?this_tmplr_xref_record .
?this_tmplr_xref_record rdfs:subClassOf ?tmplr_xref_record .
?tmplr_xref_record rdf:type ccp:IAO_EXT_0001572 .  # unification xref
?tmplr_xref_record obo:BFO_0000051 ?tmplr_react_id .
?tmplr_react_id rdf:type ?tmplr_react_ice .
filter (contains (str (?tmplr_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?tmplr_react_ice obo:IAO_0000219 ?tmplr_bio .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000066\") AS ?occurs_in_ice) .
?occurs_in_ice obo:IAO_0000219 ?occurs_in_bio .
filter (?occurs_in_bio != obo:BFO_0000066) .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

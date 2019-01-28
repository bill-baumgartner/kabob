`{:description "This rule finds any degradation record described in Reactome with unique inputs and marks those as inputs in the GO-MF that GO-CAM defines as part of the Reactome degradation."
 :name "add_reactome_degradation_inputs_to_bio_1",
 :reify ([?/input_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_input {:ln (:sha-1 ?/input_bio ?/catal_activity), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         ),
 :head
 ((?/this_input rdfs/subClassOf ?/input_bio)
  (?/catal_activity rdfs/subClassOf ?/input_restriction)
  (?/input_restriction rdf/type owl/Restriction)
  (?/input_restriction owl/onClass ?/this_input)
  (?/input_restriction owl/onProperty ?/has_input_bio)),
 :body "#add_reactome_degradation_inputs_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?input_bio ?catal_activity ?has_input_bio
WHERE {   
?degr_record rdf:type ccp:IAO_EXT_0001585 . # degradation record
?degr_record obo:BFO_0000051 ?this_left_thing_record .
?this_left_thing_record rdf:type ccp:IAO_EXT_0001549 .
?this_left_thing_record rdfs:subClassOf ?left_thing_record .
# get the bio side input
?left_thing_record obo:BFO_0000051 ?this_input_xref_record .
?this_input_xref_record rdfs:subClassOf ?input_xref_record .
?input_xref_record obo:BFO_0000051 ?input_react_id_field .
?input_react_id_field rdf:type ?input_ice .
?input_ice obo:IAO_0000219 ?input_bio .
# get the bio side reaction
?degr_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .
?xref_record obo:BFO_0000051 ?react_id_field .
?react_id_field rdf:type ?degr_ice .  
?degr_ice obo:IAO_0000219 ?degr_bio .
# get the subclassed reaction in an ordered pathway
?this_controlled_process rdfs:subClassOf ?degr_bio .  
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
# get the subclassed catalyzed reaction for this reaction
# GO-CAM defines this using a part-of relation
?this_catalyzed_process rdfs:subClassOf ?this_controlled_process .
?this_catalyzed_process rdfs:subClassOf ?process_has_catalyst_part_restriction .
?process_has_catalyst_part_restriction rdf:type owl:Restriction .
?process_has_catalyst_part_restriction owl:onProperty ?has_part_bio .
?process_has_catalyst_part_restriction owl:onClass ?catal_activity .  

bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0002233\")) AS ?has_input_ice) .
?has_input_ice obo:IAO_0000219 ?has_input_bio .
filter (?has_input_bio != obo:RO_0002233) .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

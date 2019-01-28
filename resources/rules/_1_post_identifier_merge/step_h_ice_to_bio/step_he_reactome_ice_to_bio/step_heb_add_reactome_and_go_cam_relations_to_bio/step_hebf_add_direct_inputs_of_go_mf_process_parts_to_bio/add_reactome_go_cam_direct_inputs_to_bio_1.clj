`{:description "This rule links GO-MFs that are part of biological processes, in the case where one GO-MF provides input for the next GO-MF."
 :name "add_reactome_go_cam_direct_inputs_to_bio_1",
 :reify ([?/directly_provides_input_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_catal_activity_1 {:ln (:sha-1 "gets input" ?/catal_activity_2  ?/catal_activity_1), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "MF_"}]
         [?/this_catal_activity_2 {:ln (:sha-1 "gives input" ?/catal_activity_2), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "MF_"}]
         ),
 :head
 ((?/this_catal_activity_1 rdfs/subClassOf ?/catal_activity_1)
  (?/this_catal_activity_2 rdfs/subClassOf ?/catal_activity_2)
  (?/this_catal_activity_2 rdfs/subClassOf ?/directly_provides_input_restriction)
  (?/directly_provides_input_restriction rdf/type owl/Restriction)
  (?/directly_provides_input_restriction owl/someValuesFrom ?/this_catal_activity_1)
  (?/directly_provides_input_restriction owl/onProperty ?/directly_provides_input_for_bio)),
 :body "#add_reactome_go_cam_direct_inputs_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?catal_activity_1 ?catal_activity_2 ?directly_provides_input_for_bio
WHERE {   
bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0002413\")) AS ?directly_provides_input_for_ice) .
?directly_provides_input_for_ice obo:IAO_0000219 ?directly_provides_input_for_bio .
filter (?directly_provides_input_for_bio != obo:RO_0002413) .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
?bcr_record_1 obo:BFO_0000051 ?this_left_thing_record .
?this_left_thing_record rdf:type ccp:IAO_EXT_0001549 . # left field value
?this_left_thing_record rdfs:subClassOf ?left_thing_record .
?this_right_thing_record rdfs:subClassOf ?left_thing_record .
?this_right_thing_record rdf:type ccp:IAO_EXT_0001550 . # right field value

?bcr_record_2 obo:BFO_0000051 ?this_right_thing_record .
?bcr_record_1 obo:BFO_0000051 ?this_xref_record_1 .
?this_xref_record_1 rdfs:subClassOf ?xref_record_1 .
?xref_record_1 rdf:type ccp:IAO_EXT_0001572 .
?xref_record_1 obo:BFO_0000051 ?react_id_field_1 .
?react_id_field_1 rdf:type ?bcr_ice_1 .  
?bcr_ice_1 obo:IAO_0000219 ?bcr_bio_1 .
# get the subclassed reaction in an ordered pathway
?this_controlled_process_1 rdfs:subClassOf ?bcr_bio_1 .  
?bcr_record_2 obo:BFO_0000051 ?this_xref_record_2 .
?this_xref_record_2 rdfs:subClassOf ?xref_record_2 .
?xref_record_2 rdf:type ccp:IAO_EXT_0001572 .
?xref_record_2 obo:BFO_0000051 ?react_id_field_2 .
?react_id_field_2 rdf:type ?bcr_ice_2 .  
?bcr_ice_2 obo:IAO_0000219 ?bcr_bio_2 .
# get the subclassed reaction in an ordered pathway
?this_controlled_process_2 rdfs:subClassOf ?bcr_bio_2 .  
?this_catalyzed_process_1 rdfs:subClassOf ?this_controlled_process_1 .
?this_catalyzed_process_1 rdfs:subClassOf ?process_has_catalyst_part_restriction_1 .
?process_has_catalyst_part_restriction_1 rdf:type owl:Restriction .
?process_has_catalyst_part_restriction_1 owl:onProperty ?has_part_bio .
?process_has_catalyst_part_restriction_1 owl:onClass ?catal_activity_1 .  
?this_catalyzed_process_2 rdfs:subClassOf ?this_controlled_process_2 .
?this_catalyzed_process_2 rdfs:subClassOf ?process_has_catalyst_part_restriction_2 .
?process_has_catalyst_part_restriction_2 rdf:type owl:Restriction .
?process_has_catalyst_part_restriction_2 owl:onProperty ?has_part_bio .
?process_has_catalyst_part_restriction_2 owl:onClass ?catal_activity_2 .  

}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

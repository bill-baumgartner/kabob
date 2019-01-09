`{:description "This rule connects Reactome template reaction regulation activation events with a GO-MF binding term to downstream activities."
 :name "add_reactome_template_reaction_regulations_to_bio_2",
 :reify ([?/causally_upstream_of_positive_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/binding_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_catal_activity {:ln (:sha-1 ?/catal_activity_1 ?/go_binding_bio ?/this_catalyzed_process_1), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "MF_"}]
         [?/this_controller_bio {:ln (:sha-1 ?/catal_activity_1 ?/go_binding_bio ?/controller_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         [?/this_binding_event {:ln (:sha-1 ?/controller_bio ?/catal_activity_1 ?/go_binding_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "MF_"}]
         ),
 :head
 ((?/this_binding_event rdfs/subClassOf ?/causally_upstream_of_positive_restriction)
  (?/this_binding_event rdfs/subClassOf ?/binding_restriction)
  (?/this_binding_event rdfs/subClassOf ?/go_binding_bio)
  (?/binding_restriction rdfs/subClassOf owl/Restriction)
  (?/binding_restriction owl/onProperty ?/has_participant_bio)
  (?/binding_restriction owl/someValuesFrom ?/this_controller_bio)
  (?/this_controller_bio rdfs/subClassOf ?/controller_bio)
  (?/this_catal_activity rdfs/subClassOf ?/catal_activity_1)
  (?/causally_upstream_of_positive_restriction rdf/type owl/Restriction)
  (?/causally_upstream_of_positive_restriction owl/someValuesFrom ?/this_catal_activity)
  (?/causally_upstream_of_positive_restriction owl/onProperty ?/causally_upstream_of_positive_bio)),
 :body "#add_reactome_template_reaction_regulations_to_bio_2.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?go_binding_bio ?catal_activity_1 ?this_catalyzed_process_1 ?causally_upstream_of_positive_bio ?has_participant_bio ?controller_bio
WHERE {   
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
?trreg_record rdf:type ccp:IAO_EXT_0001581 .
?trreg_record obo:BFO_0000051 ?this_controller_record .
?this_controller_record rdf:type ccp:IAO_EXT_0001566 . # controller field
?this_controller_record rdfs:subClassOf ?controller_record .
?trreg_record obo:BFO_0000051 ?control_type_field.
?control_type_field rdf:type ccp:IAO_EXT_0001568 . 
?control_type_field rdfs:label \"ACTIVATION\"@en .
?controller_record obo:BFO_0000051 ?this_controller_xref .
?this_controller_xref rdfs:subClassOf ?controller_xref .
?controller_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?controller_xref obo:BFO_0000051 ?controller_react_id .
?controller_react_id rdf:type ?controller_react_ice .
filter (contains (str (?controller_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?controller_react_ice obo:IAO_0000219 ?controller_bio .
?trreg_record obo:BFO_0000051 ?this_controlled_record .
?this_controlled_record rdf:type ccp:IAO_EXT_0001567 . # controlled field
?this_controlled_record rdfs:subClassOf ?controlled_record .
?controlled_record obo:BFO_0000051 ?this_controlled_xref .
?this_controlled_xref rdfs:subClassOf ?controlled_xref .
?controlled_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?controlled_xref obo:BFO_0000051 ?controlled_react_id .
?controlled_react_id rdf:type ?controlled_react_ice .
filter (contains (str (?controlled_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?controlled_react_ice obo:IAO_0000219 ?controlled_bio .
?this_controlled_process_1 rdfs:subClassOf ?controlled_bio .  
?this_catalyzed_process_1 rdfs:subClassOf ?this_controlled_process_1 .
?this_catalyzed_process_1 rdfs:subClassOf ?process_has_catalyst_part_restriction_1 .
?process_has_catalyst_part_restriction_1 rdf:type owl:Restriction .
?process_has_catalyst_part_restriction_1 owl:onProperty ?has_part_bio .
?process_has_catalyst_part_restriction_1 owl:onClass ?catal_activity_1 .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002304\") AS ?causally_upstream_of_positive_ice) .
?causally_upstream_of_positive_ice obo:IAO_0000219 ?causally_upstream_of_positive_bio .
filter (?causally_upstream_of_positive_bio != obo:RO_0002304) .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/GO_0005488\") AS ?go_binding_ice) .
?go_binding_ice obo:IAO_0000219 ?go_binding_bio .
filter (?go_binding_bio != obo:RO_0005488) .
bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0000057\")) AS ?has_participant_ice) .
?has_participant_ice obo:IAO_0000219 ?has_participant_bio .
filter (?has_participant_bio != obo:RO_0000057) .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}




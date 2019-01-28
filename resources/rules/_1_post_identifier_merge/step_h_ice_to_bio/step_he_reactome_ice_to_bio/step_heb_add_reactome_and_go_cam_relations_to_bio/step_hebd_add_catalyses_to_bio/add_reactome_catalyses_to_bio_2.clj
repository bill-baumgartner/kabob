`{:description "This rule finds any uncatalyzed, uncontrolled biochemical reaction pathway step record described in Reactome and puts it and a generic GO-MF on the bio side, linking it to the generic GO MF term.",
 :name "add_reactome_catalyses_to_bio_2",
 :reify ([?/generic_mf_activity {:ln (:sha-1 ?/step_process_record ?/pw_record ?/go_mf), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "MF_"}]
         [?/this_uncatalyzed_process {:ln (:sha-1 ?/this_uncontrolled_process ?/pw_record), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "BP_"}]
         [?/process_has_catalyst_part_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         ),
 :head
 (;; Reactome defines a catalytic activity via GO_MF without exception,
  ;; but some biochemical reactions don't list a catalyst 
  (?/generic_mf_activity rdfs/subClassOf ?/go_mf)  ;; general GO-MF
  ;; We subclass the pathway step being catalyzed; this sub-process corresponds
  ;; roughly to a GO-BP
  (?/this_uncatalyzed_process rdfs/subClassOf ?/this_uncontrolled_process)
  ;; per GO-CAM, we then define the catalytic activity as part of this process
  (?/this_uncatalyzed_process rdfs/subClassOf ?/process_has_catalyst_part_restriction)
  (?/process_has_catalyst_part_restriction rdf/type owl/Restriction)
  (?/process_has_catalyst_part_restriction owl/onProperty ?/has_part_bio)
  (?/process_has_catalyst_part_restriction owl/onClass ?/generic_mf_activity)    
  ),
 :body "#add_reactome_catalyses_to_bio_2.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?catal_record ?go_mf ?go_ice ?enabled_by_bio ?uncontroller_bio ?this_uncontrolled_process ?pw_bio ?has_part_bio ?pw_record ?step_process_record
WHERE {   
?pws_record rdf:type ccp:IAO_EXT_0001573 . # pathway step record
?this_pws_record rdfs:subClassOf ?pws_record .   
?pw_record obo:BFO_0000051 ?this_pws_record .
?pw_record obo:BFO_0000051 ?this_pw_xref .
?this_pw_xref rdfs:subClassOf ?pw_xref .
?pw_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?pw_xref obo:BFO_0000051 ?pw_react_id .
?pw_react_id rdf:type ?pw_react_ice .
filter (contains (str (?pw_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?pw_react_ice obo:IAO_0000219 ?pw_bio .
?pws_record obo:BFO_0000051 ?this_step_process_record .
?this_step_process_record rdf:type ccp:IAO_EXT_0001548 . # step process field
?this_step_process_record rdfs:subClassOf ?step_process_record .
optional {
?pws_record obo:BFO_0000051 ?this_step_process_record_2 .
?this_step_process_record_2 rdf:type ccp:IAO_EXT_0001548 . # step process field
?this_step_process_record_2 rdfs:subClassOf ?step_process_record_2 .
?step_process_record_2 rdf:type ccp:IAO_EXT_0001574 . # catalysis record
}
filter (!bound (?step_process_record_2)) .
#optional {
#?pws_record obo:BFO_0000051 ?this_step_process_record_3 .
#?this_step_process_record_3 rdf:type ccp:IAO_EXT_0001548 . # step process field
#?this_step_process_record_3 rdfs:subClassOf ?step_process_record_3 .
#?step_process_record_3 rdf:type ccp:IAO_EXT_0001564 . # control record
#}
#filter (!bound (?step_process_record_3)) .
?step_process_record obo:BFO_0000051 ?this_uncontrolled_xref .
?this_uncontrolled_xref rdfs:subClassOf ?uncontrolled_xref .
?uncontrolled_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?uncontrolled_xref obo:BFO_0000051 ?uncontrolled_react_id .
?uncontrolled_react_id rdf:type ?uncontrolled_react_ice .
filter (contains (str (?uncontrolled_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?uncontrolled_react_ice obo:IAO_0000219 ?uncontrolled_bio .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/GO_0003674\") AS ?go_mf_ice) .
bind (uri (\"http://purl.obolibrary.org/obo/GO_0003674\") AS ?obo_go) .
?go_mf_ice obo:IAO_0000219 ?go_mf .
filter (?go_mf != ?obo_go) .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
?pw_bio rdfs:subClassOf ?has_part_restriction .
?has_part_restriction owl:onProperty ?has_part_bio .
?has_part_restriction owl:onClass ?this_uncontrolled_process .
?this_uncontrolled_process rdfs:subClassOf ?uncontrolled_bio .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

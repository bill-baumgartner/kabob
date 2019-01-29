`{:description "This rule finds any catalysis record described in Reactome with a GO MF annotation and puts it and its enabler on the bio side, linking it to a GO MF term."
 :name "add_reactome_catalyses_to_bio_1",
 :reify ([?/catal_activity {:ln (:sha-1 ?/catal_record ?/controlled_record ?/pw_record),  :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "MF_"}]
         [?/this_catalyst {:ln (:sha-1 ?/catal_record ?/this_controlled_process ?/controller_record),  :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         [?/this_catalyzed_process {:ln (:sha-1 ?/catal_record ?/this_controlled_process),  :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "BP_"}]
         [?/enabled_by_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/process_has_catalyst_part_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         ),
 :head
 (;; Reactome defines a catalytic activity via GO_MF without exception
  (?/catal_activity rdfs/subClassOf ?/go_mf)  ;; specific GO-MF
  ;; GO CAM model uses RO's enabled by relation to connect the catalyst to its activity
  (?/catal_activity rdfs/subClassOf ?/enabled_by_restriction)
  (?/this_catalyst rdfs/subClassOf ?/controller_bio)
  (?/enabled_by_restriction rdf/type owl/Restriction)
  (?/enabled_by_restriction owl/onProperty ?/enabled_by_bio)
  (?/enabled_by_restriction owl/someValuesFrom ?/this_catalyst)
  ;; We subclass the pathway step being catalyzed; this sub-process corresponds
  ;; roughly to a GO-BP
  (?/this_catalyzed_process rdfs/subClassOf ?/this_controlled_process)
  ;; per GO-CAM, we then define the catalytic activity as part of this process
  (?/this_catalyzed_process rdfs/subClassOf ?/process_has_catalyst_part_restriction)
  (?/process_has_catalyst_part_restriction rdf/type owl/Restriction)
  (?/process_has_catalyst_part_restriction owl/onProperty ?/has_part_bio)
  (?/process_has_catalyst_part_restriction owl/onClass ?/catal_activity)    
  ),
 :body "#add_reactome_catalyses_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?catal_record ?go_mf ?go_ice ?enabled_by_bio ?controller_bio ?this_controlled_process ?pw_bio ?has_part_bio ?controlled_record ?pw_record ?controller_record
WHERE {   
?catal_record rdf:type ccp:IAO_EXT_0001574 . # catalysis record
?catal_record obo:BFO_0000051 ?this_controller_record .
?this_controller_record rdf:type ccp:IAO_EXT_0001566 . # controller field
?this_controller_record rdfs:subClassOf ?controller_record .
?controller_record obo:BFO_0000051 ?this_controller_xref .
?this_controller_xref rdfs:subClassOf ?controller_xref .
?controller_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?controller_xref obo:BFO_0000051 ?controller_react_id .
?controller_react_id rdf:type ?controller_react_ice .
filter (contains (str (?controller_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?controller_react_ice obo:IAO_0000219 ?controller_bio .
?catal_record obo:BFO_0000051 ?this_controlled_record .
?this_controlled_record rdf:type ccp:IAO_EXT_0001567 . # controlled field
?this_controlled_record rdfs:subClassOf ?controlled_record .
?controlled_record obo:BFO_0000051 ?this_controlled_xref .
?this_controlled_xref rdfs:subClassOf ?controlled_xref .
?controlled_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?controlled_xref obo:BFO_0000051 ?controlled_react_id .
?controlled_react_id rdf:type ?controlled_react_ice .
filter (contains (str (?controlled_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?controlled_react_ice obo:IAO_0000219 ?controlled_bio .
?catal_record obo:BFO_0000051 ?this_go_mf_xref_record .
?this_go_mf_xref_record rdfs:subClassOf ?go_mf_xref_record .
?go_mf_xref_record rdf:type ccp:IAO_EXT_0001570 .  # Relationship Xref
?go_mf_xref_record obo:BFO_0000051 ?go_mf_xref_id_field .
?go_mf_xref_id_field rdf:type ?go_ice .
?go_mf_xref_id_field rdfs:label ?go_id .
bind (uri (concat (\"http://ccp.ucdenver.edu/kabob/ice/GO_\", strafter (str (?go_id), \":\"))) AS ?obo_go) .
filter (contains (str (?go_ice), \"http://ccp.ucdenver.edu/kabob/ice/GO_\")) .
?go_ice obo:IAO_0000219 ?go_mf .
filter (?go_mf != ?obo_go) .   
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002333\") AS ?enabled_by_ice) .
?enabled_by_ice obo:IAO_0000219 ?enabled_by_bio .
filter (?enabled_by_bio != obo:RO_0002333) .
?this_catal_record rdfs:subClassOf ?catal_record .
?pws_record obo:BFO_0000051 ?this_catal_record .
?pws_record rdf:type ccp:IAO_EXT_0001573 . # pathway step record
?pws_record obo:BFO_0000051 ?that_controlled_record .
?that_controlled_record rdf:type ccp:IAO_EXT_0001548 .
?that_controlled_record rdfs:subClassOf ?controlled_record .  
?this_pws_record rdfs:subClassOf ?pws_record .
?pw_record obo:BFO_0000051 ?this_pws_record .
?pw_record obo:BFO_0000051 ?this_pw_xref .
?this_pw_xref rdfs:subClassOf ?pw_xref .
?pw_xref rdf:type ccp:IAO_EXT_0001572 . # unification xref
?pw_xref obo:BFO_0000051 ?pw_react_id .
?pw_react_id rdf:type ?pw_react_ice .
filter (contains (str (?pw_react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?pw_react_ice obo:IAO_0000219 ?pw_bio .
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
?pw_bio rdfs:subClassOf ?has_part_restriction .
?has_part_restriction owl:onProperty ?has_part_bio .
?has_part_restriction owl:onClass ?this_controlled_process .
?this_controlled_process rdfs:subClassOf ?controlled_bio .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

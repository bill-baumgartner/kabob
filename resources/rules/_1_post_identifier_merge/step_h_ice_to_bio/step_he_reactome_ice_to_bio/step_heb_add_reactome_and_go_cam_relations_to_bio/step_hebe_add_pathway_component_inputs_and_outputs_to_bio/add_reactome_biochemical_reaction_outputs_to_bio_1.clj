`{:description "This rule finds any biochemical record described in Reactome with unique outputs and marks those as outputs in the GO-MF that GO-CAM defines as part of the Reactome biochemical reaction."
 :name "add_reactome_biochemical_reaction_outputs_to_bio_1",
 :reify ([?/output_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_output {:ln (:sha-1 ?/output_bio ?/catal_activity), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         ),
 :head
 ((?/this_output rdfs/subClassOf ?/output_bio)
  (?/catal_activity rdfs/subClassOf ?/output_restriction)
  (?/output_restriction rdf/type owl/Restriction)
  (?/output_restriction owl/onClass ?/this_output)
  (?/output_restriction owl/onProperty ?/has_output_bio)),
 :body "#add_reactome_biochemical_reaction_outputs_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?output_bio ?catal_activity ?has_output_bio
WHERE {   
?bcr_record rdf:type ccp:IAO_EXT_0001554 . # biochemical reaction record
?bcr_record obo:BFO_0000051 ?this_right_thing_record .
?this_right_thing_record rdf:type ccp:IAO_EXT_0001550 .  # right field record
?this_right_thing_record rdfs:subClassOf ?right_thing_record .
# get the bio side output
?right_thing_record obo:BFO_0000051 ?this_output_xref_record .
?this_output_xref_record rdfs:subClassOf ?output_xref_record .
?output_xref_record obo:BFO_0000051 ?output_react_id_field .
?output_react_id_field rdf:type ?output_ice .
?output_ice obo:IAO_0000219 ?output_bio .
# get the bio side reaction
?bcr_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .
?xref_record obo:BFO_0000051 ?react_id_field .
?react_id_field rdf:type ?bcr_ice .  
?bcr_ice obo:IAO_0000219 ?bcr_bio .
# get the subclassed reaction in an ordered pathway
?this_controlled_process rdfs:subClassOf ?bcr_bio .  
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

bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0002234\")) AS ?has_output_ice) .
?has_output_ice obo:IAO_0000219 ?has_output_bio .
filter (?has_output_bio != obo:RO_0002234) .
  
  
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

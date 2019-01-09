`{:description "This rule finds any biochemical record described in Reactome with identical inputs and outputs and marks those as participants in the GO-MF that CO-CAM defines as part of the Reactome biochemical reaction."
 :name "add_reactome_biochemical_reaction_participants_to_bio_1",
 :reify ([?/participant_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_participant {:ln (:sha-1 ?/participant_bio ?/catal_activity), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         ),
 :head
 ((?/this_participant rdfs/subClassOf ?/participant_bio)
  (?/catal_activity rdfs/subClassOf ?/participant_restriction)
  (?/participant_restriction rdf/type owl/Restriction)
  (?/participant_restriction owl/onClass ?/this_participant)
  (?/participant_restriction owl/onProperty ?/has_participant_bio)),
 :body "#add_reactome_biochemical_reaction_participants_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?participant_bio ?catal_activity ?has_participant_bio
WHERE {   
?bcr_record rdf:type ccp:IAO_EXT_0001554 . # biochemical reaction record
# left and right are marked as the same; can't be inputs or outputs, only
# participants
?bcr_record obo:BFO_0000051 ?this_left_thing_record .
?this_left_thing_record rdf:type ccp:IAO_EXT_0001941 .
?this_left_thing_record rdfs:subClassOf ?left_thing_record .
# get the bio side participant
?left_thing_record obo:BFO_0000051 ?this_participant_xref_record .
?this_participant_xref_record rdfs:subClassOf ?participant_xref_record .
?participant_xref_record obo:BFO_0000051 ?participant_react_id_field .
?participant_react_id_field rdf:type ?participant_ice .
?participant_ice obo:IAO_0000219 ?participant_bio .
filter (contains (str (?participant_bio), \"http://ccp.ucdenver.edu/kabob/bio\")) .

# get the bio side reaction
?bcr_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .  # unification xref
?xref_record obo:BFO_0000051 ?react_id_field .
?react_id_field rdf:type ?bcr_ice .  
?bcr_ice obo:IAO_0000219 ?bcr_bio .
filter (contains (str (?bcr_bio), \"http://ccp.ucdenver.edu/kabob/bio\")) .

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

bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0000057\")) AS ?has_participant_ice) .
?has_participant_ice obo:IAO_0000219 ?has_participant_bio .
filter (?has_participant_bio != obo:RO_0000057) .
  
  
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

`{:description "This rule finds any biochemical reaction record described in Reactome lacking a GO BP annotation and puts it on the bio side.",
 :name "add_reactome_biochemical_reactions_to_bio_1",
 :reify ([?/this_bcr {:ln (:sha-1 ?/bcr_record), :ns "http://ccp.ucdenver.edu/kabob/bio/", :prefix "BP_"}]
         ),
 :head
 ((?/react_ice obo/IAO_0000219 ?/this_bcr) 
  (?/this_bcr rdfs/subClassOf ?/go_bp)  ;; general GO-BP
  ),
 :body "#add_reactome_biochemical_reactions_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?bcr_record ?go_bp ?react_ice 
WHERE {   
?bcr_record rdf:type ccp:IAO_EXT_0001554 . # biochemical reaction record
?bcr_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 . # unification xref
?xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?react_ice .
?react_ice rdfs:subClassOf ccp:IAO_EXT_0001643 .
filter (contains (str (?react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
OPTIONAL {
?bcr_record obo:BFO_0000051 ?this_go_bp_xref_record .
?this_go_bp_xref_record rdfs:subClassOf ?go_bp_xref_record .
?go_bp_xref_record rdf:type ccp:IAO_EXT_0001570 .  # Relationship Xref
}
filter (! BOUND (?go_bp_xref_record)) .
bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/GO_0008150\")) AS ?go_uri) .
?go_uri obo:IAO_0000219 ?go_bp .
filter (?go_bp != obo:GO_0008150) .   
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

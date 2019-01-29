`{:description "This rule finds any pathway reco%rd described in Reactome and adds its label to the bio side.",
 :name "add_reactome_pathway_labels_to_bio",
 :head
 ((?/pw_bio rdfs/label ?/pw_label) 
  ),
 :body "#add_reactome_pathway_labels_to_bio.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?pw_bio ?pw_label 
WHERE {   
?pw_record rdf:type ccp:IAO_EXT_0001553 . # pathway record
?pw_record obo:BFO_0000051 ?display_name_field .
?display_name_field rdf:type ccp:IAO_EXT_0001526 .
?display_name_field rdfs:label ?pw_label .
?pw_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 . # unification xref
?xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?react_ice .
?react_ice rdfs:subClassOf ccp:IAO_EXT_0001643 .
filter (contains (str (?react_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME\")) .
?react_ice obo:IAO_0000219 ?pw_bio .
filter (contains (str (?pw_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

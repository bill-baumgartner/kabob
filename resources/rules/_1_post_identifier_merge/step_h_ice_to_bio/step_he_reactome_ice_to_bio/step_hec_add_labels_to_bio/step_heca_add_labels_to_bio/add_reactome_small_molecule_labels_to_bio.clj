`{:description "This rule finds any small molecule record described in Reactome and adds its label (display name + location) to the bio side.",
 :name "add_reactome_small_molecule_labels_to_bio",
 :head
 ((?/small_molecule_bio rdfs/label ?/small_molecule_label) 
  ),
 :body "#add_reactome_small_molecule_labels_to_bio.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?small_molecule_bio ?small_molecule_label 
WHERE {   
?small_molecule_record rdf:type ccp:IAO_EXT_0001514 .

?small_molecule_record obo:BFO_0000051 ?this_reactome_xref_record .
?this_reactome_xref_record rdfs:subClassOf ?reactome_xref_record .
?reactome_xref_record rdf:type ccp:IAO_EXT_0001572 .
?reactome_xref_record obo:BFO_0000051 ?react_id_field .
?react_id_field rdf:type ?reactome_ice .
filter (contains (str (?reactome_ice), \"http://ccp.ucdenver.edu/kabob/ice/\")) .
?reactome_ice obo:IAO_0000219 ?small_molecule_bio .
filter (contains (str (?small_molecule_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .
?small_molecule_record obo:BFO_0000051 ?display_name_field .
?display_name_field rdf:type ccp:IAO_EXT_0001526 .
?display_name_field rdfs:label ?display_name_label .

?react_id_field ccp:ekws_temp_loc_connector_relation ?go_cc_id_field .
?go_cc_id_field rdf:type ?go_cc_ice .
?go_cc_ice obo:IAO_0000219 ?gocc_bio .
filter (contains (str (?go_cc_ice), \"http://ccp.ucdenver.edu/kabob/ice/\")) .
?go_cc_ice obo:IAO_0000219 ?go_cc_bio .
filter (contains (str (?go_cc_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .
?gocc_bio rdfs:label ?gocc_label .
bind (concat (str (?display_name_label), \" (in \", str(?gocc_label), \")\") AS ?small_molecule_label). 
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

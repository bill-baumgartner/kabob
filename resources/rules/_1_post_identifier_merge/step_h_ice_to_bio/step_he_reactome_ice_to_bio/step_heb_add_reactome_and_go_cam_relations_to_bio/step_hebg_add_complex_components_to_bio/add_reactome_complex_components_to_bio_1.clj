`{:description "This rule connects Reactome complexes to their components."
 :name "add_reactome_complex_components_to_bio_1",
 :reify ([?/has_part_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/this_component_bio {:ln (:sha-1 ?/complex_bio ?/component_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         ),
 :head
 ((?/complex_bio rdfs/subClassOf ?/has_part_restriction)
  (?/this_component_bio rdfs/subClassOf ?/component_bio)
  (?/has_part_restriction rdf/type owl/Restriction)
  (?/has_part_restriction owl/onClass ?/this_component_bio)
  (?/has_part_restriction owl/qualifiedCardinality ?/stoi_coefficient)  
  (?/has_part_restriction owl/onProperty ?/has_part_bio)),
 :body "#add_reactome_complex_components_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes> 
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?stoi_coefficient ?component_bio ?complex_bio ?has_part_bio
WHERE {   
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
?complex_record rdf:type ccp:IAO_EXT_0001516 .
?complex_record obo:BFO_0000051 ?this_component_stoi_record .
?complex_record obo:BFO_0000051 ?this_complex_xref_record .
?this_complex_xref_record rdfs:subClassOf ?complex_xref_record .  
?complex_xref_record rdf:type ccp:IAO_EXT_0001572 .
?complex_xref_record obo:BFO_0000051 ?complex_id_field .
?complex_id_field rdf:type ?complex_ice .
filter (contains (str (?complex_ice), \"ccp.ucdenver.edu/kabob/ice\")) .
?complex_ice obo:IAO_0000219 ?complex_bio .
filter (contains (str (?complex_bio), \"ccp.ucdenver.edu/kabob/bio\")) .
?this_component_stoi_record rdfs:subClassOf ?component_stoi_record .
?this_component_stoi_record rdf:type ccp:IAO_EXT_0001545 .
?component_stoi_record obo:BFO_0000051 ?this_physical_entity_record .
?component_stoi_record obo:BFO_0000051 ?stoi_coefficient .
?stoi_coefficient rdf:type ccp:IAO_EXT_0001540 .
?this_physical_entity_record rdf:type ccp:IAO_EXT_0001539 .
?this_physical_entity_record rdfs:subClassOf ?physical_entity_record .
?physical_entity_record obo:BFO_0000051 ?this_component_xref_record .
?this_component_xref_record rdfs:subClassOf ?component_xref_record .
?component_xref_record rdf:type ccp:IAO_EXT_0001572 .
?component_xref_record obo:BFO_0000051 ?component_xref_id_field .
?component_xref_id_field rdf:type ?component_ice .
filter (contains (str (?component_ice), \"ccp.ucdenver.edu/kabob/ice\")) .
?component_ice obo:IAO_0000219 ?component_bio .
filter (contains (str (?component_bio), \"ccp.ucdenver.edu/kabob/bio\")) .

}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}




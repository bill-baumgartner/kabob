`{:description "This rule finds any pathway record described in Reactome with steps, and orders those steps on the bio side.  We take Reactome at their word that this ordering is causal, but we use RO's 'immediately causally upstream of' relation to avoid problems with cycles here.",
 :name "add_reactome_pathway_orders_to_bio_1",
 :reify ([?/immediately_causally_upstream_of_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/ice/", :prefix "RS_"}]),
 :head
 ((?/pwc_bio_sub rdfs/subClassOf ?/immediately_causally_upstream_of_restriction)
  (?/immediately_causally_upstream_of_restriction rdf/type owl/Restriction)
  (?/immediately_causally_upstream_of_restriction owl/onProperty ?/immediately_causally_upstream_of_bio)
  (?/immediately_causally_upstream_of_restriction owl/onClass ?/npwc_bio_sub)
  ),
 :body "#add_reactome_pathway_orders_to_bio_1.clj
PREFIX franzOption_memoryLimit: <franz:85g> 
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95> 
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity> 
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/> 
PREFIX obo: <http://purl.obolibrary.org/obo/> 
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
SELECT DISTINCT ?pwc_bio_sub ?npwc_bio_sub ?immediately_causally_upstream_of_bio 
WHERE {   
bind (uri (\"http://ccp.ucdenver.edu/kabob/ice/BFO_0000051\") AS ?has_part_ice) .
?has_part_ice obo:IAO_0000219 ?has_part_bio .
filter (?has_part_bio != obo:BFO_0000051) .
bind (uri (str(\"http://ccp.ucdenver.edu/kabob/ice/RO_0002412\")) AS ?immediately_causally_upstream_of_uri) .
bind (uri (str(\"http://purl.obolibrary.org/obo/RO_0002412\")) AS ?immediately_causally_upstream_of_obo_uri) .
?immediately_causally_upstream_of_uri obo:IAO_0000219 ?immediately_causally_upstream_of_bio .
filter (?immediately_causally_upstream_of_bio != ?immediately_causally_upstream_of_obo_uri) .

# pathway steps must succeed each other
?pws_record_1 rdf:type ccp:IAO_EXT_0001573 .
?pws_record_1 obo:BFO_0000051 ?this_pwc_record_1 . # pathway component 1
?this_pwc_record_1 rdf:type ccp:IAO_EXT_0001548 .
?this_pwc_record_1 rdfs:subClassOf ?pwc_record_1 .
?pws_record_1 obo:BFO_0000051 ?this_pws_record_2 .
?this_pws_record_2 rdf:type ccp:IAO_EXT_0001943 . # next step field
?this_pws_record_2 rdfs:subClassOf ?pws_record_2 .
?pws_record_2 obo:BFO_0000051 ?this_pwc_record_2 .
?this_pwc_record_2 rdf:type ccp:IAO_EXT_0001548 .
?this_pwc_record_2 rdfs:subClassOf ?pwc_record_2 .
?pwc_record_1 obo:BFO_0000051 ?this_pwc_xref_record .
?this_pwc_xref_record rdf:type ccp:IAO_EXT_0001588 . # xref field
?this_pwc_xref_record rdfs:subClassOf ?pwc_xref_record .
?pwc_xref_record obo:BFO_0000051 ?pwc_react_id_field .
?pwc_record_2 obo:BFO_0000051 ?this_npwc_xref_record .
?this_npwc_xref_record rdf:type ccp:IAO_EXT_0001588 . # xref field
?this_npwc_xref_record rdfs:subClassOf ?npwc_xref_record .
?npwc_xref_record obo:BFO_0000051 ?npwc_react_id_field .

?npwc_react_id_field rdf:type ?next_step_ice .
?pwc_react_id_field rdf:type ?this_step_ice .
filter (contains (str (?this_step_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME_\")) .
filter (contains (str (?next_step_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME_\")) .
?this_step_ice obo:IAO_0000219 ?pwc_bio .
?next_step_ice obo:IAO_0000219 ?npwc_bio .
?pwc_bio_sub rdfs:subClassOf ?pwc_bio .
?npwc_bio_sub rdfs:subClassOf ?npwc_bio .

}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

`{:description "This rule finds any previously reified control record from Reactome and records its name field(s).",
 :name "add_control_record_names_from_human_reactome_to_ice_step_b1",
 :reify ([?/name_field {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 "Reactome control record name field" ?/name), :prefix "F_"}]),
 :head ((?/contr_record obo/BFO_0000051 ?/name_field)
        (?/name_field rdf/type ccp/IAO_EXT_0001525)
        (?/name_field rdfs/label ?/name)),
  :body "#add_control_record_names_from_human_reactome_to_ice_step_b1.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX bp3: <http://www.reactome.org/biopax/63/48887#>
SELECT ?contr_record ?name 
WHERE {
?contr rdf:type bp:Control . # a control event 
?contr bp:name ?name . # with a name
GRAPH <file://add_control_records_and_reactome_ids_from_human_reactome_to_ice_step_a.nt> { 
?contr obo:IAO_0000142 ?contr_record .}
}"
}
`{:description "This rule finds any small molecule record described in Reactome.  The rule places the top-level entity on the BIO side as a localized subclass of the BIO entity denoted by the ChEBI or PubChem ICE id.",
 :name "add_reactome_small_molecules_to_bio_1",
 :reify ([?/original_foo {:ln (:sha-1 "bio-side reactome small molecule" ?/small_molecule_record), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         [?/trans_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/target_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/gocc_loc {:ln (:sha-1 ?/gocc_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/localization_sc {:ln (:sha-1 "GO_0051179" ?/target_restriction ?/trans_restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "LOC_"}]),
 :head ((?/reactome_ice obo/IAO_0000219 ?/original_foo)
        (?/original_foo rdfs/subClassOf ?/external_bio)
        (?/gocc_loc rdfs/subClassOf ?/gocc_bio)
        (?/trans_restriction owl/someValuesFrom ?/original_foo)
        (?/trans_restriction rdf/type owl/Restriction)
        (?/trans_restriction owl/onProperty ?/transports_bio)
        (?/target_restriction owl/someValuesFrom ?/gocc_loc)
        (?/target_restriction rdf/type owl/Restriction)
        (?/target_restriction owl/onProperty ?/targets_bio)
        (?/localization_sc rdfs/subClassOf ?/target_restriction)
        (?/localization_sc rdfs/subClassOf ?/trans_restriction)
        (?/localization_sc rdfs/subClassOf ?/localization_bio)
        ),
 :body "#add_reactome_small_molecules_to_bio_1
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?reactome_ice ?external_bio ?transports_bio ?targets_bio ?localization_bio ?gocc_bio ?small_molecule_record 
WHERE {
?small_molecule_record rdf:type ccp:IAO_EXT_0001514 .  # small molecule record
?small_molecule_record obo:BFO_0000051 ?this_react_xref_record .
?this_react_xref_record rdfs:subClassOf ?react_xref_record .
?react_xref_record rdf:type ccp:IAO_EXT_0001572 . # unification xref
?react_xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?reactome_ice .
filter (contains (str (?reactome_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME_\")) .
?reactome_id_field ccp:ekws_temp_id_connector_relation ?external_id_field .
?reactome_id_field ccp:ekws_temp_loc_connector_relation ?go_cc_id_field .

?go_cc_id_field rdf:type ?gocc_ice .
filter (contains (str (?gocc_ice), \"http://ccp.ucdenver.edu/kabob/ice/GO_\")) . 
?gocc_ice obo:IAO_0000219 ?gocc_bio .
filter (!contains (str (?gocc_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) . 

?external_id_field rdf:type ?external_ice .
filter (contains (str (?external_ice), \"http://ccp.ucdenver.edu/kabob/ice/\")) .
?external_ice obo:IAO_0000219 ?external_bio .
filter (contains (str (?external_bio), \"http://ccp.ucdenver.edu/kabob/bio/\")) .

bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002313\")) AS ?transports) .
?transports obo:IAO_0000219 ?transports_bio .
filter (?transports_bio != obo:RO_0002313) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002339\")) AS ?targets) .
?targets obo:IAO_0000219 ?targets_bio .
filter (?targets_bio != obo:RO_0002339) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/GO_0051179\")) AS ?localization) .
?localization obo:IAO_0000219 ?localization_bio .
filter (?localization_bio != obo:GO_0051179) .
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

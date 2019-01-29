`{:description "This rule finds any Uniprot non-isoform protein record described in Reactome with either:
1. a fragment feature field defined as being amino acid a to amino acid b, where a > 1 or b < the length of the main isoform, indicating post-translational cleavage, (which would make it a variant), or 
2. a modification feature field (which would also make it a variant).
The rule places the top-level entity on the BIO side as a localized subclass of the BIO entity denoted by the UniProt ICE id.",
 :name "add_reactome_uniprot_proteins_to_bio_3",
 :reify ([?/modified_foo {:ln (:sha-1 "bio-side modified reactome protein" ?/protein_record), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         [?/original_foo {:ln (:sha-1 "bio-side original reactome protein" ?/protein_record), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "B_"}]
         [?/trans_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/target_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/variant_restriction {:ln (:restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/gocc_loc {:ln (:sha-1 ?/gocc_bio), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "RS_"}]
         [?/localization_sc {:ln (:sha-1 "GO_0051179" ?/target_restriction ?/trans_restriction), :ns "http://ccp.ucdenver.edu/kabob/bio/" :prefix "LOC_"}]),
 :head ((?/reactome_ice obo/IAO_0000219 ?/modified_foo)
        (?/original_foo rdfs/subClassOf ?/uniprot_bio)
        (?/modified_foo rdfs/subClassOf ?/variant_restriction)
        (?/variant_restriction owl/onProperty ?/variant_of_bio)
        (?/variant_restriction owl/someValuesFrom ?/original_foo)
        (?/variant_restriction rdf/type owl/Restriction)
        (?/gocc_loc rdfs/subClassOf ?/gocc_bio)
        (?/trans_restriction owl/someValuesFrom ?/modified_foo)
        (?/trans_restriction rdf/type owl/Restriction)
        (?/trans_restriction owl/onProperty ?/transports_bio)
        (?/target_restriction owl/someValuesFrom ?/gocc_loc)
        (?/target_restriction rdf/type owl/Restriction)
        (?/target_restriction owl/onProperty ?/targets_bio)
        (?/localization_sc rdfs/subClassOf ?/target_restriction)
        (?/localization_sc rdfs/subClassOf ?/trans_restriction)
        (?/localization_sc rdfs/subClassOf ?/localization_bio)
        ),
 :body "#add_reactome_uniprot_proteins_to_bio_3
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?reactome_ice ?uniprot_bio ?transports_bio ?targets_bio ?localization_bio ?gocc_bio ?protein_record ?variant_of_bio
WHERE {
?uniprot_accession_field rdf:type ccp:IAO_EXT_0000240 .
?uniprot_accession_field rdfs:label ?accession_label .
?uniprot_record obo:BFO_0000051 ?uniprot_accession_field .
?uniprot_record rdf:type ccp:IAO_EXT_0000234 .
?uniprot_record obo:BFO_0000051 ?uniprot_sequence_record .
?uniprot_sequence_record rdf:type ccp:IAO_EXT_0001234 . # Uniprot sequence record
?uniprot_sequence_record obo:BFO_0000051 ?seq_field .
?seq_field rdf:type ccp:IAO_EXT_0001240 . # uniprot sequence record - value field value
?seq_field rdfs:label ?sequence_label .
bind (strlen (?sequence_label) AS ?seq_length) .
bind (\"1\"^^xsd:integer as ?one) .

?reactome_id_field ccp:ekws_temp_external_connector_relation ?uniprot_record .
?reactome_id_field ccp:ekws_temp_id_connector_relation ?uniprot_id_field .
?reactome_id_field ccp:ekws_temp_loc_connector_relation ?go_cc_id_field .

?reactome_id_field rdf:type ?reactome_ice .
filter (contains (str (?reactome_ice), \"http://ccp.ucdenver.edu/kabob/ice/REACTOME_\")) .
?reactome_xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_xref_record rdf:type ccp:IAO_EXT_0001572 .
?this_reactome_xref_record rdfs:subClassOf ?reactome_xref_record .
?protein_record obo:BFO_0000051 ?this_reactome_xref_record .
?protein_record rdf:type ccp:IAO_EXT_0001513 .

?uniprot_id_field rdfs:label ?accession_label .
?uniprot_id_field rdf:type ccp:IAO_EXT_0001522 .  # Reactome uniprot accession field
?uniprot_id_field rdf:type ?uniprot_ice .
filter (contains (str (?uniprot_ice), \"http://ccp.ucdenver.edu/kabob/ice/UNIPROT_\")) .
?uniprot_ice obo:IAO_0000219 ?uniprot_bio .
bind (uri (concat (str (\"http://purl.obolibrary.org/obo/\"), str (?accession_label))) AS ?obo_uniprot) .
filter (?obo_uniprot != ?uniprot_bio) .
  
?go_cc_id_field rdf:type ?gocc_ice .
?go_cc_id_field rdfs:label ?gocc_id .
filter (contains (str (?gocc_ice), \"http://ccp.ucdenver.edu/kabob/ice/GO_\")) . 
?gocc_ice obo:IAO_0000219 ?gocc_bio .
filter (contains (str (?gocc_bio), \"http://ccp.ucdenver.edu/kabob/bio\")) .

 
OPTIONAL {
?protein_record obo:BFO_0000051 ?this_modification_record .
?this_modification_record rdfs:subClassOf ?modification_record .
?modification_record rdf:type ccp:IAO_EXT_0001586 .  # sequence modification feature fields or fragment feature fields are ok
} 
OPTIONAL {
?protein_record obo:BFO_0000051 ?this_fragment_record .
?this_fragment_record rdfs:subClassOf ?fragment_record .
?fragment_record rdf:type ccp:IAO_EXT_0001587 .
?fragment_record obo:BFO_0000051 ?this_location_interval .
?this_location_interval rdfs:subClassOf ?location_interval .
?location_interval rdf:type ccp:IAO_EXT_0001576 . 
?location_interval obo:BFO_0000051 ?this_start_site .
?this_start_site rdf:type ccp:IAO_EXT_0001534 .
?this_start_site rdfs:subClassOf ?start_site .
?start_site rdf:type ccp:IAO_EXT_0001575 .
?start_site obo:BFO_0000051 ?start_position .
?start_position rdf:type ccp:IAO_EXT_0001538 .
?start_position rdfs:label ?start_coord .
?location_interval obo:BFO_0000051 ?this_end_site .
?this_end_site rdf:type ccp:IAO_EXT_0001535 .
?this_end_site rdfs:subClassOf ?end_site .
?end_site rdf:type ccp:IAO_EXT_0001575 .
?end_site obo:BFO_0000051 ?end_position .
?end_position rdf:type ccp:IAO_EXT_0001538 .
?end_position rdfs:label ?end_coord .
}
filter (bound (?modification_record) || (bound (?fragment_record) && (?start_coord > ?one || ?end_coord < ?seq_length))) .

bind (uri (concat (str (\"http://purl.obolibrary.org/obo/GO_\"), strafter (str (?gocc_id), str (\":\")))) AS ?obo_gocc_uri) .
?gocc_ice obo:IAO_0000219 ?gocc_bio .
filter (?gocc_bio != ?obo_gocc_uri) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002313\")) AS ?transports) .
?transports obo:IAO_0000219 ?transports_bio .
filter (?transports_bio != obo:RO_0002313) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/RO_0002339\")) AS ?targets) .
?targets obo:IAO_0000219 ?targets_bio .
filter (?targets_bio != obo:RO_0002339) .
bind (uri (str (\"http://ccp.ucdenver.edu/kabob/ice/GO_0051179\")) AS ?localization) .
?localization obo:IAO_0000219 ?localization_bio .
filter (?localization_bio != obo:GO_0051179) .
?variant_id obo:IAO_0000219 <http://purl.obolibrary.org/obo/so#variant_of> . 
?variant_id obo:IAO_0000219 ?variant_of_bio . 
filter (contains(str(?variant_of_bio), \"kabob/bio\"))
}",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

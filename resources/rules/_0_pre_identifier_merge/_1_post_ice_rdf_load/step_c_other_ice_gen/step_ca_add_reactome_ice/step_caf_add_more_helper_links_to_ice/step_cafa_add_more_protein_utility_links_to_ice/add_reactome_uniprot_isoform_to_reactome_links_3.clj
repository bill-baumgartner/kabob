`{:description "This rule finds any Uniprot non-isoform protein record described in Reactome with no fragment features or modification features, which allows us to consider it a subclass, not a variant, of the original form.  The rule generates the Reactome protein's ICE identifier, and places the top-level entity on the BIO side as a localized subclass of the BIO entity denoted by the UniProt ICE id.",
 :name "add_reactome_uniprot_isoform_to_reactome_links_3",
 :head ((?/reactome_id_field ccp/ekws_temp_external_connector_relation ?/uniprot_record)
        ),
 :body "#add_reactome_uniprot_proteins_to_bio_1
PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?reactome_id_field ?uniprot_record
WHERE {
?protein_record rdf:type ccp:IAO_EXT_0001513 .  # protein record
?protein_record obo:BFO_0000051 ?this_xref_record .
?this_xref_record rdfs:subClassOf ?xref_record .
?xref_record rdf:type ccp:IAO_EXT_0001572 .
?xref_record obo:BFO_0000051 ?reactome_id_field .
?reactome_id_field rdf:type ?reactome_identifier .
?reactome_identifier rdfs:subClassOf ccp:IAO_EXT_0001643 . # Reactome ID
?reactome_id_field ccp:ekws_temp_id_connector_relation ?uniprot_id_field .
?uniprot_id_field rdf:type ccp:IAO_EXT_0001599 .  # UniProt isoform id field  
?uniprot_id_field rdfs:label ?accession_label .
bind (strbefore (str (?accession_label), \"-\") as ?main_label_1) .
bind (strlang (?main_label_1, \"en\") as ?main_label) .
?uniprot_accession_field rdfs:label ?main_label .
?uniprot_accession_field rdf:type ccp:IAO_EXT_0000240 .
?uniprot_record obo:BFO_0000051 ?uniprot_accession_field .
?uniprot_record rdf:type ccp:IAO_EXT_0000234 .
?uniprot_record obo:BFO_0000051 ?uniprot_sequence_record .
?uniprot_sequence_record rdf:type ccp:IAO_EXT_0001234 . # Uniprot sequence record
?uniprot_sequence_record obo:BFO_0000051 ?seq_field .
?seq_field rdf:type ccp:IAO_EXT_0001240 . # uniprot sequence record - value field value
?seq_field rdfs:label ?sequence_label .
bind (strlen (?sequence_label) AS ?seq_length) .  
}",
   :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}


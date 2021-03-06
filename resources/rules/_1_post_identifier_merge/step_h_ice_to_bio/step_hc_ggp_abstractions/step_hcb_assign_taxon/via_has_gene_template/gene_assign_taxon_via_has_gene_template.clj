;; --------------------------------------------------------------
;; --------- Uniprot SwissProt Protein Taxon Assignment ---------
;; --------------------------------------------------------------
`{:name "step-hcb_gene-assign-taxon-via-has-gene-template"
  :description "For a protein that has a taxon assigned, this rule looks for genes via the has_gene_template relation that are missing taxon assignment and infers their taxon to be the same as the protein for which they serve as a template."
  :head (
         ;; create a taxon restriction
         (?/restriction rdf/type owl/Restriction)
          (?/restriction owl/onProperty ?/only_in_taxon)
         (?/restriction owl/someValuesFrom ?/taxon)
         (?/gene rdfs/subClassOf ?/restriction))
  :reify ([?/restriction {:ln (:restriction)
                          :ns "kbio" :prefix "RS_"}])
  :body "PREFIX obo: <http://purl.obolibrary.org/obo/>
          PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
          prefix kice: <http://ccp.ucdenver.edu/kabob/ice/>
          PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
         select distinct ?gene ?taxon ?only_in_taxon {

              {
                select ?only_in_taxon {
                  kice:RO_0002160 obo:IAO_0000219 ?only_in_taxon .
                  filter (?only_in_taxon != obo:RO_0002160) .
                }
              }

              {
                select ?has_gene_template {
                  ?has_gene_template_id obo:IAO_0000219 <http://purl.obolibrary.org/obo/pr#has_gene_template> .
                  ?has_gene_template_id obo:IAO_0000219 ?has_gene_template .
                  # ensure it's a kabob bioentity (not an obo bioentity)
                  filter (contains (str(?has_gene_template), 'http://ccp.ucdenver.edu/kabob/bio/'))
                }
              }

              ?r owl:onProperty ?only_in_taxon .
              ?r owl:someValuesFrom ?taxon .
              ?protein rdfs:subClassOf ?r .
              ?protein rdfs:subClassOf ?r2 .
              ?r2 owl:onProperty ?has_gene_template .
              ?r2 owl:someValuesFrom ?gene .
              minus {
                    ?gene rdfs:subClassOf ?r3 .
                    ?r3 owl:onProperty ?only_in_taxon .
                }
              ?gene_id obo:IAO_0000219 ?gene .
              ?protein_id obo:IAO_0000219 ?protein .

          }"





   }
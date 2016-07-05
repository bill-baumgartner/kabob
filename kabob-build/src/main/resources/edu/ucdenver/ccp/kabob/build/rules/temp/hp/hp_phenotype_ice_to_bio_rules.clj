`{:name "hp-phentotype"
  :head (
         (?/pheno rdfs/subClassOf ?/hp)
         (?/gene rdfs/subClassOf ?/bioentity)

         (?/hr1 rdf/type owl/Restriction)
         (?/hr1 owl/onProperty obo/RO_0000057) ; has_participant
         (?/hr1 owl/someValuesFrom ?/gene)

         (?/pheno rdfs/subClassOf ?/hr1))

  :body ((?/geneIdField kiao/hasTemplate iaohp/HpAnnotationFileRecord_geneIdDataField1)
  (?/geneIdField obo/IAO_0000219 ?/geneIce)
  (?/geneIce obo/IAO_0000219 ?/bioentity)
  (?/record obo/BFO_0000051 ?/geneIdField)
  (?/record obo/BFO_0000051 ?/phenoIdField)
  (?/phenoIdField kiao/hasTemplate iaohp/HpAnnotationFileRecord_hpoTermDataField1)
  (?/phenoIdField obo/IAO_0000219 ?/phenoIce)
  (?/phenoIce obo/IAO_0000219 ?/hp))

  :reify ([?/pheno {:ln (:sha-1 ?/hp ?/hr1)
                 :ns "kbio" :prefix "PHENO_"}]
          [?/gene {:ln (:sha-1 ?/hp ?/bioentity)
                    :ns "kbio" :prefix "G_"}]
          [?/hr1 {:ln (:restriction)
                 :ns "kbio" :prefix "RESTR_"}])

  :options {:magic-prefixes [["franzOption_clauseReorderer" "franz:identity"]]}
  }

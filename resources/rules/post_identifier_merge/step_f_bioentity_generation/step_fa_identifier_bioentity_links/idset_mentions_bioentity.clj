

`{:name "idset-mentions-bioentity"
  :description "links identifier sets to bioentities using IAO:mentions"
  :head ((?/id_set obo/IAO_0000142 ?/entity)) ;; obo:mentions
  :reify ([?/entity {:ln (:sha-1 ?/id_set)
                     :ns "kbio" :prefix "B_" :suffix ""}])
  :sparql-string "prefix ccp: <http://ccp.ucdenver.edu/obo/ext/>
                  select ?id_set {
                  ?id_set rdf:type ccp:IAO_EXT_0000316 .
                  }"
  }


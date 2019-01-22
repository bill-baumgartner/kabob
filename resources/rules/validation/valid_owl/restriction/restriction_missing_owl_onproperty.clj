`{:name        "validation_restriction-missing-owl-onproperty-EXPECT-0"
  :description "tests that each owl restriction has at least one owl:onProperty property"
  :head ()
  :body "PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
  PREFIX franzOption_clauseReorderer: <franz:identity>
  select ?r
               { ?r rdf:type owl:Restriction .
                minus {?r owl:onProperty ?prop}
                }"

  }

`{:name        "validation_restriction-has-multiple-owl-onproperty-EXPECT-0"
  :description "tests that each owl restriction has no more than one owl:onProperty property"
  :head        ()
  :body "PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
  PREFIX franzOption_clauseReorderer: <franz:identity>
  select ?r
               { ?r rdf:type owl:Restriction .
                ?r owl:onProperty ?prop .
                ?r owl:onProperty ?prop2 .
                filter (?prop != ?prop2)
                }"

  }

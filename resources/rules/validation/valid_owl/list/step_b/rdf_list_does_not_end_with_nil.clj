`{:name        "validation_rdf-list-does-not-end-with-nil-EXPECT-0"
  :description "Tests RDF list nodes to ensure that lists end using rdf:nil"
  :head        ()
  :body "prefix ccp: <http://ccp.ucdenver.edu/obo/ext/>
  select ?list
               { ?list rdf:type rdf:List .
                  minus {
                    ?list rdf:type ccp:properlist .
                  }
                }"
  }

`{:name        "validation_rdf-list-does-not-end-with-nil-step_a"
  :description "Tests RDF list nodes to ensure that lists end using rdf:nil"
  :head        ((?/list rdf/type ccp/properlist))
  :body "select ?list {
                    ?list rdf:rest+ rdf:nil
                }"
  }

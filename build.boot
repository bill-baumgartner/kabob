(set-env! :dependencies '[[edu.ucdenver.ccp/kabob "2.0.0-SNAPSHOT"]])
(require '[clojure.java.io :refer [file]]
         '[clojure.string :refer [trim-newline]]
         '[kabob.build.run-rules :refer [commandline-process-forward-rules]]
         '[kabob.build.input-kb :refer [open-kb blazegraph-kb]]
         '[kr.core.kb :refer [kb]])
(import java.io.File
        java.io.BufferedReader
        java.io.FileReader
        com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager
        com.bigdata.rdf.sail.ExportKB)

(deftask test-connection
         "test connection to a triple store"
         [a allegrograph bool "Use an Allegrograph back end"
          ;b blazegraph bool "Use a Blazegraph back end" -- Blazegraph requires use of its dedicated branch b/c of sesame library dependency
          s stardog bool "Use a Stardog back end"
          n kb-name VAL str "KB name"
          l server-url VAL str "Server URL"
          u username VAL str "Server username"
          p password VAL str "Server password"]
         (with-pre-wrap fileset
                        ;; ensure that only one of the server back ends has been selected, otherwise error
                        (if (not= 1 (+ (if allegrograph 1 0)
                                       (if blazegraph 1 0)
                                       (if stardog 1 0)))
                          (throw (IllegalArgumentException.
                                   "More than one server implementation has been requested. Please select only one.")) true)
                        ;; add the kb-name and the server-implementation to the fileset and initialize server-specific parameters
                        (try
                          (let [
                                params {:repo-name   kb-name
                                        :username    username
                                        :password    password
                                        :server-url  server-url
                                        :server-impl (cond
                                                       allegrograph "allegrograph"
                                                       blazegraph "blazegraph"
                                                       stardog "stardog")}
                                kb (open-kb params)

                                ;kb (kb (.getBigdataSailRemoteRepository (.getRepositoryForNamespace (RemoteRepositoryManager. server-url) kb-name)))
                                ]
                            (println (str "params: " params))
                            (println (str "kb type: " (type kb)))) ; " initialized? " (.isInitialized kb))))
                          (catch Exception e (println "Caught Exception while testing kb connection: ")
                                             (.printStackTrace e)
                                             (println (str "Message: " (.getMessage e)))))
                        fileset))

(defn allegrograph-params []
  "define parameters specific to the Allegrograph implementation"
  (let [load-request-dir (file "/kabob-load-requests")
        container-name (trim-newline (slurp (file load-request-dir "agraph.container.name")))
        kb-port (trim-newline (slurp (file load-request-dir "agraph.port")))]
    {:kb-url              (str "http://" container-name ":" kb-port)
     :kb-user             "test"
     :kb-password         "xyzzy"
     :kb-data-directory   "/kabob_data/agraph-build"
     :kb-load-request-dir load-request-dir
     :kb-port             kb-port}))

(defn blazegraph-params []
  "define parameters specific to the Blazegraph implementation"
  (let [load-request-dir (file "/kabob-load-requests")
        container-name (trim-newline (slurp (file load-request-dir "blazegraph.container.name")))
        kb-port (trim-newline (slurp (file load-request-dir "blazegraph.port")))]
    {:kb-url              (str "http://" container-name ":" kb-port "/blazegraph")
     :kb-user             "admin"
     :kb-password         "admin"
     :kb-data-directory   "/kabob_data/blazegraph-build"
     :kb-load-request-dir load-request-dir
     :kb-port             kb-port}))

(defn stardog-params []
  "define parameters specific to the Stardog implementation"
  (let [load-request-dir (file "/kabob-load-requests")
        container-name (trim-newline (slurp (file load-request-dir "stardog.container.name")))
        kb-port (trim-newline (slurp (file load-request-dir "stardog.port")))]
    {:kb-url              (str "http://" container-name ":" kb-port)
     :kb-user             "admin"
     :kb-password         "admin"
     :kb-data-directory   "/kabob_data/stardog-build"
     :kb-load-request-dir load-request-dir
     :kb-port             kb-port}))

(deftask server
         "specify the triple store implementation that will be used and the name of the repository"
         [a allegrograph bool "Use an Allegrograph back end"
          ;b blazegraph bool "Use a Blazegraph back end" -- Blazegraph requires use of its dedicated branch b/c of sesame library dependency
          s stardog bool "Use a Stardog back end"
          n kb-name VAL str "KB name"]
         (with-pre-wrap fileset
                        ;; ensure that only one of the server back ends has been selected, otherwise error
                        (if (not= 1 (+ (if allegrograph 1 0)
                                       (if blazegraph 1 0)
                                       (if stardog 1 0)))
                          (throw (IllegalArgumentException.
                                   "Either no or more than one server implementation has been requested. Please select only one.")) true)
                        ;; add the kb-name and the server-implementation to the fileset and initialize server-specific parameters
                        (merge fileset {:kb-name               kb-name
                                        :kb-ontology-directory "/kabob_data/ontology"
                                        :kb-ice-directory      "/kabob_data/rdf"
                                        :kb-server-impl        (cond
                                                                 allegrograph :allegrograph
                                                                 blazegraph :blazegraph
                                                                 stardog :stardog)}
                               (cond
                                 allegrograph (allegrograph-params)
                                 blazegraph (blazegraph-params)
                                 stardog (stardog-params))
                               )))




(defn init-stardog-kb []
  "Stardog requires explicit creation of a KB before it is populated. This method
  submits a file to the Stardog load directory which causes a new KB to be initialized."
  (throw (UnsupportedOperationException. "The init-stardog-kb function has not been implemented.")))


(deftask init-kb []
         "initializes a new KB (if necessary)"
         (with-pre-wrap fileset
                        (println "Initializing the KB...")
                        (let [server-impl (:kb-server-impl fileset)]
                          (cond
                            (= server-impl :blazegraph) :default
                            (= server-impl :allegrograph) :default
                            (= server-impl :stardog) (init-stardog-kb)
                            :else (throw (IllegalArgumentException.
                                           (str "Unhandled server implementation: " server-impl))))
                          fileset)))


(deftask rule
         "Specify a rule to process"
         [p path-to-rule VAL str "path to rule file"]
         ;; eventually would be nice to add an argument to specify classpath or local path for rule location
         ;; and to be able to process single rules
         ;;l localpath       bool "rule path is a local path (by default the path is treated as the classpath)"]
         (fn middleware [next-handler]
           (fn handler [fileset]
             (next-handler (merge fileset {:path-to-rule path-to-rule})))))

(deftask print-rule []
         "prints the active rule"
         (with-pre-wrap fileset
                        (println (str "Active rule: " (:path-to-rule fileset)))
                        fileset))

(deftask run-rule []
         "Run a forward chaining rule"
         (with-pre-wrap fileset
             (let [output-dir (file (:kb-data-directory fileset) (:path-to-rule fileset))
                   rule (:path-to-rule fileset)
                   rulepath (.openStream (clojure.java.io/resource (:path-to-rule fileset)))
                   ]
               (println "running rule:" (:path-to-rule fileset) "\n"
                        "output directory:" (.getAbsolutePath output-dir) "\n"
                        "kb url:" (:kb-url fileset) "\n"
                        "kb name:" (:kb-name fileset) "\n"
                        "kb user:" (:kb-user fileset) "\n"
                        "kb password:" (:kb-password fileset) "\n"
                        "kb data-directory:" (:kb-data-directory fileset) "\n"
                        "kb server implementation:" (:kb-server-impl fileset) "\n"
                        "classpath ")
               (if-not rule
                 (do (boot.util/fail "There must be a rule specified prior to the run-rule task!") (*usage*)))
               (.mkdirs output-dir)
               (commandline-process-forward-rules `{;; The URL to the Rdf4j server to query
                                                    :server-url       ~(:kb-url fileset)
                                                    ;; The name of the repository to connect to
                                                    :repo-name        ~(:kb-name fileset)
                                                    ;; The username to use when connecting
                                                    :username         ~(:kb-user fileset)
                                                    ;; The password to use when connecting
                                                    :password         ~(:kb-password fileset)

                                                    ;; The output directory where generated triple files will be placed.
                                                    ;; IMPORTANT: The output directory path must end with a forward slash.
                                                    :output-directory ~(str (.getAbsolutePath output-dir) "/")

                                                    ;; name of the server implementation, e.g. stardog, blazegraph. Defaults to 'http'
                                                    :server-impl      ~(:kb-server-impl fileset)

                                                    ;; Names of the rule sets to use (must point to files available on the
                                                    ;; classpath; NOTE that it should not start with a forward slash)
                                                    :rule-set-names   #{~(:path-to-rule fileset)}}))
               fileset))

(defn get-load-file-prefix
  "return the file name prefix to use when invoking a load from the triple store"
  [file-to-load fileset format index]
  ;; file format: ${RULE_NAME}.port_${KB_PORT}.repo_${KB_NAME}.format_${FORMAT}.load
  (str (.getName (file file-to-load)) ".port_" (:kb-port fileset) ".repo_" (:kb-name fileset) ".format_" format "." index))

(defn load-files
  "loads the list of files specified by :files-to-load in the fileset"
  [fileset format]

  (doall (map-indexed (fn [index file-to-load] (let [load-request-dir (:kb-load-request-dir fileset)
                                                     load-file-prefix (get-load-file-prefix file-to-load fileset format index)
                                                     load-file (file load-request-dir (str load-file-prefix ".load"))
                                                     success-file (file load-request-dir (str load-file-prefix ".load.success"))
                                                     error-file (file load-request-dir (str load-file-prefix ".load.error"))
                                                     start-time (System/currentTimeMillis)
                                                     load-time-threshold-in-ms 1800000] ; 30 min

                                                 (println "loading file: " file-to-load)

                                                 ;; if old success or error files exist, delete them
                                                 (if (.exists load-file) (.delete load-file))
                                                 (if (.exists success-file) (.delete success-file))
                                                 (if (.exists error-file) (.delete error-file))

                                                 ;; submit the load request
                                                 (spit load-file file-to-load)

                                                 ;; then wait for the load to complete
                                                 (loop []
                                                   (Thread/sleep 10000) ;; check for the .success or .error files every 10s
                                                   (cond
                                                     (.exists success-file) (println "load success for " file-to-load)
                                                     (.exists error-file) (throw (IllegalStateException.
                                                                                   (str "Load failed for file:"
                                                                                        (trim-newline (slurp load-file)))))
                                                     (< load-time-threshold-in-ms
                                                        (- (System/currentTimeMillis) start-time)) (throw (IllegalStateException.
                                                                                                            (str "Halting load due to timeout (> 30 min) for file:"
                                                                                                                 (trim-newline (slurp load-file)))))
                                                     :else (recur)))))

                      (:files-to-load fileset))))

(deftask load-file-list
         "load a list of files into the KB"
         [l list-file VAL str "file containing list of files to load (one per line)"
          f format VAL str "format of files that will be loaded, e.g. ntriples"]
         (with-pre-wrap fileset
                        (load-files (merge fileset {:files-to-load (map (fn [line] (.trim line)) (line-seq (BufferedReader. (FileReader. list-file))))}) format)
                        ;; purposefully not including the :files-to-load in the fileset returned by this function since it's only needed for the load
                        fileset))

(deftask load-rule []
         "load output from a rule"
         (with-pre-wrap fileset
                        (let [rule-path (:path-to-rule fileset)
                              output-dir (file (:kb-data-directory fileset) rule-path)
                              rules (kabob.core.rule/kabob-load-rules-from-classpath rule-path)
                              files-to-load (map (fn [rule]
                                                   (.trim (.getAbsolutePath (file output-dir (str (:name rule) ".nt\n")))))
                                                 rules)
                              ]
                          (load-files (merge fileset {:files-to-load files-to-load}) "ntriples")
                          ;; purposefully not including the :files-to-load in the fileset returned by this function since it's only needed for the load
                          fileset)))



(deftask drop-rule []
         "drops the graph for the active rule"
         (with-pre-wrap fileset
                        (let [graph-name (str "<file://" (:data-directory fileset) rule-name ".nt>")]

                          )
                        fileset))


;; Blazegraph-specific tasks
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(deftask virtual-graph-gen []
         "create a virtual graph encompassing all named graphs so that queries can cross named graphs"
         (comp (rule :path-to-rule "rules/util/blazegraph/virtualgraph") (print-rule) (run-rule) (print-rule) (load-rule)))


;; KaBOB implementation tasks
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(deftask compile-ice-nt-file-list []
         "compile list of ICE ntriples files to load"
         (with-pre-wrap fileset
                        (merge fileset {:ice-nt-files (filter (fn [file] (.endsWith (.getName file) ".nt.gz"))
                                                              (file-seq (file (:kb-ice-directory fileset))))})))

(deftask compile-ice-owl-file-list []
         "compile lists of ICE RDF files to load"
         (with-pre-wrap fileset
                        (merge fileset {:ice-owl-files (filter (fn [file] (.endsWith (.getName file) ".owl"))
                                                               (file-seq (file (:kb-ice-directory fileset))))})))

(deftask compile-ontology-file-list []
         "compile lists of ontology files to load"
         (with-pre-wrap fileset
                        (merge fileset {:ontology-nt-files (filter (fn [file] (.endsWith (.getName file) ".owl.flattened.nt.noblank.nt"))
                                                                   (file-seq (file (:kb-ontology-directory fileset))))})))

(deftask load-ice-nt []
         "load the ICE ntriples files into the KB"
         (with-pre-wrap fileset
                        (load-files (merge fileset {:files-to-load (:ice-nt-files fileset)}) "ntriples")
                        ;; purposefully not including the :files-to-load in the fileset returned by this function since it's only needed for the load
                        fileset))

(deftask load-ice-owl []
         "load the ICE OWL files into the KB"
         (with-pre-wrap fileset
                        (load-files (merge fileset {:files-to-load (:ice-owl-files fileset)}) "rdfxml")
                        ;; purposefully not including the :files-to-load in the fileset returned by this function since it's only needed for the load
                        fileset))

(deftask load-ontology-nt []
         "load the processed ontologies into the KB"
         (with-pre-wrap fileset
                        (load-files (merge fileset {:files-to-load (:ontology-nt-files fileset)}) "ntriples")
                        ;; purposefully not including the :files-to-load in the fileset returned by this function since it's only needed for the load
                        fileset))

(deftask print-ontology-rdf-list []
         "prints the list of ontologies that will be loaded"
         (with-pre-wrap fileset
                        (println "----------- ONTOLOGY FILES -----------")
                        (doall (map #(println (.getAbsolutePath %)) (:ontology-nt-files fileset)))
                        (println "--------------------------------------")
                        fileset))

(deftask print-ice-rdf-list []
         "prints the list of ICE RDF files that will be loaded"
         (with-pre-wrap fileset
                        (println "----------- ICE FILES -----------")
                        (doall (map #(println (.getAbsolutePath %)) (concat (:ice-nt-files fileset)
                                                                            (:ice-owl-files fileset))))
                        (println "---------------------------------")
                        fileset))

(deftask load-ice-rdf []
         "load the ICE RDF into the KB"
         (comp (compile-ice-nt-file-list) (compile-ice-owl-file-list) (print-ice-rdf-list) (load-ice-nt) (load-ice-owl)))

(deftask load-ontology-rdf []
         "load the ICE RDF into the KB"
         (comp (compile-ontology-file-list) (print-ontology-rdf-list) (load-ontology-nt)))

(deftask validate-owl-restrictions []
         "run rules to validate ontology structures"
         (comp (rule :path-to-rule "rules/validation/valid_owl/restriction") (print-rule) (run-rule) (print-rule) (load-rule)))

(deftask validate-rdf-lists []
         "run rules to validate rdf:List structures"
         (comp (rule :path-to-rule "rules/validation/valid_owl/list/step_a") (print-rule) (run-rule) (print-rule) (load-rule)
               (rule :path-to-rule "rules/validation/valid_owl/list/step_b") (print-rule) (run-rule) (print-rule) (load-rule)))

(deftask validate-rdf-syntax []
         "run rules to validate RDF syntax"
         (comp (validate-owl-restrictions) (validate-rdf-lists)))





(deftask print-rule-twice []
         (comp (rule :path-to-rule "some/other/rule1") (print-rule) (rule :path-to-rule "some/other/rule2") (print-rule)))

(deftask print-rule-thrice []
         (comp (print-rule) (print-rule-twice)))






(deftask build-kabob []
         "build kabob from scratch. Choose a server then run this, e.g. boot server --allegrograph -n kabobhuman build-kabob"
         (comp (init-kb) (load-ontology-rdf) (validate-rdf-syntax)))




;(deftask serialize-kb
;         "specify the triple store implementation that will be used and the name of the repository"
;         [a allegrograph bool "Use an Allegrograph back end"
;          b blazegraph bool "Use a Blazegraph back end"
;          s stardog bool "Use a Stardog back end"
;          n kb-name VAL str "KB name"]
;         (with-pre-wrap fileset
;                        (let [journal
;                              include-inferred false])
;                        (ExportKB. connection output-directory RDFFormat/NQUADS include-inferred)
;                        fileset))
(defproject clj-proxytools "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.0.4"]
                 [clj-json "0.5.0"]
                 [enlive "1.0.0"]
                 [aleph "0.3.0-alpha2"]]
  :plugins      [[lein-ring "0.7.0"]]
  :main app.main
  :ring {:handler website.main/app})
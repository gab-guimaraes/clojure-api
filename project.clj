(defproject servico-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.route "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [org.clojure/data.json "0.2.6"]
                 [com.taoensso/faraday "1.11.4"]
                 [org.slf4j/slf4j-simple "1.7.28"]
                 [org.apache.kafka/kafka-clients "3.3.1"]]
  :java-source-paths ["src/servico_clojure/java"]
  :repl-options {:init-ns servico-clojure.core}
  :main ^:skip-aot servico-clojure.servidor)


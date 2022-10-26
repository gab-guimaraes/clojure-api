(ns servico-clojure.core)

(def myatom (atom 0))

(swap! myatom inc)
(swap! myatom inc)
(swap! myatom inc)
(swap! myatom inc)
(println @myatom)

(defn case-clojure []
  (case @myatom
    0 (println "o valor e 0")
    1 (println "o valor e 1")
    2 (println "o valor e 2")
    3 (println "o valor e 3")))


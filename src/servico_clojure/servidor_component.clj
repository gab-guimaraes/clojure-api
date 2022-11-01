(ns servico-clojure.servidor-component
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]))

(defrecord Servidor-Example [id]
  component/Lifecycle
  (start [this]
    (println "started... " id))

  (stop [this]
    (println "stoped...")))

(defn new-server [id]
  (map->Servidor-Example {:id id}))

(def servidor (atom nil))

(defrecord Servidor [service-map]
  component/Lifecycle
  (start [this]
    (println "Starting Server... 😃")
    (reset! servidor (http/create-server service-map))
    (http/start @servidor))
  (stop [this]
    (println "Stopping Server... 🥲")
    (when-let [s @servidor]
      (http/stop s))))

(defn new-server-component [service-map]
  (map->Servidor {:service-map service-map}))


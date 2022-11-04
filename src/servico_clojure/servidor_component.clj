(ns servico-clojure.servidor-component
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [servico-clojure.database :as database])
  [servico-clojure.database :as database])

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

(defrecord Database [conn]
  component/Lifecycle
  (start [this]
    (println "Starting Database... 🎲")
    (database/check-and-create-table-v2 conn))
  (stop [this]))

(defn new-database-component [conn]
  (map->Database {:conn conn}))

; criando component
;(defn example-system [config-options]
;  (let [{:keys [service-map]} config-options]
;    (component/system-map
;      :servidor (new-server-component config-options))))

(defn example-system [config-options]
  (component/system-map
    :database (new-database-component config-options)
    :servidor (component/using (new-server-component config-options) [:database])))

;cmd shift k / j
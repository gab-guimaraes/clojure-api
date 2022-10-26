(ns servico-clojure.database
  (:require [taoensso.faraday :as far])
  (:import (java.util UUID)))


(def task
  {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
   :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
   :endpoint   "http://localhost:8000"
   })

(far/delete-table task :my-table)

(far/create-table task :my-table
                  [:id :n]                                  ; Primary key named "id", (:n => number type)
                  {:throughput {:read 1 :write 1}           ; Read & write capacity (units/sec)
                   :block?     true                         ; Block thread during table creation
                   })

(defn insert-into-database [id tarefa name status]
  (println "creating item" id tarefa name status)
  (far/put-item task
                :my-table
                {
                 :id     id
                 :name   name
                 :tarefa tarefa
                 :status status}))

(defn get-item [id]
  (println "searching for " id)
  (far/get-item task :my-table {:id id}))

(defn get-all []
  (far/scan task :my-table))

;(far/get-item task :my-table {:id 46585})
;(get-item 46585)





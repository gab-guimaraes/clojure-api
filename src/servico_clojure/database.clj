(ns servico-clojure.database
  (:require [taoensso.faraday :as far]))

(def task {:access-key "<AWS_DYNAMODB_ACCESS_KEY>"
           :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
           :endpoint   "http://localhost:8000"})

(far/delete-table task :my-table)

(defn insert-into-database [id tarefa name status]
  (println "creating item" id tarefa name status)
  (far/put-item task
                :my-table {:id     id
                           :name   name
                           :tarefa tarefa
                           :status status}))

(defn get-item [id]
  (println "searching for " id)
  (far/get-item task :my-table {:id id}))

(defn get-all []
  (far/scan task :my-table))

(far/delete-table task :my-table)

(defn create-table-task []
  (far/create-table task :my-table
                    [:id :n]
                    {:throughput {:read 1 :write 1}
                     :block?     true}))

(defn check-table-v1 []
  (if (empty? (far/list-tables task))
    (create-table-task)
    (println "table already exists...")))

(defn check-and-create-table-v2 [conn]
  "consulta tabelas no database e verifica
  se tabela existe, se tabela nao existir, cria"
  (let [tables (far/list-tables conn)
        exist-table (filter #(= % :my-table) tables)]
    (if (empty? exist-table)
      (create-table-task)
      (println "table already exists..."))))

(defn test-filter-seq [param]
  "pequena func para testar filter"
  (println param)
  (if (= param :my-table)
    true
    false))

;(far/list-tables task)


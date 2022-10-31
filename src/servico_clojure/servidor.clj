(ns servico-clojure.servidor
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [servico-clojure.database :as database]))

(defn criar-tarefa-mapa [nome status]
  {:nome nome :status status})

(defn criar-tarefa [request]
  (let [id (rand-int 100000)
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa nome status)]
    (database/insert-into-database id tarefa nome status)
    {:status 200 :body {:mensagem "Tarefa registrada com sucesso!"
                        :tarefa   tarefa :id id}}))

(defn pesquisar-tarefa [request]
  (let [id (get-in request [:query-params :id])
        id-int (bigint id)
        response (database/get-item id-int)]
    (println ">>" id)
    (println ">>" response)
    {:status 200 :body {:payload response}}))

(defn pesquisar-todas [request]
  (let [response (database/get-all)]
    {:status 200 :body {:payload response}}))

(defn funcao-hello [request]
  {:status 200 :body "Hello world"})

(defn call-me-by-name [request]
  (let [body-req (slurp (:body request))]
    (println (get-in request [:body]))))

(def routes (route/expand-routes
              #{["/hello" :get funcao-hello :route-name :hello-world]
                ["/tarefa" :post criar-tarefa :route-name :criar-tarefa]
                ["/tarefa" :get pesquisar-tarefa :route-name :pesquisar-tarefa]
                ["/tarefa/all" :get pesquisar-todas :route-name :obter-todas]
                ["/call-me" :post call-me-by-name :route-name :call-me-baby]}))

(def service-map {::http/routes routes
                  ::http/port   9999
                  ::http/type   :jetty
                  ::http/join?  false})

(defn -main [& args]
  (http/start (http/create-server service-map))
  (println "server started... 😃"))
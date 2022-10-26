(ns servico-clojure.servidor
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.test :as test]
            [servico-clojure.database :as database]))

(defn -main [& args]
  (println "..."))

(def store (atom {}))

(defn lista-tarefas [request]
  {:status 200 :body @store})

(defn criar-tarefa-mapa [nome status]
  {:nome nome :status status})

(defn criar-tarefa-in-memory [request]                      ;request (payload)
  (let [
        nome (get-in request [:query-params :nome])
        status (get-in request [:query-params :status])
        tarefa (criar-tarefa-mapa nome status)]

    (swap! store assoc tarefa)
    {:status 200 :body {:mensagem "Tarefa registrada com sucesso!"
                        :tarefa   tarefa}}))

(defn criar-tarefa [request]
  (let [
        id (rand-int 100000)
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

;(println request)
;'(println (type (slurp (:body request))))
;(println (get-in request [:body]))


(def routes (route/expand-routes
              #{
                ["/hello" :get funcao-hello :route-name :hello-world]
                ["/tarefa" :post criar-tarefa :route-name :criar-tarefa]
                ["/tarefa" :get pesquisar-tarefa :route-name :pesquisar-tarefa]
                ["/tarefa/all" :get pesquisar-todas :route-name :obter-todas]
                ["/call-me" :post call-me-by-name :route-name :call-me-baby]
                }))

(def service-map {::http/routes routes
                  ::http/port   9999
                  ::http/type   :jetty
                  ::http/join?  false})

(def server (atom nil))

(reset! server (http/start (http/create-server service-map)))

;execute test (call api)
;(println (test/response-for (::http/service-fn @server) :get "/hello"))
;(println (test/response-for (::http/service-fn @server) :post "/tarefa?nome=Correr&status=pending"))
;(println (test/response-for (::http/service-fn @server) :post "/tarefa?nome=Pular&status=pending"))
;(println (test/response-for (::http/service-fn @server) :post "/tarefa?nome=Cagar&status=pending"))
;(println (test/response-for (::http/service-fn @server) :get "/tarefa"))

(println "server started... 😃")

;http://localhost:999/hello
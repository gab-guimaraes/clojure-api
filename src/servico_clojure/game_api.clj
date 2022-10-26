(ns servico-clojure.game-api
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.test :as test]))

(def game-list (atom {}))

(defn buscar-all [request]
  {:status 200 :body "all games"})

; nome (get-in request [:query-params :nome])
;( swap! store assoc uuid tarefa)
(defn create-game [request]
  (let [
        name (get-in request [:query-params :name])
        year (get-in request [:query-params :year])
        price (get-in request [:query-params :price])
        ]
    (swap! game-list assoc name year price)
    )
  )

(def routes (route/expand-routes #{
                                   ["/all-game" :get buscar-all :route-name :buscar-all]
                                   ["/game" :post create-game :route-name :create-game]
                                   }))

(def service-map {::http/routes routes
                  ::http/port   8080
                  ::http/type   :jetty
                  ::http/join?  false})

(def server (atom nil))

(reset! server (http/start (http/create-server service-map)))

(println (test/response-for (::http/service-fn @server) :post "/game?name=HarryPotter&year=2023&price=300"))
(println (test/response-for (::http/service-fn @server) :post "/game?name=GranTurismo6&year=2022&price=200"))

(println game-list)
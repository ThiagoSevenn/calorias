;; ;; FEITO
(ns calorias.requisicoes.reqAlimentos
    (:require [clj-http.client :as client]
              [cheshire.core :as json]
              [calorias.auxiliares.auxiliares :refer :all]))

;; Requisição de um Alimento
(defn reqAlimento [query quantidade data]
  (let [chave "EBdluKfUG3sH5qluJ5EBGA==s4on7qkhRmy87LkN"
        url "https://api.calorieninjas.com/v1/nutrition"
        resposta (client/get url
                             {:headers {"X-Api-Key" chave}
                              :query-params {"query" query}
                              :as :json})
        alimento (:body resposta)
        nomeECaloria (first (map #(select-keys % [:name :calories]) (:items alimento)))]        
    {:tipo "ganho" :nome (traduzir-para-pt (:name nomeECaloria)) :quantidade quantidade :calorias (:calories nomeECaloria) :data data}))

(defn registrar-alimento [query-bruta]
  (let [quantidade (:quantidade query-bruta)
        nome (:nome query-bruta)
        data (:data query-bruta)
        query (format "%d g %s" quantidade nome)
        alimento (reqAlimento query quantidade data)]
     (req-post (endereco-para "/transacoes")(conteudo-como-json alimento))))
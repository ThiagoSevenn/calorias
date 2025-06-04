;; ;; FEITO
;; (ns calorias.requisicoes.reqAlimentos
;;     (:require [clj-http.client :as client]
;;               [cheshire.core :as json]))

;; ;; Requisição de um Alimento
;; (defn reqAlimento [query data]
;;   (let [chave "EBdluKfUG3sH5qluJ5EBGA==s4on7qkhRmy87LkN"
;;         url "https://api.calorieninjas.com/v1/nutrition"
;;         resposta (client/get url
;;                              {:headers {"X-Api-Key" chave}
;;                               :query-params {"query" query}
;;                               :as :json})
;;         alimento (:body resposta)
;;         nomeECaloria (first (map #(select-keys % [:name :calories]) (:items alimento)))]        
;;     {:tipo "ganho" :nome (:name nomeECaloria) :valor (:calories nomeECaloria) :data data}))

;; ;; FEITO
;; (ns calorias.requisicoes.reqExercicios
;;     (:require [clj-http.client :as client]
;;               [cheshire.core :as json]))

;; ;; Requisição de um Exércicio
;; (defn reqExercicio [atividade pesoKg data]
;;   (let [chave "EBdluKfUG3sH5qluJ5EBGA==cPwFqmDHaVrKUn5X"
;;         url "https://api.api-ninjas.com/v1/caloriesburned"
;;         pesoLibras (/ pesoKg 0.454)
;;         resposta (client/get url
;;                              {:headers {"X-Api-Key" chave}
;;                               :query-params {"activity" atividade
;;                                              "weight" pesoLibras}
;;                               :as :json})
;;         exercicio (:body resposta)
;;         nomeECaloria (first (map #(select-keys % [:name :calories_per_hour]) exercicio))]
;;         {:tipo "perda" :nome (:name nomeECaloria) :valor (:calories_per_hour nomeECaloria) :data data}))
;; ;; FEITO
(ns calorias.requisicoes.reqExercicios
    (:require [clj-http.client :as client]
              [cheshire.core :as json]
              [calorias.auxiliares.auxiliares :refer :all]
              [calorias.db.db :as db]))

;; Requisição de um Exércicio
(defn reqExercicio [atividade pesoKg tempo data]
  (let [chave "EBdluKfUG3sH5qluJ5EBGA==cPwFqmDHaVrKUn5X"
        url "https://api.api-ninjas.com/v1/caloriesburned"
        pesoLibras (/ pesoKg 0.454)
        resposta (client/get url
                             {:headers {"X-Api-Key" chave}
                              :query-params {"activity" atividade
                                             "weight" pesoLibras
                                             "duration" (str tempo)}
                              :as :json})
        exercicio (:body resposta)
        nomeECaloria (first (map #(select-keys % [:name :total_calories]) exercicio))]
        {:tipo "perda" :nome (traduzir-para-pt (:name nomeECaloria)) :tempo tempo :calorias (:total_calories nomeECaloria) :data data}))

(defn registrar-exercicio [query-bruta]
  (let [atividade (:atividade query-bruta)
        peso (:peso (first (db/info-usuario)))
        data (:data query-bruta)
        tempo (:tempo query-bruta)
        exercicio (reqExercicio atividade peso tempo data)]
     (req-post (endereco-para "/transacoes")(conteudo-como-json exercicio))))
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
                              :query-params {"activity" (str atividade)
                                             "weight" (str pesoLibras)
                                             "duration" (str tempo)}
                              :as :json})
        exercicio (:body resposta)
        nomeECaloria (first (map #(select-keys % [:name :total_calories]) exercicio))]
        (println exercicio)
        {:tipo "perda" :nome (traduzir-para-pt (:name nomeECaloria)) :tempo tempo :calorias (:total_calories nomeECaloria) :data data}))

(defn formatar-atividade [atividade contador]
  {:id contador :nome (traduzir-para-pt (:name atividade))})

;; enviar 5 primeiros
(defn req-cinco-exercicios [atividade]
  (let [chave "EBdluKfUG3sH5qluJ5EBGA==cPwFqmDHaVrKUn5X"
        url "https://api.api-ninjas.com/v1/caloriesburned"
        resposta (client/get url
                             {:headers {"X-Api-Key" chave}
                              :query-params {"activity" atividade}
                              :as :json})
        exercicioBody (:body resposta)
        exercicios (map #(select-keys % [:name]) exercicioBody)
        contador (range 1 6 1)
        escolherExercicio (map formatar-atividade exercicios contador)]  
        escolherExercicio))

(defn registrar-cinco-exercicios [query-bruta]
  (let [atividade (:atividade query-bruta)
        exercicios (req-cinco-exercicios atividade)]
      (db/cadastrar-possiveis-exercicios exercicios)))

(defn registrar-exercicio [query-bruta]
  (let [atividade (:atividade query-bruta)
        peso (:peso (first (db/info-usuario)))
        data (:data query-bruta)
        tempo (:tempo query-bruta)
        exercicio (reqExercicio atividade peso tempo data)]
     (req-post (endereco-para "/transacoes")(conteudo-como-json exercicio))))
(ns calorias.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core	:as	json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json	:refer	[wrap-json-body]]

            ;; bd e validação
            [calorias.db.db :as db]
            [calorias.db.transacoes	:as	transacoes]

            ;; api-alimento e api-exercicio
            [calorias.requisicoes.reqAlimentos :as alimentos]
            [calorias.requisicoes.reqExercicios :as exercicios]))

(defn como-json [conteudo &	[status]]
    {:status	(or	status	200)
     :headers {"Content-Type"
                "application/json; charset=utf-8"}
     :body (json/generate-string conteudo)})

(defroutes app-routes
  (GET "/" [] "Olá, mundo!")
  (GET "/usuario" [] (como-json {:usuario (db/info-usuario)}))
  ;; exibe o saldo
  (GET "/saldo" [] (como-json {:saldo (db/saldo)}))
  
  ;; consulta alimentos e/ou exercicios (por periodo ou total)
  (GET "/alimentos" [] (como-json {:alimentos (db/consultarEspecifico "ganho")}))
  (GET "/exercicios" [] (como-json {:exercicios (db/consultarEspecifico "perda")}))
  (GET "/registros" [] (como-json {:registros (db/consultarGeral)}))
  (GET "/resumo/periodo" [] (como-json (db/consultarGeral)))

  (POST "/exercicios" requisicao (-> (exercicios/registrar-exercicio (:body requisicao))
                                    (como-json 201)))
  (POST "/alimentos" requisicao (-> (alimentos/registrar-alimento (:body requisicao))
                                    (como-json 201)))
  (POST "/usuario" requisicao (-> (db/cadastrar-usuario (:body requisicao))
                                  (como-json 201)))

  (POST "/transacoes"	requisicao
		(if	(transacoes/valida?	(:body requisicao))
				(->	(db/cadastrar	(:body requisicao))
						(como-json	201))
				(como-json	{:mensagem	"Requisição	inválida"}	422)))

  (route/not-found "Recurso não encontrado"))

(def app
  (->(wrap-defaults app-routes api-defaults)
     (wrap-json-body {:keywords? true	:bigdecimals?	true})))

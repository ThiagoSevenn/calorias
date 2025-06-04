(ns calorias.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core	:as	json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json	:refer	[wrap-json-body]]
            [calorias.db.db :as db]
            [calorias.db.transacoes	:as	transacoes]))

(defn como-json [conteudo &	[status]]
    {:status	(or	status	200)
     :headers {"Content-Type"
                "application/json; charset=utf-8"}
     :body (json/generate-string conteudo)})

(defroutes app-routes
  (GET "/" [] "Olá, mundo!")

  ;; exibe o saldo
  (GET "/saldo" [] (como-json {:saldo (db/saldo)}))
  
  ;; consulta alimentos e/ou exercicios (por periodo ou total)
  (GET "/alimentos" [] (como-json {:transacoes (db/consultarEspecifico "ganho")}))
  (GET "/exercicios" [] (como-json {:transacoes (db/consultarEspecifico "perda")}))
  (GET "/resumo/total" [] (como-json {:transacoes (db/consultarGeral)}))
  (GET "/resumo/periodo" [] (como-json (db/consultarGeral)))

  (POST "/usuario" requisicao 
    (if (empty? (db/info-usuario))
        (-> (db/cadastrar-usuario (:body requisicao))
            (como-json 201))
        (como-json {:mensagem "Não foi possivel cadastrar o usuário."} 422)))

  (POST "/transacoes"	requisicao
		(if	(transacoes/valida?	(:body requisicao))
				(->	(db/cadastrar	(:body requisicao))
						(como-json	201))
				(como-json	{:mensagem	"Requisição	inválida"}	422)))

  (route/not-found "Recurso não encontrado"))

(def app
  (->(wrap-defaults app-routes api-defaults)
     (wrap-json-body {:keywords? true	:bigdecimals?	true})))

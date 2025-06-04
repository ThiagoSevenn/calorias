(ns calorias.handler-test
  (:require [midje.sweet	:refer	:all]
            [ring.mock.request :as mock]
            [cheshire.core	:as	json]
            [calorias.handler :refer :all]
            [calorias.db.db :as db]))

(facts "Dá	um	'Olá,	mundo!'	na	rota	raiz"
		(let	[response	(app	(mock/request	:get	"/"))]
      (fact "o	status	da	reposta	é	200"
        (:status	response)	=>	200)
        
		  (fact "o	texto	do	corpo	é	'Olá,	mundo!'"
						(:body	response)	=>	"Olá, mundo!")))
				
(facts "Rota	inválida	não	existe"
    (let	[response	(app	(mock/request	:get	"/invalid"))]
      (fact "o	código	de	erro	é	404"
              (:status	response)	=>	404)

      (fact "o	texto	do	corpo	é	'Recurso	não	encontrado'"
              (:body	response)	=>	"Recurso não encontrado")))

(facts "Saldo	inicial	é	0"
    ;; Um mock criado.
		(against-background	[(json/generate-string	{:saldo	0}) =>	"{\"saldo\":0}"
                         (db/saldo)	=>	0])

		(let	[response	(app	(mock/request	:get	"/saldo"))]
        (fact "o formato é 'aplication/json"
          (get-in response [:headers "Content-Type"]) => 	"application/json; charset=utf-8")
				(fact "o	status	da	resposta	é	200"
						(:status response)	=>	200)
				(fact	"o texto do	corpo	é	um	JSON	cuja	chave	é	saldo	e	o	valor	é	0"
            (:body response)	=>	"{\"saldo\":0}")))

(facts "Registra	uma	receita	no	valor	de	10"
  ;;	cria	um	mock	para	a	função	db/registrar
  (against-background	(db/cadastrar	{:data "data" :nome "nome" :valor	10 :tipo "ganho"})
      =>	{:id	1	:data "data" :nome "nome" :valor 10 :tipo	"ganho"})

  (let	[response (app	(->	(mock/request	:post	"/transacoes")
      ;;	cria	o	conteúdo	do	POST
                            (mock/json-body	{:data "data" :nome "nome" :valor	10 :tipo	"ganho"})))]
      (fact "o	status	da	resposta	é	201"
                  (:status	response)	=>	201)
      (fact "o	texto	do	corpo	é	um	JSON com	o	conteúdo	enviado	e	um	id"
                  (:body	response)	=>
                      "{\"id\":1,\"data\":\"data\",\"nome\":\"nome\",\"valor\":10,\"tipo\":\"ganho\"}")))


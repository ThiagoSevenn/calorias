(ns	calorias.saldo-aceitacao-test
		(:require	[midje.sweet :refer	:all]
              [cheshire.core	:as	json]
              [calorias.auxiliares :refer	:all]
              [clj-http.client :as http]
              [calorias.db.db :as db]))

(against-background	[(before :facts	[(iniciar-servidor	porta-padrao)
                                      (db/limpar-registros)])
                      (after :facts	(parar-servidor))]
		(fact "O	saldo	inicial	é	0"	:aceitacao
				(json/parse-string (conteudo "/saldo") true)	=>	{:saldo 0})

    (fact "Add uma receita de valor 10" :aceitacao
        (http/post (endereco-para "/transacoes")(ganho "data" "nome" 10))
        (json/parse-string (conteudo "/saldo" ) true) => {:saldo 10})
      
    (fact "O	saldo	é	1000	quando	criamos	duas receitas de	2000 e uma	despesa	da	3000"	:aceitacao
				(http/post	(endereco-para "/transacoes")(ganho "data" "nome" 2000))
				(http/post	(endereco-para "/transacoes")(ganho "data" "nome" 2000))
				(http/post	(endereco-para "/transacoes")(perda "data" "nome" 3000))
				(json/parse-string	(conteudo "/saldo")	true)	=>	{:saldo	1000}))

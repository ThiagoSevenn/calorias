(ns	calorias.filtros-aceitacao-test
        (:require	[midje.sweet	:refer	:all]
              [cheshire.core	:as	json]
              [calorias.auxiliares	:refer	:all]
              [clj-http.client	:as	http]
              [calorias.db.db	:as	db]))

(def	transacoes-aleatorias
        '({:data "data" :nome "nome" :valor	7.0M	:tipo	"perda"}
      {:data "data" :nome "nome" :valor	88.0M	:tipo	"perda"}
      {:data "data" :nome "nome" :valor	106.0M	:tipo	"perda"}
      {:data "data" :nome "nome" :valor	8000.0M	:tipo	"ganho"}))


(against-background	[(before	:facts [(iniciar-servidor	porta-padrao)
                                  (db/limpar-registros)])
                (after	:facts	(parar-servidor))]

  (fact "Não	existem	receitas"	:aceitacao
      (json/parse-string	(conteudo "/alimentos")	true)
          =>	{:transacoes	'()})
  (fact "Não	existem	despesas"	:aceitacao
      (json/parse-string	(conteudo "/exercicios")	true)
          =>	{:transacoes	'()})
  (fact "Não	existem	transacoes"	:aceitacao
      (json/parse-string	(conteudo "/resumo/total")	true)
          =>	{:transacoes	'()})

  (against-background
      [(before :facts	(doseq [transacao	transacoes-aleatorias]
                    ;;	^^^^^	este	doseq	é	novidade!
                              (db/cadastrar	transacao)))
        (after :facts	(db/limpar-registros))]
      (fact "Existem	3	despesas"	:aceitacao
          (count	(:transacoes	(json/parse-string
              (conteudo "/exercicios")	true)))	=>	3)

      (fact "Existe	1	receita"	:aceitacao
          (count	(:transacoes	(json/parse-string
              (conteudo "/alimentos")	true)))	=>	1)

      (fact "Existem	4	transações"	:aceitacao
          (count	(:transacoes	(json/parse-string
              (conteudo "/resumo/total") true)))	=>	4)))

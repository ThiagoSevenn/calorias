(ns	calorias.db-test
		(:require	[midje.sweet	:refer	:all]
							[calorias.db.db	:refer	:all]))

(facts "Guarda	uma	transação	num	átomo"
	(against-background	[(before	:facts	(limpar-registros))]

		(fact "a	coleção	de	transações	inicia	vazia"
				(count	(consultarGeral))	=>	0)
		(fact "a	transação	é	o	primeiro	registro"
				(cadastrar	{:data "data" :nome "nome" :valor	7	:tipo	"ganho"})
						=>	{:id	1 :data "data" :nome "nome"	:valor	7	:tipo	"ganho"}
				(count	(consultarGeral))	=>	1)))

(facts "Calcula	o	saldo	dada	uma	coleção	de	transações"
		(against-background	[(before :facts	(limpar-registros))]

				(fact "saldo	é	positivo	quando	só	tem	ganho"
						(cadastrar	{:data "data" :nome "nome" :valor	1	:tipo	"ganho"})
						(cadastrar	{:data "data" :nome "nome" :valor	10	:tipo	"ganho"})
            (cadastrar	{:data "data" :nome "nome" :valor	100	:tipo	"ganho"})
						(cadastrar	{:data "data" :nome "nome" :valor	1000	:tipo	"ganho"})
						(saldo)	=>	1111)

				(fact "saldo	é	negativo	quando	só	tem	despesa"
						(cadastrar	{:data "data" :nome "nome" :valor	2	:tipo	"perda"})
						(cadastrar	{:data "data" :nome "nome" :valor	20	:tipo	"perda"})
						(cadastrar	{:data "data" :nome "nome" :valor	200	:tipo	"perda"})
						(cadastrar	{:data "data" :nome "nome" :valor	2000	:tipo	"perda"})
						(saldo)	=>	-2222)

				(fact "saldo	é	a	soma	das	receitas	menos	a	soma	das	despesas"
						(cadastrar	{:data "data" :nome "nome" :valor	2	:tipo	"perda"})
						(cadastrar	{:data "data" :nome "nome" :valor	10	:tipo	"ganho"})
						(cadastrar	{:data "data" :nome "nome" :valor	200	:tipo	"perda"})
						(cadastrar	{:data "data" :nome "nome" :valor	1000	:tipo	"ganho"})
						(saldo)	=>	808)))


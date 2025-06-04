(ns	calorias.transacoes-test
		(:require	[midje.sweet	:refer	:all]
							[calorias.db.transacoes	:refer	:all]))

(fact "Uma transação sem data não é valida"
	(valida? {:nome "nome" :valor	230	:tipo	"ganho" }) => false)

(fact "Uma transação sem nome não é valida"
	(valida? {:data "data" :valor	230	:tipo	"ganho" }) => false)

(fact "Uma	transação	sem	valor	não	é	válida"
  (valida?	{:data "data" :nome "nome" :tipo	"ganho"})	=>	false)

(fact "Uma	transação	com	valor	negativo	não	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	-10	:tipo	"ganho"})	=>	false)

(fact "Uma	transação	com	valor	não	numérico	não	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	"mil"	:tipo	"ganho"})	=>	false)

(fact "Uma	transação	sem	tipo	não	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	90})	=>	false)

(fact "Uma	transação	com	tipo	desconhecido	não	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	8	:tipo	"investimento"})	=>	false)

(fact "Uma	transação	com	valor	numérico	positivo e	com	tipo	conhecido	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	230	:tipo	"ganho"})	=>	true)

(fact "Uma	transação	com	valor	numérico	positivoe	com	tipo	conhecido	é	válida"
	(valida?	{:data "data" :nome "nome" :valor	230	:tipo	"perda"})	=>	true)
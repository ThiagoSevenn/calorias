(ns calorias.auxiliares.auxiliares
		(:require [cheshire.core	:as	json]
              [clj-http.client	:as	http]))

(def porta-padrao 3001)

(defn endereco-para	[rota] 
  (str "http://localhost:"
        porta-padrao rota))

(def requisicao-para (comp http/get endereco-para))

(defn conteudo [rota] (:body (requisicao-para rota)))

(defn	conteudo-como-json	[transacao]
		{:content-type	:json
			:body	(json/generate-string	transacao)
			:throw-exceptions	false})

(defn	perda	[data nome valor]
		(conteudo-como-json	{:data data :nome nome :valor	valor	:tipo	"perda"}))

(defn	ganho	[data nome valor]
		(conteudo-como-json	{:data data :nome nome :valor valor	:tipo	"ganho"}))

(defn req-post [endereco dado]
	(http/post endereco dado))
(ns calorias.auxiliares.auxiliares
		(:require [cheshire.core	:as	json]
              [clj-http.client	:as	http]))

(def porta-padrao 3000)

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
		(conteudo-como-json	{:data data :nome nome :calorias	valor	:tipo	"perda"}))

(defn	ganho	[data nome valor]
		(conteudo-como-json	{:data data :nome nome :calorias valor	:tipo	"ganho"}))

(defn req-post [endereco dado]
	(http/post endereco dado))

(defn traduzir-para-pt [entrada]
  (let [url (format "https://api.mymemory.translated.net/get" )
				parametros {:query-params {"q" entrada
																	"langpair" "en|pt"}
										:as :json}
        resposta (http/get url parametros)
				traducao-bruta (:responseData (:body resposta))
				traducao (:translatedText traducao-bruta)]
        traducao))	

(defn traduzir-para-en [entrada]
  (let [url (format "https://api.mymemory.translated.net/get" )
				parametros {:query-params {"q" entrada
																	"langpair" "pt|en"}
										:as :json}
        resposta (http/get url parametros)
				traducao-bruta (:responseData (:body resposta))
				traducao (:translatedText traducao-bruta)]
        traducao))	
;; FEITO!!  
(ns calorias.db.db
    (:require [clojure.string :as str]))

;; usuario
(def usuario (atom []))

(defn limpar-usuario []
    (reset! usuario []))

(defn cadastrar-usuario [dados]
    (limpar-usuario)
    (swap! usuario conj dados))

(defn info-usuario []
    @usuario)

;; registros
;; transforma a data em um tipo int formatado
(defn formatar-data [dataBruta]
    (let[data (reverse (str/split dataBruta #"/"))
         vetorInt (doall (map #(Integer/parseInt %) data))
         vetorMultiplicador [10000 100 1]
         dataInt (apply + (doall (map #(* %1 %2) vetorInt vetorMultiplicador)))]
        dataInt))

;; inicializa um atom
(def registros (atom []))

;; reseta o atom
(defn limpar-registros []
    (reset! registros []))

;; filtro em registros de acordo com o tipo
(defn filtro [tipo]
    (filter #(= tipo (:tipo %)) @registros))

;; cadastra um registro e adiciona seu id
(defn cadastrar [objeto]
    (let [colecao-atualizada (swap! registros conj objeto)]
        (merge objeto {:id (count colecao-atualizada)})))

;; saldo de caloria (quantidade ingerida - quantidade gasta)
(defn saldo []
    (if (empty? @registros)
        0
        (let [ganhoCalorico (apply + (doall (map :calorias (filtro "ganho"))))
              perdaCalorica (apply + (doall (map :calorias (filtro "perda"))))]
            (- ganhoCalorico perdaCalorica))))

;; consultar dados 
(defn adicionar-id [id dado]
    (merge dado {:id id}))

(defn consultarGeral []
    (let [colecao @registros
          quantidade (+ 1 (count colecao))
          contador (range 1 quantidade 1)]
        (doall (map adicionar-id contador colecao))))

(defn consultarEspecifico [tipo]
    (let [colecao (filtro tipo)
          quantidade (+ 1 (count colecao))
          contador (range 1 quantidade 1)]
        (doall (map adicionar-id contador colecao))))

        
;; FEITO!!
(ns calorias.db.transacoes)

;; validar o registro de refeição ou exercício
(defn valida? [registro]
    (and (contains? registro :nome)
         (contains? registro :calorias)
         (contains? registro :tipo)
         (contains? registro :data)
         (number? (registro :calorias))
         (pos? (registro :calorias))
         (or (= "ganho" (:tipo registro))
             (= "perda" (:tipo registro)))))
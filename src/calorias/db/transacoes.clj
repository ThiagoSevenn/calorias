;; FEITO!!
(ns calorias.db.transacoes)

;; validar o registro de refeição ou exercício
(defn valida? [registro]
    (and (contains? registro :nome)
         (contains? registro :valor)
         (contains? registro :tipo)
         (contains? registro :data)
         (number? (registro :valor))
         (pos? (registro :valor))
         (or (= "ganho" (:tipo registro))
             (= "perda" (:tipo registro)))))
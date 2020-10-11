(ns dpgurl.counter)

(def counter (atom 2147483647))
(defn get-counter [] (deref counter))
(defn inc-counter [] (swap! counter inc))

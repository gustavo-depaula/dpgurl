(ns dpgurl.core
  (:import java.util.Base64))

(def counter (atom 2147483647))
(def db (atom {}))

(defn decimal->bitstring [n] ( Long/toString n 2))
(defn bitstring->list-of-6-chars-bitstrings [bitstring]
  (re-seq #".{1,6}" bitstring))
(defn bitstring-list->decimal-list [bitstrings]
  (map #(Integer/parseInt %1 2) bitstrings))
(def base64-map (re-seq #".{1,1}" "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-"))
(defn decimal-list->base64-string [decimal-list]
  (clojure.string/join
   (map #(nth base64-map %1) decimal-list)))

(defn number-string->base64-string [str]
  (->> str
       (decimal->bitstring)
       (bitstring->list-of-6-chars-bitstrings)
       (bitstring-list->decimal-list)
       (decimal-list->base64-string)
       (clojure.string/join)))

(defn create-short-url [long-url]
  (swap! counter inc)
  (let [counter-value (deref counter)
        short-url (number-string->base64-string counter-value)]
    (swap!
     db
     #(assoc %1 short-url long-url))
    short-url))

(defn short-code->long-url [short-code]
  (let [db-value (deref db)]
    (get db-value short-code)))

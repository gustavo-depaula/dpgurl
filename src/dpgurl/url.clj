(ns dpgurl.url
  (:import java.util.Base64))

(defn- decimal->bitstring [n] ( Long/toString n 2))
(defn- bitstring->list-of-6-chars-bitstrings [bitstring]
  (re-seq #".{1,6}" bitstring))
(defn- bitstring-list->decimal-list [bitstrings]
  (map #(Integer/parseInt %1 2) bitstrings))
(def base64-map (re-seq #".{1,1}" "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-"))
(defn- decimal-list->base64-string [decimal-list]
  (clojure.string/join
   (map #(nth base64-map %1) decimal-list)))

(defn- number-string->base64-string [str]
  (->> str
       (decimal->bitstring)
       (bitstring->list-of-6-chars-bitstrings)
       (bitstring-list->decimal-list)
       (decimal-list->base64-string)
       (clojure.string/join)))

(defn make-short-url [counter]
  (number-string->base64-string counter))

(ns dpgurl.db
  (:require [taoensso.carmine :as car :refer [wcar]]))

(def redis-url (System/getenv "REDIS_URL"))

;; Local, in-memory db if REDIS_URL not present
;; no support for expirable links
(def db (atom {}))

(defn- local-db-get-long-url [short]
  (let [db-value (deref db)
        long-url (get db-value short)]
    long-url))

(defn- local-db-insert-url-into-db [short long]
  (swap! db assoc short long))

;; Redis db
;; uses carmine https://github.com/ptaoussanis/carmine
(def server1-conn {:pool {} :spec {:uri redis-url}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn- redis-get-long-url [short]
  (wcar* (car/get short)))

(def day-in-seconds (* 24 60 60))
(def expire-short-url-time day-in-seconds)
(defn- redis-insert-url-into-db [short long]
  (wcar*
   (car/setnx short long)
   (car/expire short expire-short-url-time)))

;; public interface
(defn get-long-url [short]
  (if (nil? redis-url)
    (local-db-get-long-url short)
    (redis-get-long-url short)))

(defn insert-url-into-db [short long]
  (if (nil? redis-url)
    (local-db-insert-url-into-db short long)
    (redis-insert-url-into-db short long)))

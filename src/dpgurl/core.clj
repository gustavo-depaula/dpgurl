(ns dpgurl.core
  (:require [dpgurl.url :refer [make-short-url]]
            [dpgurl.db :refer [insert-url-into-db get-long-url]]
            [dpgurl.counter :refer [get-counter inc-counter]]))

(defn create-short-url-workflow
  [req]
  (let [long-url (get (:body req) :link)
        counter (get-counter)
        short-url (make-short-url counter)]
    (do (inc-counter)
        (insert-url-into-db short-url long-url))
    {:status 200 :body {:short-url short-url
                        :long-url long-url}}))


(defn get-long-url-workflow
  [short]
  (let [long-url (get-long-url short)]
    {:status 301 :headers {"location" long-url}}))

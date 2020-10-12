(ns dpgurl.core
  (:require [dpgurl.url :refer [make-short-url]]
            [dpgurl.db :refer [insert-url-into-db get-long-url]]))

(defn create-short-url-workflow
  [req]
  (let [long-url (get (:body req) :link)
        short-url (make-short-url)
        operation-result (insert-url-into-db short-url long-url)]
    (if (= operation-result 0)
      (create-short-url-workflow req)
      {:status 200 :body {:short-url short-url
                          :long-url long-url}})))

(defn get-long-url-workflow
  [short]
  (let [long-url (get-long-url short)]
    (if (nil? long-url)
      {:status 404} ;; TODO: handle 404 beautifully
      {:status 302 :headers {"location" long-url}})))

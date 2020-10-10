(ns dpgurl.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [dpgurl.core :refer [create-short-url short-code->long-url]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (POST "/url" req
        (let [long-url (get (:body req) :link)
              short-url (create-short-url long-url)]
          {:status 200 :body {:short-url short-url
                              :long-url long-url}}))
  (GET "/:short" [short]
       (let [long-url (short-code->long-url short)]
         {:status 200 :body {:long-url long-url}}))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

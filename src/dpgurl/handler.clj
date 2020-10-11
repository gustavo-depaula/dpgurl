(ns dpgurl.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [dpgurl.core :refer [create-short-url-workflow get-long-url-workflow]]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (POST "/url" req
        (create-short-url-workflow req))
  (GET "/:short" [short]
       (get-long-url-workflow short))
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response))

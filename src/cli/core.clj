(ns cli.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]))

(defn fetch-short-url [long]
  (let [url "https://dpgurl.herokuapp.com/url"
        body (format "{ \"link\": \"%s\" }" long)
        req-opts {:content-type :json
                  :body body}
        response (client/post url req-opts)
        res-body (parse-string (:body response))]
    res-body))

(defn print-url-pair [url-pair]
  (let [short-url (get url-pair "short-url")
        long-url (get url-pair "long-url")]
    (println (format "New short url! %s%s ~> %s"
                     "https://dpgurl.herokuapp.com/"
                     short-url
                     long-url))))

(defn -main
  [& args]
  (let [longs args
        url-pairs (map fetch-short-url longs)]
    (run! print-url-pair url-pairs)))

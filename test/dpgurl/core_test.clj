(ns dpgurl.core-test
  (:require [clojure.test :refer :all]
            [dpgurl.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as cheshire]))

(deftest happy-paths
  (def long-url "future.dpgu.me")
  (def short-url (atom ""))

  (testing "POST request to /url creates new short url"
    (let [response (app (-> (mock/request :post  "/url")
                            (mock/content-type "application/json")
                            (mock/body (cheshire/generate-string {:link long-url}))))
          body (cheshire/parse-string (:body response))]

      (swap! short-url (fn [_] (get body "short-url")))
      (is (= 200 (:status response)))
      (is (= long-url (get body "long-url")))))

  (testing "GET request to fetch long url given short url"
    (let [url (str "/" @short-url)
          response (app (-> (mock/request :get url)))
          headers (:headers response)]

      (is (= 302 (:status response)))
      (is (= long-url (get headers "location"))))))

(deftest sad-paths
  (testing "GET on non-existent/expired short-url returns 404"
    (let [url "tikTok"
          response (app (-> (mock/request :get url)))
          headers (:headers response)]

      (is (= 404 (:status response))))))

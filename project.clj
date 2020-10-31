(defproject dpgurl "0.1.0-SNAPSHOT"
  :description "An expirale url shortener written in Clojure"
  :url "https://dpgurl.herokuapp.com"
  :min-lein-version "2.9.1"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.3.1"]
                 [com.taoensso/carmine "3.0.0"]
                 [clj-crypto "1.0.2"]
                 [ring/ring-defaults "0.3.2"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-mock "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler dpgurl.handler/app}
  :repl-options {:init-ns dpgurl.core})

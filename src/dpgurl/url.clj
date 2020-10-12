(ns dpgurl.url
  (:import
   java.util.Base64)
  (:require [clj-crypto.core :as crypto :refer [get-data-bytes create-salt]]))
  
;; stolen from https://github.com/greglook/whidbey/blob/master/src/whidbey/types.clj
(defn bin-str
  "Renders a byte array as a base-64 encoded string."
  [^bytes bin]
  (-> (Base64/getUrlEncoder)
      (.withoutPadding)
      (.encodeToString bin)))

(defn make-short-url []
  (bin-str (get-data-bytes (create-salt))))

(ns dpgurl.db)

(def db (atom {}))

(defn get-long-url [short]
  (let [db-value (deref db)
        long-url (get db-value short)]
    long-url))

(defn insert-url-into-db [short long]
    (swap!
     db
     #(assoc %1 short long)))
  

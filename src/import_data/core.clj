(ns import-data.core
  (:require [clojure-csv.core :as csv]
            [monger.core :as mg]
            [monger.collection :as mc]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [cli]])
  (:import org.bson.types.ObjectId))

(def db
  (mg/get-db (mg/connect) "monger-test"))

(def parse-file
  (with-open [file (clojure.java.io/reader (clojure.java.io/resource "data.csv"))]
    (doseq [line (line-seq file)]
      (let [cat (rand-nth ["100" "200" "300"])
            subcat (get (into {} [["100" [101 102 103]] ["200" [201 202 203]] ["300" [301 302 303]]]) cat)]
        (mc/insert db "subscriptions" {:user_id line, :category_id (int (read-string cat)), :subcategory_id  (int (rand-nth subcat)), :metainfo (take 3 (shuffle ["meta1" "meta2" "meta3" "meta4" "meta5" "meta6" "meta7" "meta8" "meta9"])), :timestamp (new java.util.Date) })
        (println line)))))

(defn -main [& args]
  (parse-file))

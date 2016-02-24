(ns cl-scraper.core
  (:require [pl.danieljanus.tagsoup :as ts]
            [net.cgrand.enlive-html :as html]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.format :as f]))

(defn get-cl-listings []
  (-> "https://chicago.craigslist.org/search/sss?sort=date&query=mid%20century%20modern%20credenza"
    (ts/parse-xml)
    (html/select [:span.pl])))

(defn last-4-hours? [listing]
  (->> (html/select listing [:time])
       (map #((comp :datetime :attrs) %))
       (map #(f/parse (f/formatter "yyyy-MM-dd HH:mm") %))
       (map #(t/after? (t/minus (t/now) (t/hours 10)) %))))

(defn -main []
  (let [recent-listings (->> (get-cl-listings)
                             (take-while #(false? (first (last-4-hours? %)))))]
    (html/select recent-listings [:a])))

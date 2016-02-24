(ns cl-scraper.core
  (:require [pl.danieljanus.tagsoup :as ts]
            [net.cgrand.enlive-html :as html]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [hiccup.core :as h]
            [postal.core :as p]
            [clojure.data.json :refer [read-str]]))

(def email-config
  (-> (slurp  "/Users/mairetilow/Documents/email_config.json")
      (read-str :key-fn keyword)))

(defn send-email [[title href]]
  (when (seq title)
    (do (Thread/sleep 200)
        (p/send-message {:host "smtp.gmail.com"
                         :user (:user email-config)
                         :pass (:pass email-config)
                         :ssl :yes!!!11}
                        {:from (:from email-config)
                         :to (:to email-config)
                         :subject title
                         :body href}))))

(defn parse-href [html]
  ((comp :href :attrs) html))

(defn parse-title [html]
  ((comp first :content) html))

(defn parse-html [html]
  [(parse-title html) (str "https://chicago.craigslist.org" (parse-href html))])

(defn get-cl-listings [keyword]
  (-> (str "https://chicago.craigslist.org/search/sss?sort=date&query=" keyword)
      (clojure.string/replace #" " "%20")
      (ts/parse-xml)
      (html/select [:span.pl])))

(defn last-4-hours? [n html]
  (->> (html/select html [:time])
       (map #((comp :datetime :attrs) %))
       (map #(f/parse (f/formatter "yyyy-MM-dd HH:mm") %))
       (map #(t/after? (t/minus (t/now) (t/hours 6) (t/minutes n)) %))))

(defn cl-scrape [n keyword]
  (do (Thread/sleep 200)
      (let [recent-listings (->> (get-cl-listings keyword)
                                 (take-while #(false? (first (last-4-hours? n %)))))]
        (->>(html/select recent-listings [:a])
            (map parse-html)))))

(defn cl-scrape! [n & phrases]
  (->> (map #(cl-scrape n %) phrases)
       (apply concat)
       distinct
       (map send-email)))

(defproject cl-scraper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [com.draines/postal "1.11.3"]
                 [enlive "1.1.6"]
                 [clj-time "0.11.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.json "0.2.6"]])

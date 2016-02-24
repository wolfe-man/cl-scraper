(ns cl-scraper.email
  (:require [postal.core :as p]))

(defn send-email []
  (p/send-message {:host "smtp.gmail.com"
                   :user "jsmith"
                   :pass "sekrat!!1"
                   :ssl :yes!!!11}
                  {:from "me@draines.com"
                   :to "foo@example.com"
                   :subject "Hi!"
                   :body "Test."}))

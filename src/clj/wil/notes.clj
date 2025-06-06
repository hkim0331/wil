(ns wil.notes
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [wil.db.core :as db]
   [ring.util.http-response :as response]))

(defn create-note!
  [{params :params}]
  (log/debug "create-note" params)
  (try
    (db/create-note! params)
    (response/ok {:ok "created"})
    (catch Exception e (throw (.getMessage e)))))

(defn notes-login
  [{{:keys [login]} :path-params}]
  (log/debug "notes-login" login)
  (response/ok (db/notes-login {:login login})))

(defn date-count [_]
  (log/debug "date-count")
  (response/ok (db/date-count)))

;; NO GOOD.
(defn notes-all
  [_params]
  (log/debug "notes-all")
  (response/ok (db/notes-all)))

(defn get-note
  [{{:keys [id]} :params}]
  (log/debug "get-note")
  (response/ok (db/get-note {:id id})))

(defn date-notes-randomly
  "retrieve random n `date` note"
  [{{:keys [date n]} :path-params}]
  (let [ret (db/date-notes-randomly
             {:date date :n (parse-long n)})]
    (log/debug "date-notes-randomly" ret)
    (response/ok ret)))

;; FIXME: admin only
;; (defn list-notes
;;   "can only see hkimura user"
;;   [{{:keys [date]} :path-params :as req}]
;;   (log/info "list-notes" (get-in req [:session :identity]))
;;   (if (= "hkimura" (get-in req [:session :identity]))
;;     {:status 200
;;      :headers {"content-type" "text/html"}
;;      :body (str/join (for [{:keys [login note]} (db/list-notes {:date date})]
;;              (str "<p>" login "<br>" note "</p>")))}
;;     {:status 404}))

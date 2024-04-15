(ns wil.goods
  (:require
   [clojure.tools.logging :as log]
   [wil.db.core :as db]
   [ring.util.http-response :as response]))

(comment
  (defn create-good-bad!
    [{{:keys [from to condition]} :params}]
  ;;(let [kind (if (= condition "good") 1 -1)]
    (try
      (let [kind (case condition
                   "good" 1
                   "so-so" 0
                   "bad" -1
                   (throw (Exception. (str "unknown condition: " condition))))]
        (db/create-good-bad!
         {:from_ from :to_ to :kind kind})
        (response/ok "inserted"))
      (catch Exception e (throw (Exception. (.getMessage e))))))
  :rcf)

(defn create-good-bad!
  [{{:keys [from to condition]} :params}]
  (let [hit (db/find-good-bad {:from_ from :to_ to})]
    (if hit
      (response/ok "すでに。")
      (let [kind (case condition
                   "good" 1
                   "so-so" 0
                   "bad" -1
                   (throw (Exception. (str "unknown condition:" condition))))]
        (try
          (db/create-good-bad!
           {:from_ from :to_ to :kind kind})
          (response/ok (str "received" condition))
          (catch Exception e (throw (Exception. (.getMessage e)))))))))

(defn good-bad
  [{{:keys [id]} :params}]
  (log/info "good-bad" id)
  (let [ret (db/good-bad {:id (Integer/parseInt id)})]
    (response/ok ret)))

(defn good-bad-sent
  [{{:keys [login]} :params}]
  (log/info "good-bad-sent" login)
  (let [ret (db/good-bad-sent {:login login})]
    (response/ok ret)))

(defn goods-bads
  [{{:keys [date]} :path-params}]
  (log/info "goods-bads" date)
  (let [goods (:count (db/goods-bads {:date date :kind 1}))
        so-so (:count (db/goods-bads {:date date :kind 0}))
        bads  (:count (db/goods-bads {:date date :kind -1}))]
    (response/ok {:date date :goods goods :so-so so-so :bads bads})))

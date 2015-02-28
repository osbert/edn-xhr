(ns edn-xhr.core
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.reader :as reader]
            [goog.events :as events]
            [dommy.core :refer-macros [sel sel1]]
            [cljs.core.async :refer [chan <! >!]])
  (:import [goog.net XhrIo]
           goog.net.EventType
           [goog.events EventType]))

(def ^:private meths
  {:get "GET"
   :put "PUT"
   :post "POST"
   :delete "DELETE"})

(def *anti-forgery-field* "#__anti-forgery-token")

(defn anti-forgery-token []
  (some-> *anti-forgery-field*
          sel1
          dommy.core/value))

(def default-headers {"Content-Type" "application/edn"
                      "Accept" "application/edn"})

(defn headers-with-csrf
  ([existing-headers]
   (headers-with-csrf (anti-forgery-token)))
  ([existing-headers csrf-token]
   (merge existing-headers
          (if csrf-token
            {"X-CSRF-Token" csrf-token}
            {}))))

(defn headers [method]
  (clj->js
   (if (some #{:put :post :delete} [method])
     (headers-with-csrf default-headers)
     default-headers)))

(defn edn-xhr [{:keys [method url data on-complete on-error on-progress]}]
  (let [xhr (XhrIo.)]
    (events/listen xhr goog.net.EventType.SUCCESS
                   (fn [e]
                     (on-complete (reader/read-string (.getResponseText xhr)))))
    (events/listen xhr goog.net.EventType.ERROR
                   (fn [e]
                     (on-error {:error (.getResponseText xhr)})))
    (let [m (meths method)]
      (. xhr
         (send url m (when data (pr-str data))
               (headers m))))
    xhr))

(ns com.tdibar.app
  (:require [com.biffweb :as biff :refer [q]]
            [com.tdibar.middleware :as mid]
            [com.tdibar.ui :as ui]
            [com.tdibar.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))

(def about-page
  (ui/page
   {:base/title (str "About " settings/app-name)}
   [:p "This app was made with "
    [:a.link {:href "https://biffweb.com"} "Biff"] "."]))

(defn echo [{:keys [params]}]
  {:status 200
   :headers {"content-type" "application/json"}
   :body params})

(def plugin
  {:static {"/about/" about-page}
   :api-routes [["/api/echo" {:post echo}]]})

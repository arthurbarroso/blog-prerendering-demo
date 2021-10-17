(ns re-frame.core)

(defn reg-event-db [& _args])
(defn reg-event-fx [& _args])
(defn reg-sub [& _args])
(defn subscribe [& _args] (atom false))
(defn dispatch [& _args])
(defn dispatch-sync [& _args])

(ns re-frame-cljs-http.http-fx)

(ns cljs-http)

(ns render
  (:require [app.render-server :refer [main-to-html]]))

(defn render-server []
  (main-to-html {:name "app" :users []}))

(println (render-server))

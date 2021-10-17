(ns app.render-client
  (:require [app.component :refer [counting-component]]
            [app.events :as events]
            [re-frame.core :as re-frame]
            [reagent.dom :as rdom]))

(defn ^:dev/after-load main-mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "root")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [counting-component] root-el)))

(defn ^:export main-render-browser []
  (re-frame/dispatch [::events/init-db])
  (main-mount-root))

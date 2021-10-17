(ns app.render-server
  (:require [app.component :refer [counting-component]]
            [app.events :as events]
            [clojure.string :as string]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [reagent.dom.server :as dom-server]
            ["react-dom" :as react-dom]
            ["fs" :as fs]))

(defn ^:export main-hydrate []
  (re-frame/dispatch-sync [::events/init-db])
  (let [cb (.getElementById js/document "root")]
    (react-dom/hydrate (r/as-element [counting-component])
                       cb)))

(defn main-to-html [initial-data]
  (re-frame/dispatch-sync [::events/init-db])
  (let [html-base "
<html>
  <head>
    <meta charset=\"utf-8\">
    <script src=\"/assets/js/shared.js\"></script>
    <script src=\"/assets/js/counting.js\"></script>
  </head>
  <body>
    <div id=\"root\">${{html-string}}</div>
  </body>
  <script>app.render_server.main_hydrate();</script>
</html>"
        pre-rendered-view (dom-server/render-to-string [counting-component initial-data])
        final (clojure.string/replace html-base "${{html-string}}" pre-rendered-view)]
    (fs/writeFileSync "public/counting-view.html" final)))

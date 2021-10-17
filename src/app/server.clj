(ns app.server
  (:require [app.render :as renderer]
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.middleware.exception :as exception]
            [ring.adapter.jetty :as jetty]))

(def router-options {:exception pretty/exception
                     :middleware [exception/exception-middleware]})

(defn server []
  (ring/ring-handler
    (ring/router
      [""
       ["/assets/*" (ring/create-resource-handler {:root "."})]
       ["/" {:get (fn [_]
                    {:body
                       (-> (renderer/build-page
                             (renderer/context-build)
                             "public/assets/clean.js"
                             "app.component.countinghtml"
                             {})
                           (renderer/template-html-2))})}]])
    router-options))

(defn run-server []
  (jetty/run-jetty (server) {:port 4000 :join? false}))

(comment
  (run-server))

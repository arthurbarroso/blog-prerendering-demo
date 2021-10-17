(ns util.clean-static
  (:require [app.render :as renderer]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn static-hook
  {:shadow.build/stage :flush}
  [build-state & args]
  (let [original (slurp (io/file "public/assets/graal.js"))
        start (- (string/index-of original "function graaljs_async_not_supported()") 0)
        to-replace (subs original start (+ start 664))]
    (spit "public/assets/clean.js"
          (-> original
              (string/replace to-replace " "))))
  (let [html-to-output (-> (renderer/build-page
                             (renderer/context-build)
                             "public/assets/clean.js"
                             "app.component.countinghtml"
                             {})
                           (renderer/template-html-2))]
    (spit "public/prerendered.html" html-to-output))
  build-state)

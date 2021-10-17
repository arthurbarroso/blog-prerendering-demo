(ns util.clean
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn hook
  {:shadow.build/stage :flush}
  [build-state & args]
  (let [original (slurp (io/file "public/assets/graal.js"))
        start (- (string/index-of original "function graaljs_async_not_supported()") 0)
        to-replace (subs original start (+ start 664))]
    (spit "public/assets/clean.js"
          (-> original
              (string/replace to-replace " "))))
  build-state)

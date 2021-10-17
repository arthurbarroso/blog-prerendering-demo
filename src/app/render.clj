(ns app.render
  (:require [clojure.java.io :as io])
  (:import (org.graalvm.polyglot Context Source Engine)
           (org.graalvm.polyglot.proxy ProxyArray ProxyObject)))

(defn serialize-arg [arg]
  (cond
    (keyword? arg)
      (name arg)

    (symbol? arg)
      (name arg)

    (map? arg)
      (ProxyObject/fromMap (into {} (map (fn [[k v]]
                                           [(serialize-arg k) (serialize-arg v)])
                                         arg)))

    (coll? arg)
      (ProxyArray/fromArray (into-array Object (map serialize-arg arg)))

    :else
      arg))

(defn execute-fn [context fn & args]
  (let [fn-ref (.eval context "js" fn)
        argsv (into-array Object (map serialize-arg args))]
    (assert (.canExecute fn-ref) (str "cannot execute " fn))
    (.execute fn-ref argsv)))

(defn template-html-2 [react-data]
  (str "<html>
        <head>
       <meta charset=\"utf-8\">
         <script src=\"/assets/clean.js\">
         </script>
        </head>
        <body>
        <div id=\"root\">"
       react-data
       "</div>
       </body>
       <script>
        app.component.countinghydrate();
       </script>
       </html>"))

(defn context-build []
  (let [engine (Engine/create)]
    (doto (Context/newBuilder (into-array String ["js"]))
      (.engine engine)
      (.allowExperimentalOptions true)
      (.option "js.experimental-foreign-object-prototype" "true")
      (.option "js.timer-resolution" "1")
      (.option "js.java-package-globals" "false")
      (.out System/out)
      (.err System/err)
      (.allowAllAccess true)
      (.allowNativeAccess true))))

(def build-page
  (memoize
    (fn [ctx js-file fun arg]
      (let [context (.build ctx)
            app-s (-> js-file
                      (io/file)
                      (#(.build (Source/newBuilder "js" %))))]
        (.eval context app-s)
        (.asString (execute-fn context fun arg))))))


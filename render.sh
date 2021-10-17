classpath="$(clojure -A:nbb -Spath -Sdeps '{}')"
nbb --classpath "$classpath" script.cljs

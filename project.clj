(defproject com.iterinc/edn-xhr "0.1.0-SNAPSHOT"
  :description "EDN XHR utility with CSRF anti-forgery-field support"
  :url "https://github.com/osbert/edn-xhr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371" :scope "provided"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [prismatic/dommy "1.0.0"]]
  :plugins [[lein-cljsbuild "1.0.4"]]
  :cljsbuild {:builds {:app {:source-paths ["src/cljs" "target/generated/cljs"]
                             :compiler {:output-to     "resources/public/js/com.iterinc.edn_xhr.js"
                                        :output-dir    "resources/public/js/out"
                                        :asset-path   "js/out"
                                        :optimizations :none
                                        :pretty-print  true}
                             :jar true}}}
  :clean-targets ^{:protect false} ["resources/public/js"]
  :hooks [leiningen.cljsbuild]
  )

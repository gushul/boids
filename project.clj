(defproject boids "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [generateme/fastmath "1.3.0-SNAPSHOT"]
                 [clojure2d "1.2.0-SNAPSHOT"]]
  :main boids.core/main
  :repl-options { :init-ns boids.core }
  )

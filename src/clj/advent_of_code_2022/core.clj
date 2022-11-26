(ns advent-of-code-2022.core
  (:require
   [advent-of-code-2022.handler :as handler]
   [luminus.http-server :as http]
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.tools.logging :as log]
   [mount.core :as mount])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (log/error {:what :uncaught-exception
                 :exception ex
                 :where (str "Uncaught exception on" (.getName thread))}))))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :parse-fn #(Integer/parseInt %)]])

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(mount/defstate ^{:on-reload :noop} http-server
  :start
  (http/start
   (-> {:port 3000}
       (update :io-threads #(or % (* 2 (.availableProcessors (Runtime/getRuntime)))))
       (assoc  :handler (handler/app))
       (select-keys [:handler :host :port :async?])))
  :stop
  (http/stop http-server))

(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))

(defn start-app [args]
  (doseq [component (-> args
                        (parse-opts cli-options)
                        mount/start-with-args
                        :started)]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))

(defn -main [& args]
  (start-app args))

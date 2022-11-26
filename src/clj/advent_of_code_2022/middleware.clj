(ns advent-of-code-2022.middleware
  (:require
   [clojure.tools.logging :as log]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.middleware.flash :refer [wrap-flash]]
   [ring.middleware.cors :refer [wrap-cors]]
   [ring.adapter.undertow.middleware.session :refer [wrap-session]]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))


(defn error-response
  [details]
  {:status  (:status (get details :status))
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body {:error "true"}})

(defn wrap-internal-error [handler]
  (let [error-result (fn [^Throwable t]
                       (log/error t (.getMessage t))
                       (error-response {:status 500
                                        :title "Something very bad has happened!"
                                        :message "We've dispatched a team of highly trained gnomes to take care of the problem."}))]
    (fn wrap-internal-error-fn
      ([req respond _]
       (handler req respond #(respond (error-result %))))
      ([req]
       (try
         (handler req)
         (catch Throwable t
           (error-result t)))))))

(defn wrap-csrf [handler]
  (wrap-anti-forgery
   handler
   {:error-response
    (error-response
     {:status 403
      :title "Invalid anti-forgery token"})}))


(defn wrap-base [handler]
  (-> handler
      wrap-flash
      (wrap-cors :access-control-allow-methods [:get] :access-control-allow-origin #"http://localhost:1234")
      (wrap-session {:cookie-attrs {:http-only true}})
      (wrap-defaults
       (-> site-defaults
           (assoc-in [:security :anti-forgery] false)
           (dissoc :session)))
      wrap-internal-error))



(ns advent-of-code-2022.handler
  (:require
   [advent-of-code-2022.middleware :as middleware]
   [advent-of-code-2022.solutions.day-01 :as day-01]
   [advent-of-code-2022.solutions.day-02 :as day-02]
   [advent-of-code-2022.solutions.day-03 :as day-03]
   [advent-of-code-2022.solutions.day-04 :as day-04]
   [advent-of-code-2022.solutions.day-05 :as day-05]
   [advent-of-code-2022.solutions.day-06 :as day-06]
   [advent-of-code-2022.solutions.day-07 :as day-07]
   [advent-of-code-2022.solutions.day-08 :as day-08]
   [advent-of-code-2022.solutions.day-09 :as day-09]
   [advent-of-code-2022.solutions.day-10 :as day-10]
   [advent-of-code-2022.solutions.day-11 :as day-11]
   [advent-of-code-2022.solutions.day-12 :as day-12]
   [advent-of-code-2022.solutions.day-13 :as day-13]
   [advent-of-code-2022.solutions.day-14 :as day-14]
   [advent-of-code-2022.solutions.day-15 :as day-15]
   [advent-of-code-2022.solutions.day-16 :as day-16]
   [advent-of-code-2022.solutions.day-17 :as day-17]
   [advent-of-code-2022.solutions.day-18 :as day-18]
   [advent-of-code-2022.solutions.day-19 :as day-19]
   [advent-of-code-2022.solutions.day-20 :as day-20]
   [advent-of-code-2022.solutions.day-21 :as day-21]
   [advent-of-code-2022.solutions.day-22 :as day-22]
   [advent-of-code-2022.solutions.day-23 :as day-23]
   [advent-of-code-2022.solutions.day-24 :as day-24]
   [advent-of-code-2022.solutions.day-25 :as day-25]
   [muuntaja.core :as m]
   [mount.core :as mount]
   [reitit.ring :as ring]
   [ring.util.response :as response]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.webjars :refer [wrap-webjars]]))

(def solutions
  {"1" day-01/output
   "2" day-02/output
   "3" day-03/output
   "4" day-04/output
   "5" day-05/output
   "6" day-06/output
   "7" day-07/output
   "8" day-08/output
   "9" day-09/output
   "10" day-10/output
   "11" day-11/output
   "12" day-12/output
   "13" day-13/output
   "14" day-14/output
   "15" day-15/output
   "16" day-16/output
   "17" day-17/output
   "18" day-18/output
   "19" day-19/output
   "20" day-20/output
   "21" day-21/output
   "22" day-22/output
   "23" day-23/output
   "24" day-24/output
   "25" day-25/output})

(defn get-solution
  [request]
  (let [day (-> request (get :path-params) (get :day))]
    (->
     {:body (m/encode "application/json" (get solutions day))}
     (response/update-header "Content-Type" (constantly "application/json")))))

(defn get-solution-page
  [_request]
  (-> {:body
       (slurp "resources/public/solution/index.html")}
      (response/update-header "Content-Type" (constantly "text/html"))))

(def routes
  [""
   {:middleware [middleware/wrap-csrf]}
   ["/api/solution/:day"
    {:get get-solution}]
   ["/solution"
    {:get get-solution-page}]])

(defn- async-aware-default-handler
  ([_] nil)
  ([_ respond _] (respond nil)))


(mount/defstate app-routes
  :start
  (ring/ring-handler
   (ring/router
    [routes])
   (ring/routes
    (ring/create-resource-handler
     {:path "/"})
    (wrap-content-type
     (wrap-webjars async-aware-default-handler)))))

(defn app []
  (middleware/wrap-base #'app-routes))

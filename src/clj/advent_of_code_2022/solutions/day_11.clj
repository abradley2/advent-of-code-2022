(ns advent-of-code-2022.solutions.day-11
  (:require
   [clojure.spec.alpha :as s]))

(defn parse-test [raw-test] raw-test)

(defn run-operation [old op]
  ((:fn op)
   (if (= (:arg-1 op) :old) old (:arg-1 op))
   (if (= (:arg-2 op) :old) old (:arg-2 op))))

(defn parse-operation [raw-operation]
  (as-> (re-matches #"\s*new\s*=\s*(old|\d+)\s*([\+\-\*]{1})\s*(old|\d+).*" raw-operation) matches
    {:arg-1 (->> (get matches 1) (s/assert some?) (#(if (= % "old") :old (read-string %))))
     :fn (read-string (s/assert some? (get matches 2)))
     :arg-2 (->> (get matches 3) (s/assert some?) (#(if (= % "old") :old (read-string %))))}))

(defn parse-trait [raw-trait]
  (as-> (.split raw-trait ":") [name body]
    (cond
      (= name "Starting items") {:items (read-string (str "(" body ")"))}
      (= name "Operation") {:operation (parse-operation body)}
      (= name "Test") {:test (parse-test body)})))

(defn parse-monkey [raw-monkey]
  (let [lines (.split raw-monkey (str #"\n\s{2}(?=\w)"))
        name (first lines)
        traits (->> (map parse-trait (rest lines)) (reduce merge {}))]

    {:name name :traits traits}))

(defn parse-input [input]
  (->> (.split (s/assert string? input) (str #"\n\n"))
       (map parse-monkey)))


(defn part-1
  [_input]
  "Not implemented")

(defn part-2
  [_input]
  "Not implemented")

(def output
  {"Part One" (-> (slurp "resources/input/day_11.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_11.txt") part-2)})
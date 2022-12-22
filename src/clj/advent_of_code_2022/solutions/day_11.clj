(ns advent-of-code-2022.solutions.day-11
  (:require
   [clojure.spec.alpha :as s]))

(defn parse-trait [input] input)

(defmacro make-op [_new _eq argA op argB] `((fn [old] (~op ~argA ~argB))))



(defn parse-monkey [input]
  (let [lines (.split input (str #"\n\s{2}(?=\w)"))
        name (first lines)
        traits (map parse-trait (rest lines))]

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
(ns advent-of-code-2022.solutions.day-01
  (:require [clojure.string :as str]))

(defn parse-input [input]
  (->> (str/split input #"\n\n")
       (map (comp #(map read-string %) #(str/split % #"\n")))))

(defn part-1 [input]
  (->> (parse-input input)
       (map #(reduce + 0 %))
       (#(apply max %))))

(defn part-2 [input]
  (->> (parse-input input)
       (map #(reduce + 0 %))
       sort
       reverse
       (take 3)
       (reduce + 0)))

(def output
  {"Part One" (-> (slurp "resources/input/day_01.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_01.txt") part-2)})
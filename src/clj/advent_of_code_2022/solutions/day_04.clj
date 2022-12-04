(ns advent-of-code-2022.solutions.day-04
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [clojure.set :as set]
   advent-of-code-2022.solutions.spec))

(defn to-range [input]
  (let [[from to] (map read-string (str/split input #"-"))]
    (->> (range from (+ 1 to))
         (s/assert :spec/non-empty))))

(defn parse-input [input]
  (->> (str/split input #"\n")
       (map #(str/split % #","))
       (map #(map to-range %))))

(defn check-superset [set-a set-b]
  (cond
    (set/superset? set-a set-b) true
    (set/subset? set-a set-b) true
    :else false))

(defn check-overlap [set-a set-b] (-> (set/intersection set-a set-b) count (not= 0)))

(defn part-1
  [input]
  (->> (parse-input input)
       (map #(map set %))
       (s/assert (s/coll-of (s/coll-of set?)))
       (filter #(apply check-superset %))
       count))

(defn part-2
  [input]
  (->> (parse-input input)
       (map #(map set %))
       (s/assert (s/coll-of (s/coll-of set?)))
       (filter #(apply check-overlap %))
       count))

(def output
  {"Part One" (-> (slurp "resources/input/day_04.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_04.txt") part-2)})
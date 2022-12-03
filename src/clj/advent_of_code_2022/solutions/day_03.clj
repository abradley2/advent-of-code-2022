(ns advent-of-code-2022.solutions.day-03
  (:require
   advent-of-code-2022.solutions.spec
   [clojure.string :as str]
   [clojure.spec.alpha :as s]
   [clojure.set :as set]))

(def get-priority #(str/index-of "$abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" %))

(s/fdef partition-rucksack
  :args (s/cat :items (s/and (s/coll-of string?) (comp even? count))))

(defn partition-rucksack [items]
  (partition (/ (count items) 2) items))

(defn parse-input [input]
  (->> (str/split input #"\n")
       (map #(str/split % #""))
       (map #(s/assert (s/coll-of string?) %))
       (map partition-rucksack)))

(s/fdef get-intersect :args (s/cat :set-a set? :set-b set?))

(defn get-intersect [set-a set-b]
  (->> (set/intersection set-a set-b)
       (s/assert :spec/count-1)
       first))

(defn part-1
  [input]
  (->> (parse-input input)
       (map #(map set %))
       (map #(apply get-intersect %))
       (map get-priority)
       (reduce + 0)))

(defn part-2
  [_input]
  "Not Implemented")

(def output
  {"Part One" (-> (slurp "resources/input/day_03.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_03.txt") part-2)})
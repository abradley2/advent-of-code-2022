(ns advent-of-code-2022.solutions.day-06
  (:require
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.spec.alpha :as s]))

(defn parse-input [input]
  (->> (apply list input)
       (s/assert seq?)
       (s/assert (s/coll-of char?))))

(defn all-unique [coll] (and
                         (= (count (set coll)) (count coll))
                         (every? char? coll)))

(defn run-solution [input initial-stack]
  (let [all-chars (parse-input input)]
    (loop
     [chars all-chars
      idx 0
      stack initial-stack]
      (if (all-unique stack)
        idx
        (recur
         (rest chars)
         (inc idx)
         (as-> stack next-stack
           (take (dec (count stack)) next-stack)
           (cons (first chars) next-stack)))))))

(defn part-1
  [input]
  (run-solution input '(nil nil nil nil)))

(defn part-2
  [input]
  (run-solution input '(nil nil nil nil nil nil nil nil nil nil nil nil nil nil)))

(def output
  {"Part One" (-> (slurp "resources/input/day_06.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_06.txt") part-2)})
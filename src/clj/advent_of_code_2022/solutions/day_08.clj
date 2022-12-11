(ns advent-of-code-2022.solutions.day-08
  (:require
   [clojure.string :as str]
   [clojure.set :as set]
   [clojure.spec.alpha :as s]))

(defn parse-input [input]
  (->> (str/split input #"\n")
       (map #(->> (str/split % #"") (map read-string)))))

(defn create-rows [parsed-input]
  (map-indexed (fn [col-idx row]
                 (map-indexed (fn [row-idx col]
                                {:x row-idx :y col-idx :height col}) row)) parsed-input))

(defn create-columns
  [rows]
  (let [max-columns (-> (first rows) count)
        rows-vec (vec (map vec rows))]
    (loop
     [row-idx 0
      columns '()]
      (if (not= row-idx max-columns)
        (recur (inc row-idx) (cons (map #(get % row-idx) rows-vec) columns))
        columns))))

(defn -count-trees [tree-coll]
  (loop
   [cur-tree (first tree-coll)
    next-trees (rest tree-coll)
    prev-height -1
    visible-trees '()]
    (if (nil? cur-tree) visible-trees
        (recur
         (first next-trees)
         (rest next-trees)
         (max prev-height (:height cur-tree))
         (if (> (:height cur-tree) prev-height)
           (cons cur-tree visible-trees)
           visible-trees)))))

(defn count-trees [tree-coll]
  (set (concat (-count-trees tree-coll) (-count-trees (reverse tree-coll)))))

(defn part-1
  [input]
  (let
   [tree-rows (-> (parse-input input) create-rows)
    tree-columns (create-columns tree-rows)
    trees (concat tree-rows tree-columns)]
    (count (-> (reduce
                #(set/union %1 (count-trees %2))
                #{}
                trees)))))

(defn part-2
  [_input]
  "Not implemented")

(def output
  {"Part One" (-> (slurp "resources/input/day_08.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_08.txt") part-2)})
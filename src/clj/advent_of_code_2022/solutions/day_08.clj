(ns advent-of-code-2022.solutions.day-08
  (:require
   [clojure.string :as str]
   [clojure.set :as set]))

(defn parse-input [input]
  (->> (str/split input #"\n")
       (map #(->> (str/split % #"") (map read-string)))))

(defn create-rows [parsed-input]
  (map-indexed (fn [col-idx row]
                 (map-indexed (fn [row-idx col]
                                {:x row-idx :y col-idx :h col}) row)) parsed-input))

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
         (max prev-height (:h cur-tree))
         (if (> (:h cur-tree) prev-height)
           (cons cur-tree visible-trees)
           visible-trees)))))

(defn count-sightline [pov-tree tree-coll]
  (loop
   [cur-tree (first tree-coll)
    next-trees (rest tree-coll)
    prev-height -1
    visible-trees '()]
    (if (nil? cur-tree) visible-trees
        (let [next-visible-trees
              (if (or (> (:h cur-tree) prev-height)
                      (and (= (:h cur-tree) prev-height) (< prev-height (:h pov-tree))))
                (cons cur-tree visible-trees)
                visible-trees)]
          (if (>= (:h cur-tree) (:h pov-tree))
            next-visible-trees
            (recur
             (first next-trees)
             (rest next-trees)
             (max prev-height (:h cur-tree))
             next-visible-trees))))))

(defn count-trees [tree-coll]
  (set (concat (-count-trees tree-coll) (-count-trees (reverse tree-coll)))))

(defn get-column [tree columns]
  (let [column (first columns)]
    (if (= (:x tree) (-> (first column) :x)) column (recur tree (rest columns)))))

(defn get-row [tree rows]
  (let [row (first rows)]
    (if (= (:y tree) (-> (first row) :y)) row (recur tree (rest rows)))))

(defn grade-tree [tree tree-rows tree-columns]
  (let [ends (fn [r] [(reverse (first r)) (last r)])
        [left right] (ends  (partition-by #(= % tree) (get-row tree tree-rows)))
        [up down] (ends  (partition-by #(= % tree) (get-column tree tree-columns)))]
    (->> {:up (count (count-sightline tree up))
          :down (count (count-sightline tree down))
          :left (count (count-sightline tree left))
          :right (count (count-sightline tree right))}
         vals
         (apply *))))

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
  [input]
  (let [tree-rows (-> (parse-input input) create-rows)
        tree-columns (create-columns tree-rows)
        trees (flatten tree-columns)]
    (->> (map #(grade-tree % tree-rows tree-columns) trees)
         (apply max))))

; 1470 == too low

(def output
  {"Part One" (-> (slurp "resources/input/day_08.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_08.txt") part-2)})

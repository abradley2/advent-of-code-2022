(ns advent-of-code-2022.solutions.day-05
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]
            [advent-of-code-2022.solutions.util :as util]
            advent-of-code-2022.solutions.spec))

(defn parse-diagram [input]
  (let [lines (str/split input #"\n")
        indices (-> (last lines) (util/re-index-map #"\d"))
        crates (->> (drop-last lines) (map #(util/re-index-map % #"\[(\w)\]")))]
    {:indices indices
     :crates (->
              (reduce #(merge-with list %1 %2) {} crates)
              (update-keys #(read-string (get indices %)))
              (update-vals (comp flatten list)))}))

(defn parse-instruction
  [instruction]
  (let [[_ quant from to] (re-find #"move\s+(\d)\s+from\s+(\d)\s+to\s+(\d)" instruction)]
    {:quant (read-string quant) :from (read-string from) :to (read-string to)}))

(defn parse-input [input]
  (let [[diagram-input instructions-input] (str/split input #"\n\n")
        diagram (parse-diagram diagram-input)
        instructions (map parse-instruction (str/split instructions-input #"\n"))]
    [diagram instructions]))

(defn run-instruction [crates instruction]
  (-> crates
      (update
       (:from instruction)
       #(drop (:quant instruction) %))
      (update
       (:to instruction)
       #(list (->> (get crates (:from instruction)) (take (:quant instruction))) %))
      (update-vals flatten)))

(defn part-1
  [input]
  (let [[diagram instructions] (parse-input input)]
    (reduce
     run-instruction
     (:crates diagram)
     instructions)))

(defn part-2
  [input]
  input)

(def output
  {"Part One" (-> (slurp "resources/input/day_05.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_05.txt") part-2)})
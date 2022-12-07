(ns advent-of-code-2022.solutions.util
  (:require
   [clojure.string :as str]))

(defn -step-re-index
  "Utility function for the below that executes a regex and returns the match
   in addition to the index of that first match. Allows for the match to be
   a capture group. If the match is nil, returns nil
   "
  [string re]
  (let
   [m (-> (re-find re string)
          (#(if (vector? %) (second %) %)))]
    (cond m
          (let [idx (str/index-of string m)]
            {:match m
             :next (->> (split-at (inc idx) string) second (str/join ""))}))))

(defn all-indices-of
  "Like str/index-of but gives *all the indices*"
  [string target]
  (loop
   [cur string
    indices '()
    offset 0]
    (let [index (str/index-of cur target)]
      (if (= index nil) indices
          (recur
           (apply str (second (split-at (inc index) cur)))
           (cons (+ offset index) indices)
           (+ offset index 1))))))

(defn re-index-map
  "Takes a string and a regex and returns a map of all matches from that regex
   as values, with the a list of the indices they were found at as keys
   "
  [string re]
  (into (sorted-map) (loop
                      [index-map {}
                       next string]
                       (let [result (-step-re-index next re)]
                         (case result
                           nil index-map
                           (recur
                            (reduce
                             (fn [-index-map idx]
                               (assoc -index-map idx (:match result)))
                             index-map
                             (all-indices-of string (:match result)))
                            (:next result)))))))

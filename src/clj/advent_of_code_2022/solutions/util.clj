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

(defn re-index-map
  "Takes a string and a regex and returns a map of all matches from that regex
   as values, with the columns they were found at as keys
   "
  [string re]
  (loop
   [index-map {}
    next string]
    (let [result (-step-re-index next re)]
      (case result
        nil index-map
        (recur
         (assoc index-map (str/index-of string (:match result)) (:match result))
         (:next result))))))

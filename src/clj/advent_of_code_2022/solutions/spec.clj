(ns advent-of-code-2022.solutions.spec
  (:require
   [clojure.spec.alpha :as s]))

(s/def :spec/count-1 #(= 1 (count %)))

(s/def :spec/partition-by-3 #(= 0 (mod (count %) 3)))
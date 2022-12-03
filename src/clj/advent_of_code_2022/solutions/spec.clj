(ns advent-of-code-2022.solutions.spec
  (:require
   [clojure.spec.alpha :as s]))

(s/def :spec/count-1 #(= 1 (count %)))

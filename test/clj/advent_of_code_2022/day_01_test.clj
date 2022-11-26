(ns advent-of-code-2022.day-01-test
  (:require
   [clojure.test :as t]
   [advent-of-code-2022.solutions.day-01 :as day-01]))

(t/deftest day-01-test
  (t/testing "Part 1 - Sample"
    (let [input "1234"]
      (t/is (= "FIX ME" (day-01/part-1 input)))))
  (t/testing "Part 1"
    (let [input (slurp "resources/input/day_01.txt")]
      (t/is (= "FIX ME" (day-01/part-1 input))))))

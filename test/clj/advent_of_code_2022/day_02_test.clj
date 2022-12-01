(ns advent-of-code-2022.day-02-test
  (:require
   [clojure.test :as t]
   [advent-of-code-2022.solutions.day-02 :as day-02]))

(t/deftest day-02-part-1
  (t/testing "Sample"
    (let [input "2x3x4"]
      (t/is (= 58 (day-02/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_02.txt")]
      (t/is (= 1586300 (day-02/part-1 input))))))
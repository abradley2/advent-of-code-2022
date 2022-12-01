(ns advent-of-code-2022.day-01-test
  (:require
   [clojure.test :as t]
   [advent-of-code-2022.solutions.day-01 :as day-01]))

(t/deftest day-01-part-1
  (t/testing "Sample 1"
    (let [input "(())"]
      (t/is (= 0 (day-01/part-1 input)))))
  (t/testing "Sample 2"
    (let [input "()()"]
      (t/is (= 0 (day-01/part-1 input)))))
  (t/testing "Sample 3"
    (let [input "))((((("]
      (t/is (= 3 (day-01/part-1 input)))))
  (t/testing "Sample 4"
    (let [input ")())())"]
      (t/is (= -3 (day-01/part-1 input)))))
  (t/testing "Actual"
    (let
     [input (slurp "resources/input/day_01.txt")]
      (t/is (= 280 (day-01/part-1 input))))))

(t/deftest day-01-part-2
  (t/testing "Sample 1"
    (let [input ")"]
      (t/is (= 1 (day-01/part-2 input)))))
  (t/testing "Sample 2"
    (let [input "()())"]
      (t/is (= 5 (day-01/part-2 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_01.txt")]
      (t/is (= 1797 (day-01/part-2 input))))))
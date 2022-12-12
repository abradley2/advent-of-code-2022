(ns advent-of-code-2022.day-08-test
  (:require [advent-of-code-2022.solutions.day-08 :as day-08]
            [clojure.test :as t]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]))

(s/check-asserts true)

(stest/instrument)

(def sample-data "30373
25512
65332
33549
35390")

(t/deftest day-08-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 21 (day-08/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_08.txt")]
      (t/is (= 1688 (day-08/part-1 input))))))

(t/deftest day-08-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 8 (day-08/part-2 input))))))
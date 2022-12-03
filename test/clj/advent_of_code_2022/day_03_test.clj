(ns advent-of-code-2022.day-03-test
  (:require [clojure.spec.test.alpha :as stest]
            [clojure.spec.alpha :as s]
            [advent-of-code-2022.solutions.day-03 :as day-03]
            [clojure.test :as t]))


(stest/instrument)

(s/check-asserts true)

(def sample-data "vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw")

(t/deftest day-03-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 157 (day-03/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_03.txt")]
      (t/is (= 7428 (day-03/part-1 input))))))

(t/deftest day-03-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 70 (day-03/part-2 input))))))
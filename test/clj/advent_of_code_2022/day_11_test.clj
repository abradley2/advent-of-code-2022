(ns advent-of-code-2022.day-11-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [advent-of-code-2022.solutions.day-11 :as day-11]))


(s/check-asserts true)

(stest/instrument)

(def sample-data "Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1")

(t/deftest day-11-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 10605 (day-11/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_11.txt")]
      (t/is (= 57348 (day-11/part-1 input))))))

(t/deftest day-11-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 2713310158 (day-11/part-2 input))))))
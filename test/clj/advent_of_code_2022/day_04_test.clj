(ns advent-of-code-2022.day-04-test
  (:require
   [advent-of-code-2022.solutions.day-04 :as day-04]
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]))

(stest/instrument)

(s/check-asserts true)

(def sample-data "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8")

(t/deftest day-04-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 2 (day-04/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_04.txt")]
      (t/is (= 651 (day-04/part-1 input))))))

(t/deftest day-04-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 4 (day-04/part-2 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_04.txt")]
      (t/is (= 956 (day-04/part-2 input))))))
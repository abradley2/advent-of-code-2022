(ns advent-of-code-2022.day-09-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [advent-of-code-2022.solutions.day-09 :as day-09]))

(s/check-asserts true)

(def sample-data "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2")

(t/deftest day-09-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 13 (day-09/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_09.txt")]
      (t/is (= 5619 (day-09/part-1 input))))))

(def second-sample-data "R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20")

(t/deftest day-09-part-2-test
  (t/testing "Sample"
    (let [input second-sample-data]
      (t/is (= 36 (day-09/part-2 input))))))
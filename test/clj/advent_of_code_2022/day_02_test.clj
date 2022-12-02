(ns advent-of-code-2022.day-02-test
  (:require
   [clojure.test :as t]
   [clojure.spec.test.alpha :as stest]
   [advent-of-code-2022.solutions.day-02 :as day-02]))

(def sample-data "A Y
B X
C Z")

(stest/instrument)

(t/deftest day-02-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 15 (day-02/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_02.txt")]
      (t/is (= 13565 (day-02/part-1 input))))))

(t/deftest day-02-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 12 (day-02/part-2 input))))
    (t/testing "Actual"
      (let [input (slurp "resources/input/day_02.txt")]
        (t/is (= 12424 (day-02/part-2 input)))))))
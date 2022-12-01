(ns advent-of-code-2022.day-01-test
  (:require
   [clojure.test :as t]
   [advent-of-code-2022.solutions.day-01 :as day-01]))

(def sample-input "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

(t/deftest day-01-part-01-test
  (t/testing "Sample"
    (let [input sample-input]
      (t/is (= 24000 (day-01/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_01.txt")]
      (t/is (= 66719 (day-01/part-1 input))))))

(t/deftest day-01-part-02-test
  (t/testing "Sample"
    (let [input sample-input]
      (t/is (= 45000 (day-01/part-2 input))))))

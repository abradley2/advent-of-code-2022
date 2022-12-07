(ns advent-of-code-2022.day-05-test
  (:require
   [advent-of-code-2022.solutions.day-05 :as day-05]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [clojure.test :as t]))

(s/check-asserts true)

(stest/instrument)

(def sample-data "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2")

(t/deftest day-05-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= "CMZ" (day-05/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_05.txt")]
      (t/is (= "BWNCQRMDB" (day-05/part-1 input))))))

(t/deftest day-05-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= "MCD" (day-05/part-2 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_05.txt")]
      (t/is (= "NHWZCBNBF" (day-05/part-2 input))))))
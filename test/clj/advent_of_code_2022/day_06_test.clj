(ns advent-of-code-2022.day-06-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [advent-of-code-2022.solutions.day-06 :as day-06]))

(s/check-asserts true)

(stest/instrument)

(t/deftest day-06-part-1-test
  (t/testing "Sample A"
    (let [input "mjqjpqmgbljsphdztnvjfqwrcgsmlb"]
      (t/is (= 7 (day-06/part-1 input)))))
  (t/testing "Sample B"
    (let [input "bvwbjplbgvbhsrlpgdmjqwftvncz"]
      (t/is (= 5 (day-06/part-1 input)))))
  (t/testing "Sample C"
    (let [input "nppdvjthqldpwncqszvftbrmjlhg"]
      (t/is (= 6 (day-06/part-1 input)))))
  (t/testing "Sample D"
    (let [input "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"]
      (t/is (= 11 (day-06/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_06.txt")]
      (t/is (= 1658 (day-06/part-1 input))))))

;; (t/deftest day-06-part-2-test
;;   (t/testing "Sample A"
;;     (let [input "mjqjpqmgbljsphdztnvjfqwrcgsmlb"]
;;       (t/is (= 19 (day-06/part-2 input)))))
;;   (t/testing "Sample B"
;;     (let [input "bvwbjplbgvbhsrlpgdmjqwftvncz"]
;;       (t/is (= 23 (day-06/part-2 input))))))
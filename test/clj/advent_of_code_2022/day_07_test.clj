(ns advent-of-code-2022.day-07-test
  (:require
   [clojure.spec.alpha :as s]
   [clojure.spec.test.alpha :as stest]
   [advent-of-code-2022.solutions.day-07 :as day-07]
   [clojure.test :as t]))

(s/check-asserts true)

(stest/instrument)

(def sample-data "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k")

(t/deftest day-07-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 95437 (day-07/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_07.txt")]
      (t/is (= 1648397 (day-07/part-1 input))))))

(t/deftest day-07-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 24933642 (day-07/part-2 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_07.txt")]
      (t/is (= 1815525 (day-07/part-2 input))))))
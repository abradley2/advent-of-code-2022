(ns advent-of-code-2022.day-10-test
  (:require
   [clojure.test :as t]
   [clojure.spec.alpha :as s]
   [advent-of-code-2022.solutions.day-10 :as day-10]))

(s/check-asserts true)

(def sample-data "addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop")

(t/deftest day-10-part-1-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= 13140 (day-10/part-1 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_10.txt")]
      (t/is (= 15020 (day-10/part-1 input))))))

(def part-2-sample "##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....")

(def part-2-actual "####.####.#..#..##..#....###...##..###..
#....#....#..#.#..#.#....#..#.#..#.#..#.
###..###..#..#.#....#....#..#.#..#.#..#.
#....#....#..#.#.##.#....###..####.###..
#....#....#..#.#..#.#....#....#..#.#....
####.#.....##...###.####.#....#..#.#....")

(t/deftest day-10-part-2-test
  (t/testing "Sample"
    (let [input sample-data]
      (t/is (= part-2-sample (day-10/part-2 input)))))
  (t/testing "Actual"
    (let [input (slurp "resources/input/day_10.txt")]
      (t/is (= part-2-actual (day-10/part-2 input))))))
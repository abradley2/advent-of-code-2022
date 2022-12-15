(ns advent-of-code-2022.solutions.day-10
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]))

(defn parse-line [input-line]
  (some
   identity
   [(some->
     (re-matches #"addx\s+(-?\d+)" input-line)
     (#(merge {:op :addx :cycles 2} {:val (read-string (get % 1))})))
    (some->
     (re-matches #"noop" input-line)
     ((constantly {:op nil :cycles 1})))]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map parse-line)))

(defn to-cmd [op]
  (case (:op op)
    :addx {:delay 2 :run #(+ % (:val op))}
    nil))

(defn run-cmd [cmd state]
  (if (= 0 (:delay cmd)) [nil (-> state ((:run cmd)))]
      [(update cmd :delay dec) state]))

(defn run-cmds [initial-cmds initial-state]
  (loop [x (first initial-cmds)
         xs (rest initial-cmds)
         cmds '()
         state initial-state]
    (if (nil? x)
      [cmds state]
      (let
       [[next-cmd next-state] (run-cmd x state)]
        (recur
         (first xs)
         (rest xs)
         (if (nil? next-cmd) cmds (cons next-cmd cmds))
         next-state)))))

(defn part-1
  [input]
  (let [initial-ops (parse-input input)]
    (loop
     [op (first initial-ops)
      next (rest initial-ops)
      cmds '()
      state 1
      cycle 0
      cycle-map {}]
      (if (and (nil? op) (empty? cmds))
        (into (sorted-map) cycle-map)
        (let [cmd (some-> op to-cmd)
              [next-cmds next-state] (if (nil? cmd)
                                       (run-cmds cmds state)
                                       (run-cmds (cons cmd cmds) state))]
          (recur
           (first next)
           (rest next)
           next-cmds
           next-state
           (inc cycle)
           (assoc cycle-map cycle state)))))))

(defn part-2
  [input]
  "Not implemented")

(def output
  {"Part One" (-> (slurp "resources/input/day_10.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_10.txt") part-2)})
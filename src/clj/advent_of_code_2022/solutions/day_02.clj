(ns advent-of-code-2022.solutions.day-02
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]))

(def match?
  (s/and
   #(->> (get % :p-1) (contains? #{:rock :paper :scissors}))
   #(->> (get % :p-2) (contains? #{:rock :paper :scissors}))))

(def strategy?
  (s/and
   #(->> (get % :p-1) (contains? #{:win :draw :lose}))
   #(->> (get % :p-2) (contains? #{:rock :paper :scissors}))))

(def play-map {":rock:win" :paper
               ":rock:lose" :scissors
               ":rock:draw" :rock
               ":paper:win" :scissors
               ":paper:lose" :rock
               ":paper:draw" :paper
               ":scissors:win" :rock
               ":scissors:lose" :paper
               ":scissors:draw" :scissors})

(defn parse-input [input]
  (let [parsed-input {"A" :rock
                      "B" :paper
                      "C" :scissors
                      "X" :rock
                      "Y" :paper
                      "Z" :scissors}]
    (->> (str/split input #"\n")
         (map (comp
               #(->
                 (assoc {} :p-2 (->> (get % 0) (get parsed-input)))
                 (assoc :p-1 (->> (get % 1) (get parsed-input))))
               #(str/split % #"\s"))))))

(defn to-strategy
  [match] (let [corrections {:rock :lose
                             :paper :draw
                             :scissors :win}]
            (assoc match :p-1 (get corrections (get match :p-1)))))

(s/fdef to-strategy :args (s/cat :match match?))

(defn to-match [strategy]
  (assoc strategy :p-1 (get play-map (str (get strategy :p-2) (get strategy :p-1)))))

(s/fdef to-match :args (s/cat :strategy strategy?))

(defn match-to-score [match]
  (let [p-1 (get match :p-1)
        p-2 (get match :p-2)
        scores {:rock 1
                :paper 2
                :scissors 3
                :win 6
                :draw 3
                :loss 0}
        outcome {":rock:rock" :draw
                 ":rock:paper" :loss
                 ":rock:scissors" :win
                 ":paper:rock" :win
                 ":paper:paper" :draw
                 ":paper:scissors" :loss
                 ":scissors:rock" :loss
                 ":scissors:paper" :win
                 ":scissors:scissors" :draw}]
    (+ (->> (get outcome (str p-1 p-2)) (get scores)) (get scores p-1))))

(s/fdef match-to-score :args (s/cat :match match?))

(defn part-1
  [input]
  (->>
   (parse-input input)
   (map match-to-score)
   (reduce + 0)))

(defn part-2
  [input]
  (->>
   (parse-input input)
   (map to-strategy)
   (map to-match)
   (map match-to-score)
   (reduce + 0)))

(def output
  {"Part One" (-> (slurp "resources/input/day_02.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_02.txt") part-2)})
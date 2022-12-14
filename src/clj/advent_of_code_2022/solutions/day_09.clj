(ns advent-of-code-2022.solutions.day-09
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]))

(defn parse-input [input]
  (->> (s/assert string? input)
       str/split-lines
       (map #(re-matches #"(\w{1})\s+(\d+)" %))
       (map #(-> {} (assoc :dir (get % 1)) (assoc :len (read-string (get % 2)))))
       (s/assert (s/every (s/and #(string? (:dir %)) #(int? (:len %)))))))

(defn unfold-instructions [instructions]
  (->> (map #(repeat (:len %) %) instructions)
       flatten
       (map #(:dir %))))

(defn move-head [head instruction]
  (case instruction
    "U" (update head :y inc)
    "D" (update head :y dec)
    "L" (update head :x dec)
    "R" (update head :x inc)))

(comment
  (s/assert (s/spec #(= {:x 2 :y 2} %)) (move-head {:x 2 :y 1} "U"))
  (s/assert (s/spec #(= {:x -1 :y 0} %)) (move-head {:x 0 :y 0} "L"))
  (s/assert (s/spec #(= {:x 9 :y 8} %)) (move-head {:x 9 :y 9} "D"))
  (s/assert (s/spec #(= {:x -11 :y 22} %)) (move-head {:x -12 :y 22} "R")))

(defn adjust-int [from to] (if (not= from to) (if (> from to) (dec from) (inc from)) to))

(comment
  (s/assert (s/spec #(= 3 %)) (adjust-int 2 4))
  (s/assert (s/spec #(= 6 %)) (adjust-int 7 -2))
  (s/assert (s/spec #(= 5 %)) (adjust-int 5 5)))

(defn move-tail [tail head]
  (cond
    (and (< (abs (- (:x tail) (:x head))) 2) (< (abs (- (:y tail) (:y head))) 2)) tail
    (and (not= (:x tail) (:x head)) (not= (:y tail) (:y head)))
    (-> tail
        (update :x #(adjust-int % (:x head)))
        (update :y #(adjust-int % (:y head))))
    (not= (:x tail) (:x head)) (update tail :x #(adjust-int % (:x head)))
    (not= (:y tail) (:y head)) (update tail :y #(adjust-int % (:y head)))))

(comment
  (s/assert (s/spec #(= % {:x 1 :y 1})) (move-tail {:x 1 :y 1} {:x 2 :y 2}))
  (s/assert (s/spec #(= % {:x 2 :y 1})) (move-tail {:x 1 :y 1} {:x 3 :y 1}))
  (s/assert (s/spec #(= % {:x 3 :y 3})) (move-tail {:x 2 :y 2} {:x 9 :y 9})))

(defn part-1
  [input]
  (let [instructions (unfold-instructions (parse-input input))]
    (->> (loop
          [head {:x 0 :y 0}
           tail {:x 0 :y 0}
           x (first instructions)
           xs (rest instructions)
           visited #{}]
           (if (nil? x)
             visited
             (let [next-head (move-head head x)
                   next-tail (move-tail tail next-head)
                   next-visited (set/union visited #{next-tail})]
               (recur next-head next-tail (first xs) (rest xs) next-visited))))
         count)))

(defn part-2
  [input]
  (let [instructions (unfold-instructions (parse-input input))]
    (->> (loop
          [rope '()
           head {:x 0 :y 0 :root true}
           tail (repeat 9 {:x 0 :y 0})
           x (first instructions)
           xs (rest instructions)
           visited #{}]
           (if (nil? x)
             visited
             (if (nil? head)
               (recur '() (first (reverse rope)) (rest (reverse rope)) (first xs) (rest xs) visited)
               (let [next-head (if (:root head) (move-head head x) head)
                     next-tail (cons (move-tail (first tail) next-head) (rest tail))
                     next-visited (set/union visited #{next-tail})]
                 (recur
                ; add the head to our rope
                  (cons next-head rope)
                ; the next part of the tail is the new head
                  (first next-tail)
                ; the rest of the tail is the new tail
                  (rest next-tail)

                ; only advance instructions if we are at the root
                  x
                  xs
                  next-visited)))))
         count)))

(def output
  {"Part One" (-> (slurp "resources/input/day_09.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_09.txt") part-2)})
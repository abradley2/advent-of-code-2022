(ns advent-of-code-2022.solutions.day-01)

(defn parse-input [input]
  (->> (.split input "")
       (map #(cond
               (= % "(") :up
               (= % ")") :down
               :else (throw (new AssertionError (str "Invalid symbol found in input: " %)))))))

(defn part-1 [input]
  (->> (parse-input input)
       (reduce
        #(cond
           (= %2 :up) (inc %1)
           (= %2 :down) (dec %1))
        0)))

(defn part-2 [input]
  (loop
   [instructions (parse-input input)
    data {:floor 0 :position 0}]
    (some->
     (first instructions)
     (#(cond
         (= % :up) (update data :floor inc)
         (= % :down) (update data :floor dec)))
     (as-> next
           (update next :position inc)
       (if (= (:floor next) -1)
         (:position next)
         (recur (rest instructions) next))))))

(def output
  {"Part One" (-> (slurp "resources/input/day_01.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_01.txt") part-2)})
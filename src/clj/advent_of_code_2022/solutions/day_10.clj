(ns advent-of-code-2022.solutions.day-10
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]))

(defn parse-line [input-line]
  (some
   identity
   [(some->
     (re-matches #"addx\s+(-?\d+)" input-line)
     (#(merge {:op :addx} {:val (read-string (get % 1))})))
    (some->
     (re-matches #"noop" input-line)
     ((constantly {:op :noop})))]))

(defn parse-input [input]
  (->> (str/split-lines input)
       (map parse-line)))

(defn to-cmd [op]
  (->> (case (:op op)
         :addx {:delay 2 :run #(+ % (:val op))}
         :noop {:delay 1 :run identity}
         nil)
       (s/assert some?)))

(defn run-cmd [cmd state]
  (if (= 0 (dec (:delay cmd))) [nil (-> state ((:run cmd)))]
      [(update cmd :delay dec) state]))

(defn score-cycle-map [cycle-map]
  (reduce
   #(+ %1 (* %2 (get cycle-map %2)))
   0
   '(20 60 100 140 180 220)))

(defn create-cycle-map [initial-ops]
  (loop
   [op (first initial-ops)
    next (rest initial-ops)
    cmd-slot nil
    state 1
    cycle 2
    cycle-map {1 1}]
    (cond
      (some? cmd-slot) (let [[next-cmd next-state] (run-cmd cmd-slot state)]
                         (recur
                          op
                          next
                          next-cmd
                          next-state
                          (inc cycle)
                          (assoc cycle-map cycle next-state)))
      (and (nil? cmd-slot) (nil? op)) (into (sorted-map) cycle-map)
      :else (recur (first next) (rest next) (to-cmd op) state cycle cycle-map))))

(defn part-1
  [input]
  (let [initial-ops (parse-input input)]
    (-> (create-cycle-map initial-ops)
        score-cycle-map)))

(defn partition-cycles [cycle-map]
  (loop
   [cycle-list (into (vector) cycle-map)
    partitioned-list '()]
    (if (empty? cycle-list)
      (reverse partitioned-list)
      (recur
       (drop 40 cycle-list)
       (cons (->> (take 40 cycle-list) (map second)) partitioned-list)))))

(defn draw-sprite [sprite-position rendered-row]
  (let [bounds #(min 39 (max 0 %))
        draw #(if (= :px %) :px :sprite)]
    (-> rendered-row
        (update (bounds (dec sprite-position)) draw)
        (update (bounds sprite-position) draw)
        (update (bounds (inc sprite-position)) draw))))

(defn draw-pixel [pixel-position rendered-row]
  (update
   rendered-row
   pixel-position
   #(if (= % :sprite) :px %)))

(defn clear-sprites [rendered-row]
  (mapv
   #(if (= % :sprite) nil %)
   rendered-row))

(defn print-screen [screen]
  (let [print-px (fn [screen-row] (map #(if (= % :px) "#" ".") screen-row))]
    (str/join "\n" (->> (map print-px screen) (map #(str/join "" %))))))

(defn part-2
  [input]
  (let [cycle-map (-> (parse-input input) create-cycle-map)
        partitions (->> (partition-cycles cycle-map) (filter #(= 40 (count %))))]
    (-> (loop
         [pixel 0
          row nil
          screen partitions
          rendered-row nil
          rendered-screen nil]
          (if (nil? (first row))
            (if (nil? (first screen))
              (reverse (cons rendered-row rendered-screen))
              (recur
               0
               (first screen)
               (rest screen)
               (apply vector (repeat 40 nil))
               (if (nil? rendered-row)
                 rendered-screen
                 (cons (clear-sprites rendered-row) rendered-screen))))
            (recur
             (inc pixel)
             (rest row)
             screen
             (->> (draw-sprite (first row) rendered-row) (draw-pixel pixel) clear-sprites)
             rendered-screen)))
        print-screen)))


(def output
  {"Part One" (-> (slurp "resources/input/day_10.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_10.txt") part-2)})
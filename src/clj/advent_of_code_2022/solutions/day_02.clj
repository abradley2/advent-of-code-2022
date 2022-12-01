(ns advent-of-code-2022.solutions.day-02)

(defn to-dimensions [vec]  {:length (first vec) :width (second vec) :height (last vec)})

(defn cubic-volume [dimensions] (reduce * 1 (vals dimensions)))

(defn smallest-face [dimensions]
  (let [min-1 (apply min (vals dimensions))]
    (->> [min-1 (or (filter #(not= % min-1) (vals dimensions)) min-1)]
         (reduce + 0)
         (* 2))))

(defn to-faces [dimensions]
  (let [l (:length dimensions)
        w (:width dimensions)
        h (:height dimensions)]
    [(* l w) (* w h) (* h l)]))

(defn to-area
  "2*l*w + 2*w*h + 2*h*l"
  [dimensions]
  (let [faces (to-faces dimensions)]
    (reduce + (apply min faces) (map #(* % 2) faces))))

(defn parse-input [input]
  (->> (.split input "\n")
       (map (comp to-dimensions #(map read-string %) #(.split % "x")))))

(defn part-1
  [input]
  (->> (parse-input input)
       (map to-area)
       (reduce + 0)))

(defn part-2
  [input]
  (->> (parse-input input)
       (map #(+ (smallest-face %) (cubic-volume %)))))

(def output
  {"Part One" (-> (slurp "resources/input/day_02.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_02.txt") part-2)})
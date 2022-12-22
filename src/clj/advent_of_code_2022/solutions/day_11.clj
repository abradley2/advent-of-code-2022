(ns advent-of-code-2022.solutions.day-11
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]))

(defn parse-condition [monkey-id raw-cond]
  (let [[compare, effect] (.split raw-cond ":")]
    {:compare #(= % (read-string (-> (re-matches #"\s*If\s+(.+)" (.trim compare)) (get 1))))
     :effect (or
              (some->> (re-matches #"throw\s+to\s+monkey\s+(\d+)" (.trim effect))
                       (#(get % 1))
                       (partial (fn [to-id item all-monkies]
                                  (map
                                   (fn [monkey]
                                     (cond
                                       (= (:name monkey) monkey-id)
                                       (assoc monkey :items (filter #(= item %) (:items monkey)))
                                       (= (:name monkey) to-id)
                                       (update monkey :items #(concat item (cons % nil)))
                                       :else monkey))
                                   all-monkies))))
              effect)}))

(defn parse-test [monkey-id raw-test]
  (let [lines (.split raw-test "\n")
        test-stmt (first lines)
        cond-stmts (rest lines)]
    {:check (s/assert some? (some
                             identity
                             [(some-> (re-matches #"\s*divisible\s+by\s+(\d+).*" test-stmt)
                                      (get 1)
                                      read-string
                                      ((fn [v] #(= 0 (mod v %)))))]))
     :conditions (map (partial parse-condition monkey-id) cond-stmts)}))

(defn run-operation [old op]
  ((:fn op)
   (if (= (:arg-1 op) :old) old (:arg-1 op))
   (if (= (:arg-2 op) :old) old (:arg-2 op))))

(defn parse-operation [raw-operation]
  (as-> (re-matches #"\s*new\s*=\s*(old|\d+)\s*([\+\-\*]{1})\s*(old|\d+).*" raw-operation) matches
    {:arg-1 (->> (get matches 1) (s/assert some?) (#(if (= % "old") :old (read-string %))))
     :fn (read-string (s/assert some? (get matches 2)))
     :arg-2 (->> (get matches 3) (s/assert some?) (#(if (= % "old") :old (read-string %))))}))

(defn parse-trait [monkey-id raw-trait]
  (let [[name body] (str/split raw-trait #":" 2)]
    (cond
      (= name "Starting items") {:items (read-string (str "(" body ")"))}
      (= name "Operation") {:operation (parse-operation body)}
      (= name "Test") {:test (parse-test monkey-id body)})))

(defn parse-monkey [raw-monkey]
  (let [lines (.split raw-monkey (str #"\n\s{2}(?=\w)"))
        id (re-find #"\d+" (first lines))
        traits (->> (map (partial parse-trait id) (rest lines)) (reduce merge {}))]
    (merge {:id id} traits)))

(defn monkey-a-round [initial-monkies]
  (loop
   [monkey-idx 0
    monkies initial-monkies
    round 1]
    (if (nil? (get monkies monkey-idx))
      (if (= round 20)
        monkies
        (recur
         0
         monkies
         (inc round)))
      (let []
        (recur
         (inc monkey-idx)
         monkies
         round)))))

(defn parse-input [input]
  (->> (.split (s/assert string? input) (str #"\n\n"))
       (map parse-monkey)))

(defn part-1
  [input]
  (-> (parse-input input) monkey-a-round))

(defn part-2
  [_input]
  "Not implemented")

(def output
  {"Part One" (-> (slurp "resources/input/day_11.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_11.txt") part-2)})
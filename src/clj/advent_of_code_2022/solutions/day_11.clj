(ns advent-of-code-2022.solutions.day-11
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]))

(defn parse-condition [raw-cond]
  (let [[compare, effect] (.split raw-cond ":")]
    {:compare #(= % (read-string (-> (re-matches #"\s*If\s+(.+)" (.trim compare)) (get 1))))
     :effect (->> (or
                   (some->> (re-matches #"throw\s+to\s+monkey\s+(\d+)" (.trim effect))
                            (#(get % 1))
                            (partial (fn [to-id item all-monkies]
                                       (mapv
                                        (fn [monkey]
                                          (cond
                                            (= (:id monkey) to-id)
                                            (update monkey :items #(concat % (cons item nil)))
                                            :else monkey))
                                        all-monkies))))
                   nil)
                  (s/assert some?))}))

(defn parse-test [raw-test]
  (let [lines (.split raw-test "\n")
        test-stmt (first lines)
        cond-stmts (rest lines)]
    {:check (s/assert some? (some
                             identity
                             [(some-> (re-matches #"\s*divisible\s+by\s+(\d+).*" test-stmt)
                                      (get 1)
                                      read-string
                                      ((fn [v] #(= 0 (mod % v)))))]))
     :conditions (map parse-condition cond-stmts)}))

(defn divisibility [sut total-sum prod-nums]
  (loop
   [prod-num (first prod-nums)
    next (rest prod-nums)
    ]
   )
  )

; a number is divisible by another number, if one of the product of that numbers
; is divisible by that number

; (+ 5 (* 3 7 8 9))
; 1517
; this number is divisble by 37

(defn run-operation [old op]
  (println old)
  ((:fn op)
   (if (= (:arg-1 op) :old) old (:arg-1 op))
   (if (= (:arg-2 op) :old) old (:arg-2 op))))

(defn parse-operation [raw-operation]
  (as-> (re-matches #"\s*new\s*=\s*(old|\d+)\s*([\+\-\*]{1})\s*(old|\d+).*" raw-operation) matches
    {:arg-1 (->> (get matches 1) (s/assert some?) (#(if (= % "old") :old (read-string %))))
     :fn (s/assert some? (-> (get matches 2) read-string eval))
     :arg-2 (->> (get matches 3) (s/assert some?) (#(if (= % "old") :old (read-string %))))}))

(defn parse-trait [raw-trait]
  (let [[name body] (str/split raw-trait #":" 2)]
    (cond
      (= name "Starting items") {:items (map long (read-string (str "(" body ")")))}
      (= name "Operation") {:operation (parse-operation body)}
      (= name "Test") {:test (parse-test  body)})))

(defn parse-monkey [raw-monkey]
  (let [lines (.split raw-monkey (str #"\n\s{2}(?=\w)"))
        id (re-find #"\d+" (first lines))
        traits (->> (map parse-trait (rest lines)) (reduce merge {}))]
    (-> (merge {:id id :inspection-count (long 0)} traits)
        (#(merge % (:test %)))
        (#(dissoc % :test)))))

(defn resolve-item [worry-mod item monkey all-monkies]
  (let [next-item (if (= worry-mod 1)
                    (run-operation item (:operation monkey))
                    (int (/ (run-operation item (:operation monkey)) worry-mod)))
        check-result ((:check monkey) next-item)
        effect-fn (reduce
                   (fn [acc condition]
                     (if (some? acc) acc
                         (if ((:compare condition) check-result) (:effect condition) nil)))
                   nil
                   (:conditions monkey))]
    (effect-fn next-item all-monkies)))

(defn monkey-a-round [round-count worry-mod initial-monkies]
  (loop
   [monkey-idx 0
    monkies initial-monkies
    round 1]
    (let [monkey (get monkies monkey-idx)]
      (if (nil? monkey)
        (if (= round round-count) monkies (recur 0 monkies (inc round)))
        (recur
         (inc monkey-idx)
         (-> (reduce (fn [next-monkies item]
                       (resolve-item worry-mod item monkey next-monkies)) monkies (:items monkey))
             (update-in [monkey-idx :inspection-count] #(Long/sum % (long (count (:items monkey)))))
             (assoc-in [monkey-idx :items] '()))
         round)))))

(defn parse-input [input]
  (->> (.split (s/assert string? input) (str #"\n\n"))
       (map parse-monkey)
       (into (vector))))

(defn part-1
  [input]
  (->>
   (parse-input input)
   (#(monkey-a-round 20 3 %))
   (map :inspection-count)
   sort
   reverse
   (take 2)
   (apply *)))

(defn part-2
  [input]
  (->>
   (parse-input input)
   (#(monkey-a-round 1000 1 %))
   (map :inspection-count)))

(def output
  {"Part One" (-> (slurp "resources/input/day_11.txt") part-1)
   "Part Two" ""})
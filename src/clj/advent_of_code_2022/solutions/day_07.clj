(ns advent-of-code-2022.solutions.day-07
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]))

(defn get-command [line]
  (some-> (re-matches #"\$\s+(\w+)\s*(.*)" line)
          (as-> matches
                {:fn (get matches 1)
                 :arg (get matches 2)})
          (#(if (= "" (:arg %)) (assoc % :arg nil) %))))

(defn get-output [item]
  (let [dir-info (some-> (re-matches #"dir\s+(.+)" item)
                         (as-> matches {:type :dir
                                        :dir-name (get matches 1)}))
        file-info (some-> (re-matches #"^(?!dir)(\d+)\s+(.+)" item)
                          (as-> matches
                                {:type :file
                                 :file-size (->> (get matches 1) read-string (s/assert int?))
                                 :file-name (get matches 2)}))]
    (s/assert some? (or dir-info file-info))))

(defn read-terminal [input]
  (let
   [terminal (->>
              (str/split input #"\$")
              (map #(str "$" %))
              rest
              (map (fn [cmd-out]
                     (let [lines (str/split cmd-out #"\n")]
                       {:cmd (->> (get-command (first lines)) (s/assert some?))
                        :out (->>
                              (rest lines)
                              (map get-output)
                              (#(if (empty? %) nil %)))}))))]
    terminal))

; all directories must have their path end in "/"
(def cap-dir #(if (str/ends-with? % "/") % (str % "/")))

(defn clean-file-tree [file-tree]
  (vals (reduce
         #(assoc %1 (str (:type %2) (:path %2) (:file-name %2)) %2)
         {}
         file-tree)))

(defn create-file-tree [terminal]
  (loop
   [current-root "/"
    files '({:type :dir :path "/"})
    line (first terminal)
    next-lines (rest terminal)]
    (if
     (nil? line)
      (clean-file-tree files)
      (let [func (->> line (:cmd) (:fn) (s/assert some?))
            out (:out line)
            arg (->> line (:cmd) (:arg) (s/assert (s/or :nil nil? :string string?)))]
        (case func
          "cd" (let [next-root
                     (cap-dir (case arg
                                "/" "/"
                                ".." (->> (str/split current-root #"/") drop-last (str/join "/"))
                                (str current-root arg "/")))]
                 (recur next-root
                        (if next-root (cons {:type :dir :path next-root} files) files)
                        (first next-lines)
                        (rest next-lines)))
          "ls" (recur
                current-root
                (flatten
                 (list files (map
                              #(case (:type %)
                                 :file (merge % {:path (cap-dir (str current-root (:dir-name %)))})
                                 :dir {:type :dir :path (cap-dir (str current-root (:dir-name %)))})
                              out)))
                (first next-lines)
                (rest next-lines))
          (s/assert nil? "Error: func should be either 'cd' or 'ls'"))))))

(defn files-under [path file-tree]
  (->> (filter  #(= (:type %) :file) file-tree)
       (filter #(str/starts-with? (:path %) path))))

(defn get-dir-sizes [file-tree]
  (->> (reduce
        (fn [acc cur]
          (if (= (:type cur) :dir)
            (assoc acc (:path cur) 0)
            acc))
        {}
        file-tree)
       (map
        (fn [[path size]]
          {:path path :size
           (reduce
            #(+ %1 (:file-size %2))
            size
            (files-under path file-tree))}))))

(defn part-1
  [input]
  (->> (read-terminal input)
       create-file-tree
       get-dir-sizes
       (map :size)
       (filter #(< % 100000))
       (reduce + 0)))

(defn get-first-past [n coll]
  (->> (sort coll)
       (reduce
        #(if (and (= %1 nil) (< n %2)) %2 %1)
        nil)))

(defn get-dir-deletion-target [dir-size-map]
  (let [target-usage 40000000
        current-usage (get dir-size-map "/")
        space-needed (- current-usage  target-usage)]
    (get-first-past space-needed (vals dir-size-map))))

(defn part-2
  [input]
  (->> (read-terminal input)
       create-file-tree
       ((fn [file-tree]
          (let [dir-paths (->> (filter #(= (:type %) :dir) file-tree) (map :path))
                files (filter #(= (:type %) :file) file-tree)]
            (reduce
             #(assoc %1 %2 (->> (files-under %2 files) (map :file-size) (reduce + 0)))
             {}
             dir-paths))))
       get-dir-deletion-target))

(def output
  {"Part One" (-> (slurp "resources/input/day_07.txt") part-1)
   "Part Two" (-> (slurp "resources/input/day_07.txt") part-2)})
(ns wh.components.stats.db)

(defn x-axis [stats stat]
  {:domain [0 (dec (max 4 (count (get stats stat))))]
   :range [0 300]
   :visible false})

(defn y-axis [stats stat]
  {:domain [0 (apply max (map :count (get stats stat)))]
   :range [100 0]
   :visible false})

(defn stat-total [stats stat]
  (:count (last (get stats stat))))

(defn stat-change [stats stat]
  (let [current (:count (last (get stats stat)))
        previous (:count (last (drop-last (get stats stat))))
        change (when previous (/ (- current previous) previous))]
    (cond
      (nil? change) nil
      (zero? change) "0%"
      (pos? change) (str "+" (int (* change 100.0)) "%")
      :otherwise (str (int (* change 100.0)) "%"))))

(defn chart-values [stats stat]
  (let [counts (as-> (map :count (get stats stat)) counts
                     (if (< (count counts) 4)
                       (take-last 4 (into [0 0 0 0] counts))
                       counts))]
    (vec (map-indexed vector counts))))

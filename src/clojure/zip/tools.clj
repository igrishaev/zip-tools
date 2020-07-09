(ns clojure.zip.tools
  (:require
   [clojure.zip :as zip]))


(defn ->layer [loc]
  (some->> loc
           zip/down
           (iterate zip/right)
           (take-while some?)))


(defn iter-depth [loc]
  (->> loc
       (iterate zip/next)
       (take-while (complement zip/end?))))


(defn by-layers [loc]
  (->> [loc]
       (iterate #(mapcat ->layer %))
       (take-while seq)))


(defn iter-breadth [loc]
  (apply concat (by-layers loc)))


#_
(def _data
  [[:rub :usd]
   [:usd :eur]
   [:usd :lir]
   [:usd :yen]
   [:yen :din]
   [:lir :eur]
   [:eur :lir]])


(def _data
  [[:rub :usd]
   [:usd :eur]

   [:usd :rub]
   [:eur :usd]

   [:rub :lir]
   [:lir :eur]])



(def _data
  [[1 2]
   [1 3]
   [2 5]
   [2 10]
   [3 6]
   [3 10]])

#_
(defn get-vals [val]
  (seq (for [[from to] _data
             :when (= from val)]
         to)))



(def _z (zip/zipper
         (constantly true)
         get-vals
         nil
         1))


#_
(->> _z
     iter-breadth
     (take 20)
     (filter
      (fn [loc]
        (-> loc zip/node (= :din))))
     (map (fn [loc]
            (conj (zip/path loc) (zip/node loc)))))

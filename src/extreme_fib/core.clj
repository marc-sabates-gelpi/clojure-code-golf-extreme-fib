(ns extreme-fib.core
  (:require [clojure.math.numeric-tower :as math])
  (:gen-class))

;; Improvements:
;; * Could pfib use concurrent loop?

(defn fib [n]
  (loop [a 0N b 1N c n]
    (if (>= 0 c)
      a
      (recur (+ a b) a (dec c)))))

;; extreme-fib.core> (time (do (fact 100000) nil))
;; "Elapsed time: 5530.880174 msecs"
;; nil
(def fact (memoize (fn [n]
                     (reduce * (range 1N (inc n))))))

;; extreme-fib.core> (time (do (fact 100000) nil))
;; OutOfMemoryError Java heap space  java.util.Arrays.copyOfRange (Arrays.java:3592)
;; (def facts (reductions * (iterate inc 1N)))
;; (defn fact [n]
;;   (nth facts (dec n) 1N))

(def p (memoize (fn [n k]
                  (/ (fact n) (* (fact k) (fact (- n k)))))))

(defn pfib [N]
  (let [n (dec N) end (math/floor (/ n 2))]
    (loop [k end sum 0N]
      (if (< k 0)
        sum
        (recur (dec k) (+ sum (p (- n k) k)))))))

(defn bfib [n]
  "Binet's formula"
  (/ (- (math/expt (inc (math/sqrt 5)) n) (math/expt (- 1 (math/sqrt 5)) n))
     (* (math/expt 2 n) (math/sqrt 5))))

(defn ffib [n]
  "Approximation formula"
  (* (/ 1 (math/sqrt 5))
     (math/expt (/ (inc (math/sqrt 5)) 2) (inc n))))

(defn extreme-fib
  ([] (extreme-fib 1000 1000000000))
  ([digit n] (time (take digit
                         (str
                          (fib n))))))

(defn -main
  [& args]
  (let [[a b & rest] args]
  (println (extreme-fib (bigint a) (bigint b)))))

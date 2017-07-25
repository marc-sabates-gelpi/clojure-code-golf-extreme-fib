(ns extreme-fib.core
  (:require [clojure.math.numeric-tower :as math])
  (:gen-class))

;; Improvements:
;; * Could fact make the most of memoization and recursive calls or we'll run out of stack space?
;; * Could be better to have an iterative fact & concurrent loop?
;; Take a look to this https://stackoverflow.com/questions/1662336/clojure-simple-factorial-causes-stack-overflow#1663053
;; * Could pfib loop from k=end to k=0 so that it would call fact from 0 to n?
;; * Could pfib use concurrent loop?

;; So far:
;; [fib]
;; extreme-fib.core> (extreme-fib 100 10000)
;; "Elapsed time: 16.249762 msecs"
;; (\3 \3 \6 \4 \4 \7 \6 \4 \8 \7 \6 \4 \3 \1 \7 \8 \3 \2 \6 \6 \6 \2 \1 \6 \1 \2 \0 \0 \5 \1 \0 \7 \5 \4 \3 \3 \1 \0 \3 \0 \2 \1 \4 \8 \4 \6 \0 \6 \8 \0 \0 \6 \3 \9 \0 \6 \5 \6 \4 \7 \6 \9 \9 \7 \4 \6 \8 \0 \0 \8 \1 \4 \4 \2 \1 \6 \6 \6 \6 \2 \3 \6 \8 \1 \5 \5 \5 \9 \5 \5 \1 \3 \6 \3 \3 \7 \3 \4 \0 \2)
;; [pfib]
;; extreme-fib.core> (extreme-fib 100 10000)
;; "Elapsed time: 446490.359854 msecs"
;; (\3 \3 \6 \4 \4 \7 \6 \4 \8 \7 \6 \4 \3 \1 \7 \8 \3 \2 \6 \6 \6 \2 \1 \6 \1 \2 \0 \0 \5 \1 \0 \7 \5 \4 \3 \3 \1 \0 \3 \0 \2 \1 \4 \8 \4 \6 \0 \6 \8 \0 \0 \6 \3 \9 \0 \6 \5 \6 \4 \7 \6 \9 \9 \7 \4 \6 \8 \0 \0 \8 \1 \4 \4 \2 \1 \6 \6 \6 \6 \2 \3 \6 \8 \1 \5 \5 \5 \9 \5 \5 \1 \3 \6 \3 \3 \7 \3 \4 \0 \2)

(defn fib [n]
  (loop [a 0N b 1N c n]
    (if (>= 0 c)
      a
      (recur (+ a b) a (dec c)))))

(def fact (memoize (fn [n]
                     (reduce * (range 1N (inc n))))))

;; XXX All these implementations fail with:
;; IndexOutOfBoundsException   clojure.lang.RT.nthFrom (RT.java:885)
;; (def facts (reductions * (iterate inc 1N)))
;; (def facts (lazy-cat [1N] (map * facts (iterate inc 2N))))
;; (defn fact [n]
;;   (nth facts (dec n)))

(def p (memoize (fn [n k]
                  (/ (fact n) (* (fact k) (fact (- n k)))))))

(defn pfib [N]
  (let [n (dec N) end (math/floor (/ n 2))]
    (loop [k end sum 0N]
      (if (< k 0)
        sum
        (recur (dec k) (+ sum (p (- n k) k)))))))

(defn extreme-fib
  ([] (extreme-fib 1000 1000000000))
  ([digit n] (time (take digit
                         (str
                          (fib n))))))

(defn -main
  [& args])

(ns boids.vector_test
  (:require [clojure.test :refer :all]
            [boids.vector :refer :all]))

(deftest div-test
  (testing "vector.div test"
    (is (= [2 2]
           (div [4 4] 2)))))

(deftest add-test
  (testing "vector.add test"
    (is (= [2 2]
           (add [1 1] [1 1])))))

(deftest sub-test
  (testing "vector.sub test"
    (is (= [2 2]
           (sub [4 4] [2 2])))))

(deftest mult-test
  (testing "vector.mult test"
    (is (= [4 6]
           (mult [2 3] 2)))))

(deftest dot-test
  (testing "vector.dot test"
    (is (= 33
           (dot [5 2] [5 4])))))

(deftest mag-sq-test
  (testing "vector.mag-sq test"
    (is (= 10
           (mag-sq [1 3])))))

(deftest mag-test
  (testing "vector.mag test"
    (is (= 3.0
           (mag [3 0])))))

(deftest normalize-test
  (testing "vector.normalize"
    (is (= [0.9486832980505138 0.31622776601683794]
           (normalize (3 1))))))

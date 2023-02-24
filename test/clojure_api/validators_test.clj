(ns clojure_api.validators_test
  (:require [clojure.test :refer :all]
            [clojure_api.validators :as v]))

(deftest validate-name-test
  (testing "Answers true"
    (are [name] (= true (v/validate-name name))
      "bob"     
      "Alice 2nd" 
      "John's"   ))

  (testing "Answers false"
    (are [name] (= false (v/validate-name name))
      34
      false
      89.3)))

(deftest validate-sex-test
  (testing "Answers true"
    (are [sex] (= true (v/validate-sex sex))
      "m"
      "M"
      "f"
      "F"
      "n"
      "N"))

  (testing "Answers false"
    (are [sex] (= false (v/validate-sex sex))
      "m/M"
      "f/F"
      "n/N"
      "rff"
      "234"
      "Male"
      90)))

(deftest validate-age-test
  (testing "Answers true"
    (are [age] (= true (v/validate-age age))
      45
      20
      0
      99))

  (testing "Answers false"
    (are [age] (= false (v/validate-age age))
      34.3
      false
      "32"
      -1
      100)))

(deftest validate-uuid-test
  (testing "Answers true"
    (are [uuid] (= true (v/validate-uuid uuid))
      "392ebaf2-6f5d-46e2-b46c-2f36d83e6624"
      "02a84506-eb41-4f0d-b67b-30c45a97b43d"
      "e0794e0d-9435-413d-a646-edb3b7315939"))

  (testing "Answers false"
    (are [uuid] (= false (v/validate-uuid uuid))
      34.3
      false
      230
      "aeaf4-arfr-3aef-aref"
      "name"
      nil
      )))

(deftest validate-date-of-birth-test
  (testing "Answers true"
    (are [date-of-birth] (= true (v/validate-date-of-birth date-of-birth))
      "2000-05-20"
      "1992-12-31"
      "2023-03-05"))

  (testing "Answers false"
    (are [date-of-birth] (= false (v/validate-date-of-birth date-of-birth))
      34.3
      false
      230
      "aeaf4-arfr-3aef-aref"
      "name"
      "20/12/2000"
      "1990-13-20")))


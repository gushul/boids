;; Complete minimal example

(ns boids.core
  "Complete minimal example"
  (:require [clojure2d.core :refer :all]
            [clojure2d.color :as c]
            [fastmath.core :as m]
            [boids.vector :as v]
            ))

;; be sure everything is fast as possible
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(def canvas-width 800)
(def canvas-height 600)
(def boid-radius 10)
(def MAX_SPEED 2)
(def MAX_FORCE 0.03)
(def BOID_SIZE 2.0)

(defn draw-boid [canvas radius {:keys [position velocity]}]
  (let [sides 3
        a (/ (* m/PI 2) sides)
        [x y] position
        direction (v/heading velocity)
        coordinates (map #(list (+ x (* radius (m/cos (+ direction (* a %)))))
                                (+ y (* radius (m/sin (+ direction (* a %))))))
                         (range sides))
        ]
    (apply triangle canvas (flatten coordinates))))

(defn init-boid [width height]
  (let [angle (rand (* Math/PI 2))]
    {:position [(rand-int width) (rand-int height)],
     :acceleration [0 0],
     :velocity [(Math/cos angle) (Math/sin angle)],
     }))

(defn separate [boid boids]
  (let [desiredseparation 25
        [steer count] (reduce (fn [[steer count] boid']
                              (let [d (v/dist (:position boid)
                                              (:position boid'))]
                                (if (and (> d 0) (< d desiredseparation))
                                  [(-> (v/sub (:position boid) (:position boid'))
                                       v/normalize
                                       (v/div d)
                                       (v/add steer))
                                   (inc count)]
                                  [steer count]
                                  )))
                            [[0 0] 0] boids)

        steer_average (if (> count 0)
                        (v/div steer count)
                        steer)

        steering (fn [steer]
                   (-> steer
                       v/normalize
                       (v/mult MAX_SPEED)
                       (v/sub (:velocity boid))
                       (v/limit MAX_FORCE)))
        ]
    (if (> (v/mag steer_average) 0)
      (steering steer_average)
      steer_average
      ))
  )

(defn align [boid boids]
  (let [neighbordist 50
        [sum count] (reduce (fn [[sum count] boid']
                              (let [d (v/dist (:position boid)
                                              (:position boid'))]
                                (if (and (> d 0) (< d neighbordist))
                                  [(v/add sum (:position boid')) (inc count)]
                                  [sum count]
                                  )))
                            [[0 0] 0] boids)
        line-up (fn [target boid]
                  (-> sum
                      v/normalize
                      (v/mult MAX_SPEED)
                      (v/sub (:velocity boid))
                      (v/limit MAX_FORCE)))
        ]
    (if (> count 0)
      (line-up (v/div sum count) boid)
      [0 0]
      ))
  )

(defn cohesion [boid boids]
  (let [neighbordist 50
        [sum count] (reduce (fn [[sum count] boid']
                              (let [d (v/dist (:position boid)
                                              (:position boid'))]
                                (if (and (> d 0) (< d neighbordist))
                                  [(v/add sum (:position boid')) (inc count)]
                                  [sum count]
                                  )))
                            [[0 0] 0] boids)
        seek (fn [target boid]
               (-> target
                   (v/sub (:position boid))
                   v/normalize
                   (v/mult MAX_SPEED)
                   (v/sub (:velocity boid))
                   (v/limit MAX_FORCE)))
        ]
    (if (> count 0)
      (seek (v/div sum count) boid)
      [0 0]
      )))

(defn flock [boid boids]
  (let [separation (v/mult (separate boid boids) 1.5)
        alignment  (v/mult (align boid boids) 1.0)
        coherence  (v/mult (cohesion boid boids) 1.0)
        acceleration (-> (:acceleration boid)
                         (v/add separation)
                         (v/add alignment)
                         (v/add coherence))
        ]
    (assoc boid :acceleration acceleration)))

(defn update-boid [boid]
  (-> boid
      (update :velocity v/add (:acceleration boid))
      (update :velocity v/limit MAX_SPEED)
      (update :position v/add (:velocity boid))
      (update :acceleration v/mult 0)
      ))

(defn borders [boid]
 (-> boid
    (update :position (fn [[x y]]
                        (let [x' (if (< x (- BOID_SIZE))
                                   (+ canvas-width BOID_SIZE)
                                   x)
                              y' (if (< y (- BOID_SIZE))
                                   (+ canvas-height BOID_SIZE)
                                   y)
                              ]
                          [x' y']
                          )))
    (update :position (fn [[x y]]
                        (let [x' (if (> x (+ canvas-width BOID_SIZE))
                                   (- BOID_SIZE)
                                   x)
                              y' (if (> y (+ canvas-height BOID_SIZE))
                                   (- BOID_SIZE)
                                   y)
                              ]
                          [x' y']
                          )))))

(defn run [boid boids]
  (-> boid
      (flock boids)
      update-boid
      borders
      ))

(defn draw
  "Some function decription."
  [canvas ;; canvas to draw on
   window ;; window bound to function (for mouse movements)
   ^long framecount ;; frame number
   boids] ;; state (if any)

  (set-background canvas :white)
  (set-color canvas :black)
  (doseq [boid boids] (draw-boid canvas boid-radius boid))

  (map #(run % boids) boids))

;; create canvas, display window and draw on canvas via draw function (60 fps)
;; show-window {:keys [canvas window-name w h fps draw-fn state draw-state setup hint refresher always-on-top? background]
(def window (show-window {:canvas (canvas canvas-width canvas-height),
                          :window-name "Boids simulation.",
                          :fps 60,
                          :draw-fn draw,
                          :setup (fn [canvas window] (repeatedly 100 #(init-boid canvas-width canvas-height)))}))

(defn main []
  window)

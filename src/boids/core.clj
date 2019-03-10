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

(defn draw-boid [canvas radius {:keys [x y direction]}]
  (let [sides 3
        a (/ (* m/PI 2) sides)
        coordinates (map #(list (+ x (* radius (m/cos (+ direction (* a %)))))
                                (+ y (* radius (m/sin (+ direction (* a %))))))
                         (range sides)) 
        ]
    (apply triangle canvas (flatten coordinates))
    )
  )

(defn init-boid [width height]
  (let [angle (rand Math/PI)]
    {:position [(rand-int width) (rand-int height)],
     :acceleration [0 0],
     :velocity [(Math/cos angle) (Math/sin angle)],
     :r 2.0,
     :maxspeed 2,
     :maxforce 0.03}
    ))

(defn init []
  (repeatedly 10 (init-boid canvas-width canvas-height))
  )

(defn separate [boid boids]
  (let [desiredseparation 25.0
        steer [0 0 0]
        count 0 ;; how many many boids in the vicinity
        ]
    
    )
  
  )

(defn flock [boid boids]
  
  )

(defn draw
  "Some function decription."
  [canvas ;; canvas to draw on
   window ;; window bound to function (for mouse movements)
   ^long framecount ;; frame number
   state] ;; state (if any)

  (set-background canvas :white)
  (set-color canvas :black)
  (doseq [boid (state)] (draw-boid canvas boid-radius boid)) 

  (map #(flock % boids) boids)
  )

;; create canvas, display window and draw on canvas via draw function (60 fps) 
;; show-window {:keys [canvas window-name w h fps draw-fn state draw-state setup hint refresher always-on-top? background]
(def window (show-window {:canvas (canvas canvas-width canvas-height),
                          :window-name "Boids simulation.",
                          :fps 25,
                          :draw-fn draw,
                          :setup #(init)}))

(defn main [] window)


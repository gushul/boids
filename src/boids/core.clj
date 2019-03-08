;; Complete minimal example

(ns boids.core
  "Complete minimal example"
  (:require [clojure2d.core :refer :all]
            [clojure2d.color :as c]
            [fastmath.core :as m]))

;; be sure everything is fast as possible
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn draw-boid [canvas x y radius angle]
  (let [sides 3
        a (/ (* m/PI 2) sides)
        coordinates (map #(list (+ x (* radius (m/cos (+ angle (* a %)))))
                                (+ y (* radius (m/sin (+ angle (* a %))))))
                         (range sides)) 
        ]
    (apply triangle canvas (flatten coordinates))
    )
  )

(defn draw
  "Draw rotating rectangle. This function is prepared to be run in refreshing thread from your window."
  [canvas ;; canvas to draw on
   window ;; window bound to function (for mouse movements)
   ^long framecount ;; frame number
   state] ;; state (if any), not used here
  (let [midwidth (* 0.5 ^long (width canvas))] ;; find middle of the canvas

    (-> canvas ;; use canvas (context is already ready! It's draw function.)
        (set-color :black)
        (draw-boid  30  30 10 (rand m/PI))
        (draw-boid 130 130 10 (rand m/PI))
        (draw-boid 230 230 10 (rand m/PI))
        (draw-boid 330 330 10 (rand m/PI))
        (draw-boid 430 430 10 (rand m/PI))
        ;; (set-background :linen) ;; clear background with :inen color
        ;; (translate midwidth midwidth) ;; set origin in the middle
        ;; (rotate (/ framecount 100.0)) ;; rotate clockwise (based on number of frame)
        ;; (set-color :maroon) ;; set color to maroon
        ;; (crect 0 0 midwidth midwidth) ;; draw centered rectangle
        ;; (rotate (/ framecount -90.0)) ;; rotate counterclockwise
        ;; (set-color 255 69 0 200) ;; set color to orange with transparency
        ;; (crect 0 0 (* 0.9 midwidth) (* 0.9 midwidth))
        
        ))) ;; draw smaller rectangle

;; create canvas, display window and draw on canvas via draw function (60 fps) 
(def window (show-window (canvas 800 600) "Boids" draw))

(defn main [] window)


(ns ttt-ring.routes (:require [ring.adapter.jetty :as jetty]
            [ttt-ring.round :refer [start]]
            [tic-tac-toe.board :refer [new-board make-move winner board-size]]
            [tic-tac-toe.ai :refer [next-move]]
            [ring.util.response :refer [response file-response]]
            [clojure.data.json :as json]))

(defn game-index [req] {:status 200 :body "index" :headers {"Content-Type" "text/html"}})

(defn show-game [player board piece difficulty]
  (if (= player :human)
    (response (json/write-str {:board board :size (board-size board) :piece piece :state "playing" :difficulty difficulty}))
    (next-move board piece difficulty)))

(defn end-game [board]
  (let [state (if (winner board) "win" "draw")]
    (response (json/write-str {:board board :size (board-size board) :state state :winner (winner board)}))))

(defn create-game [req]
  (let [params (:form-params req)
        size (Integer. (get params "size"))
        difficulty (Integer. (get params "difficulty"))
        order (Integer. (get params "order"))]
  (start {:players (if (= order 1) [:human :ai] [:ai :human])
          :pieces (if (= order 1) [:X :O] [:O :X])
          :board (new-board size)
          :difficulty difficulty
          :move-fn show-game
          :end-fn end-game})))

(defn update-game [req]
  (let [params (:form-params req)
        move (Integer. (get params "move"))
        board (vec (map #(keyword %) (get params "board[]")))
        piece (keyword (get params "piece"))
        difficulty (Integer. (get params "difficulty"))]
    (start {:players [:ai :human]
            :pieces [:O :X]
            :board (make-move board move piece)
            :difficulty difficulty
            :move-fn show-game
            :end-fn end-game})))

(defn not-found [req]
 {:status 404
   :headers {"Content-Type" "text/html"}
   :body "Page Not Found"})

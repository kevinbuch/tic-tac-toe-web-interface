(ns ttt-ring.round
  (:require [tic-tac-toe.board :refer :all]))

(defn start [{:keys [players pieces board difficulty move-fn end-fn turn-fn]}]
  (let [move-fn move-fn
        end-fn end-fn
        turn-fn turn-fn]
  (if (over? board)
    (end-fn board)
    (let [player (first players)
          piece (first pieces)]
      (turn-fn piece board)
      (if (= :ai player)
        (recur {:players [(last players) (first players)]
                :pieces [(last pieces) (first pieces)]
                :board (make-move board (move-fn player board piece difficulty) piece)
                :difficulty difficulty
                :move-fn move-fn
                :end-fn end-fn
                :turn-fn turn-fn})
        (move-fn player board piece difficulty))))))

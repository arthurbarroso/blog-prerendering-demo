(ns app.events
  (:require [re-frame-cljs-http.http-fx]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::init-db
  (fn [_ _]
    {:db
       {:name "app"
        :loading false
        :error nil
        :users []}}))

(re-frame/reg-event-db
  ::change-name
  (fn [db [_ v]]
    (assoc db :name v)))

(re-frame/reg-event-db
  ::success
  (fn [db [_ result]]
    (assoc db :users (-> result :body :data))))

(re-frame/reg-event-db
  ::failure
  (fn [db [_ result]]
    (assoc db :users [] :http-failure true :http-error result)))

(re-frame/reg-event-fx
  ::fetch
  (fn [cofx [_ _]]
    {:db (assoc (:db cofx) :b true)
     :http-cljs {:method :get
                 :url "https://reqres.in/api/users?page=2"
                 :params {:testing true}
                 :timeout 8000
                 :on-success [::success]
                 :on-failure [::failure]}}))

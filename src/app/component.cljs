(ns app.component
  (:require [app.events :as events]
            [app.subs :as subs]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [reagent.dom.server :as dom-server]
            ["react-dom" :as react-dom]))

(defn counting-component [initial-data]
  (let [click-2 (reagent/atom 0)
        name (re-frame/subscribe [::subs/name])
        users (re-frame/subscribe [::subs/users])]
    (fn []
      [:div
       "The atom " [:code "click-count"] " has value: "
       @click-2 ". "
       [:p "Name has value " (if-not (false? @name) @name (:name initial-data))]
       [:p "Users:"
        (for [user (if-not (false? @users) @users (:users initial-data))]
          ^{:key (:id user)}
          [:p (:first_name user)])]
       [:p "Test 2"]
       [:input {:type "button" :value "Click me!"
                :on-click #(swap! click-2 inc)}]
       [:button {:on-click #(re-frame/dispatch [::events/change-name "teste"])}
        "Change name"]
       [:button {:on-click #(re-frame/dispatch [::events/fetch])}
        "Fetch users"]])))

(defn wrapped-counter []
  (re-frame/dispatch-sync [::events/init-db])
  (fn []
    [counting-component]))

(defn countinghtml []
  (dom-server/render-to-string [wrapped-counter]))

(defn ^:export countinghydrate []
  (let [cb (.getElementById js/document "root")]
    (react-dom/hydrate (reagent/as-element [wrapped-counter])
                       cb)))

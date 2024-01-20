(ns com.tdibar.home
  (:require [clojure.tools.logging :as log]
            [clojure.pprint :as pprint]
            [clj-http.client :as http]
            [com.biffweb :as biff]
            [com.tdibar.middleware :as mid]
            [com.tdibar.ui :as ui]
            [com.tdibar.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]))

(defn home-page [ctx]
  (ui/page
   ctx
  ;;  [:h2.text-2xl.font-bold [:code.text-green-400 (str "Who is Tyler DiBartolo?")]]
  ;;  [:div.flex.w-full.max-w-lg.items-center.space-x-2.bg-black.rounded-lg.p-2
  ;;   [:input.flex.h-10.w-full.border.border-input.px-3.py-2.text-sm.ring-offset-background.file:border-0.file:bg-transparent.file:text-sm.file:font-medium.placeholder:text-muted-foreground.focus-visible:outline-none.focus-visible:ring-2.focus-visible:ring-ring.focus-visible:ring-offset-2.disabled:cursor-not-allowed.disabled:opacity-50.bg-black.text-white.placeholder-gray-400.rounded-lg
  ;;    {:placeholder "curl https://api.example.com/data" :type "text"}]
  ;;   [:button.inline-flex.items-center.justify-center.rounded-md.text-sm.font-medium.ring-offset-background.transition-colors.focus-visible:outline-none.focus-visible:ring-2.focus-visible:ring-ring.focus-visible:ring-offset-2.disabled:pointer-events-none.disabled:opacity-50.h-10.px-4.py-2.bg-gray-800.text-white
  ;;    {:hx-get "/describe"
  ;;     :hx-target "#sparql-response"
  ;;     :hx-swap "innerHTML"}
  ;;    [:svg.h-4.w-4 {:stroke "currentColor"
  ;;                   :fill "none"
  ;;                   :stroke-linejoin "round"
  ;;                   :width "24"
  ;;                   :xmlns "http://www.w3.org/2000/svg"
  ;;                   :stroke-linecap "round"
  ;;                   :stroke-width "2"
  ;;                   :viewBox "0 0 24 24"
  ;;                   :height "24"}
  ;;     [:polygon {:points "5 3 19 12 5 21 5 3"}]]]]
   
   [:div.bg-black.bg-opacity-90.text-green-400.p-6.rounded-lg;;.w-full.max-w-lg
    [:div.flex.justify-between.items-center
     [:div.flex.gap-2.text-red-500
      [:div.w-3.h-3.rounded-full.bg-red-500]
      [:div.w-3.h-3.rounded-full.bg-yellow-500]
      [:div.w-3.h-3.rounded-full.bg-green-500]]
     [:p.text-sm "~/🐇" ]
      [:button.inline-flex.items-center.justify-center.rounded-md.text-sm.font-medium.ring-offset-background.transition-colors.focus-visible:outline-none.focus-visible:ring-2.focus-visible:ring-ring.focus-visible:ring-offset-2.disabled:pointer-events-none.disabled:opacity-50.h-10.px-4.py-2.bg-green-500.text-white
       {:hx-get "/describe"
        :hx-target "#sparql-response"
        :hx-swap "innerHTML"}
       [:svg.h-4.w-4 {:stroke "currentColor"
                      :fill "none"
                      :stroke-linejoin "round"
                      :width "24"
                      :xmlns "http://www.w3.org/2000/svg"
                      :stroke-linecap "round"
                      :stroke-width "2"
                      :viewBox "0 0 24 24"
                      :height "24"}
        [:polygon {:points "5 3 19 12 5 21 5 3"}]]]]
     
     [:font-mono.mt-4
      [:div 
       [:span "$ curl -X GET " 
        [:a {:href "https://tylerdibartolo.com/api/describe"} 
         [:span.undeline.decoration-white "tylerdibartolo.com/api/describe"]]]]
      [:div#sparql-response.whitespace-pre-wrap]]]
   
   ))

(def jtd-describe
  "@prefix ex:     <http://example.org/> .\n@prefix foaf:   <http://xmlns.com/foaf/0.1/> .\n@prefix org:    <http://example.org/organizations/> .\n@prefix rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n@prefix rdfs:   <http://www.w3.org/2000/01/rdf-schema#> .\n@prefix schema: <http://schema.org/> .\n@prefix skill:  <http://example.org/skills/> .\n\nex:TylerDiBartolo  rdf:type  foaf:Person;\n        ex:ethnicity         \"Italian-American\";\n        ex:generation        \"second\";\n        skill:hasExperience  ex:SalesConsulting , ex:SoftwareEngineering , ex:ProductManagement , ex:UXDesign;\n        skill:isSkilledIn    skill:IBMCloud , skill:UML , skill:Jira , skill:RDF , skill:Confluence , skill:Git , skill:Python , skill:AWS , skill:Clojure , skill:Java;\n        ex:workedAt          org:WarnerBrosDiscovery , org:IBM;\n        schema:interestedIn  ex:KnowledgeManagement , ex:Clojure , ex:TechnicalWriting , ex:JSONLD , ex:SymbolicAI , ex:SoftwareDesign , ex:DataArchitecture;\n        foaf:birthPlace      ex:BrooklynNY;\n        foaf:name            \"Tyler DiBartolo\" .\n")

(defn get-sparql [{:keys [session params] :as ctx}] 
  (biff/render [:div [:br] jtd-describe]))

(defn get-sparql-api [ctx]
  (log/info (pprint/pprint ctx))
  {:status 200
   :body jtd-describe
   :headers {"Content-Type" "application/x-turtle; charset=UTF-8"}})

(def plugin
  {:routes [["/" {:get home-page}]
            ["/describe" {:get get-sparql}]]
   :api-routes [["/api/describe" {:get get-sparql-api}]]})

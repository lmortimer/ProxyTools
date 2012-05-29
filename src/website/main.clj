(ns website.main
	(:use 		[compojure.core]
				[net.cgrand.enlive-html]
				[ring.middleware reload])

	(:require 	[compojure.route :as route]
			  	[compojure.handler :as handler]
			  	[clj-json.core :as json]))


(deftemplate index-template "index.html"
	[s])

(defroutes main-routes
	(GET "/" [] index-template)
	(GET "/er" [] "wtf")

	(GET "/json/:id" [id] {	:status 200
						:headers {"Content-Type" "application/json"}
						:body (json/generate-string {"hi" "there" "ok" id})})

	(route/resources "/")
	(route/not-found "Page not found"))

(def app
	(handler/site main-routes))
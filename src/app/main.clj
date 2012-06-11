(ns app.main
	(:use  	[ring.adapter.jetty :only [run-jetty]]
			[websocket.main :only [start-websocket-server]]
			[website.main :only [app]]))

(defn -main
  []
  	(run-jetty #'app {:port 1323 :join? false})
    (start-websocket-server)
    (println "Websocket listening on 127.0.0.1:8080")
    (println "Webserver listening on 127.0.0.1:1323"))
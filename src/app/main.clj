(ns app.main
	(:use  	[ring.adapter.jetty :only [run-jetty]]
			[websocket.main :only [start-websocket-server]]
			[website.main :only [app]]))

(defn -main
  []
  	(run-jetty #'app {:port 80 :join? false})
    (start-websocket-server)
    


	)
(ns app.main
	(:use 	[proxy.check :only [socks5]]
			[websocket.main :only [start-websocket-server]]))

(defn -main
  []
    (start-websocket-server)

	;(def proxies ["127.0.0.1:9050" "127.0.0.1:9050" "127.0.0.1:9051"])

	#_(for [proxy proxies]
		(future (socks5 proxy)))




	)
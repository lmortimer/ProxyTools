(ns app.main
	(:use 	[proxy.check :only [socks5]]
			[websocket.main :only [start-websocket-server]]))

(defn -main
  []
  ;(start-websocket-server))
  (println (socks5 "127.0.0.1:9050"))
  (println (socks5 "127.0.0.1:9051"))
  (println (socks5 "127.0.0.1:9052"))
  (println (socks5 "127.0.0.1:9050")))
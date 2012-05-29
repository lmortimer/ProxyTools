(ns websocket.main
	(:use 	[lamina.core]
			[aleph.http]
			[proxy.check :only [socks5]])

	(:require [clj-json [core :as json]]))

(defn ws-handler [channel request]
	(receive-all channel 
		(fn [msg]
			(let [	packet (json/parse-string msg)
					command (get packet "type")]

			(if (= command "single_proxy")
				(let [raw-response {"type" "single_proxy"
									"ip"	(get packet "ip")
									"alive"	(socks5 (get packet "ip"))}

					 encoded-response (json/generate-string raw-response)]

					(println encoded-response)
					(enqueue channel encoded-response)))

			(if (= command "check_proxies")
				(let [foo "bar"]
				(println (get packet "ips"))
				(println (type (get packet "ips")))))

			(println packet)
			;(enqueue channel msg)
			))))


(defn start-websocket-server
	[]
	(println "starting websocket server")
	(start-http-server ws-handler {:port 8080 :websocket true}))
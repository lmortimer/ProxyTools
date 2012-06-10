(ns websocket.main
	(:use 	[lamina.core]
			[lamina.executor]
			[aleph.http]
			[proxy.check :only [socks http]])

	(:require [clj-json [core :as json]]))


(defn ws-socks5
	[proxy channel]
	(let
		[result (socks proxy)
		raw-response {	"type" "single_proxy"
						"ip"   proxy
						"alive" result}
		encoded-response (json/generate-string raw-response)]

		(println "HELLO FROM INSIDE")
		;(println encoded-response)
		(enqueue channel encoded-response)

		))



(defn ws-handler [channel request]
	(receive-all channel 
		(fn [msg]
			(let [	packet (json/parse-string msg)
					command (get packet "type")]

			(if (= command "single_proxy")
				(let [raw-response {"type" "single_proxy"
									"ip"	(get packet "ip")
									"alive"	(socks (get packet "ip"))}

					 encoded-response (json/generate-string raw-response)]

					(println encoded-response)
					(enqueue channel encoded-response)))

			(if (= command "check_proxies")
				(do
					(println "checking multiple proxies")
					(doall (for [i (get packet "ips")]
						(future (ws-socks5 i channel))))
					;(doall (map fut-ws-socks5 (get packet "ips") '(channel)))
					))

			(println packet)
			;(enqueue channel msg)
			))))


(defn start-websocket-server
	[]
	(println "starting websocket server")
	(start-http-server ws-handler {:port 8080 :websocket true}))
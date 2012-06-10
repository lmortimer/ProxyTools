(ns websocket.main
	(:use 	[lamina.core]
			[lamina.executor]
			[aleph.http]
			[proxy.check :only [socks http]])

	(:require [clj-json [core :as json]]))


(defn ws-check-proxies
	[proxy proxy-type channel]
	(let
		[result (cond
					(= "SOCKS" proxy-type) (socks proxy)
					(= "HTTP" proxy-type) (http proxy))

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
					command (get packet "type")
					proxy-type (get packet "proxy_type")]

			(if (= command "check_proxies")
				(do
					(println "checking multiple proxies")
					(doall (for [i (get packet "ips")]
						(future (ws-check-proxies i proxy-type channel))))
					))

			(println packet)
			;(enqueue channel msg)
			))))


(defn start-websocket-server
	[]
	(println "starting websocket server")
	(start-http-server ws-handler {:port 8080 :websocket true}))
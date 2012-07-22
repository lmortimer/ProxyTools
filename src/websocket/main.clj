(ns websocket.main
	(:use 	[lamina.core]
			[lamina.executor]
			[aleph.http]
			[proxy.check :only [socks http location]])

	(:require [clj-json [core :as json]]))


(defn ws-check-proxies
	[proxy proxy-type channel]
	(let
		[result (cond
					(= "SOCKS" proxy-type) (socks proxy)
					(= "HTTP" proxy-type) (http proxy))

		raw-response {	"type" "single_proxy"
						"ip"   proxy
						"ptype" (:ptype result)
						"alive" (:up result)
						"location" (.toLowerCase (location proxy))
						"anonymity" (:anonymity result)}
		encoded-response (json/generate-string raw-response)]

		(enqueue channel encoded-response)))



(defn ws-handler [channel request]
	(receive-all channel 
		(fn [msg]
			(let [	packet (json/parse-string msg)
					command (get packet "type")
					proxy-type (get packet "proxy_type")]

			(if (= command "check_proxies")
				(do
					(doall (for [i (get packet "ips")]
						(future (ws-check-proxies i proxy-type channel))))
					))))))


(defn start-websocket-server
	[]
	(println "Starting websocket server")
	(start-http-server ws-handler {:port 8080 :websocket true}))
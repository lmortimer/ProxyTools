(ns proxy.check
  (:use [clojure.string :only [split join]]
        [clojure.java.io :only [reader]])
  (:import [java.net
            InetSocketAddress
            Proxy
            Proxy$Type
            URL
            URLConnection
            SocketException]))

(def judge "http://www.trueboolean.com")

(defn socks
  "Checks to see if a SOCKS5 proxy is alive"
  [ip]
  (let [[ip port] (split ip #":")
        address (new InetSocketAddress ip (Integer/parseInt port))
        prox (new Proxy (Proxy$Type/SOCKS) address)
        url (new URL judge)]

    (try
      (let [urlconn (.openConnection url prox)]

      (.setConnectTimeout urlconn 10000)
      
      (if (= 200 (.getResponseCode urlconn))
        true))

    (catch RuntimeException e
      (if (instance? SocketException (.getCause e))
        false)))))


(defn http
  "Checks to see if an HTTP proxy is alive"
  [ip]
  (let [[ip port] (split ip #":")
        address (new InetSocketAddress ip (Integer/parseInt port))
        prox (new Proxy (Proxy$Type/HTTP) address)
        url (new URL judge)]

    (try
      (let [urlconn (.openConnection url prox)]

      (.setConnectTimeout urlconn 10000)
      
      (if (= 200 (.getResponseCode urlconn))
        true))

    (catch RuntimeException e
      (if (instance? SocketException (.getCause e))
        false)))))
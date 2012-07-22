(ns proxy.check
  (:use [clojure.string :only [split join]]
        [clojure.java.io :only [reader]])
  (:import [java.net
            InetSocketAddress
            Proxy
            Proxy$Type
            URL
            URLConnection
            SocketException]
            [com.maxmind.geoip LookupService]))

(def judge "http://www.microsoft.com")
(def country-db (LookupService. "resources/GeoIP.dat"))


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
        {:up true
          :ptype "socks"
          :anonymity "Level 3"}))

    (catch RuntimeException e
      (if (instance? SocketException (.getCause e))
        { :up false
          :ptype "socks"
          :anonymity "-"})))))


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
        {:up true
          :ptype "http"
          :anonymity "HTTP untested"}))

    (catch RuntimeException e
      (if (instance? SocketException (.getCause e))
        {:up false
         :ptype "http"
         :anonymity "-"})))))


(defn location
  "Return the country code of the IP address."
  [ip]
  (let [address (first (split ip #":"))]
    (.getCode (.getCountry country-db address))))
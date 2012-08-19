
### general JDK definitions

JC	 = $(JAVA_HOME)/bin/javac
JDOC	 = $(JAVA_HOME)/bin/javadoc
JFLAGS   = -O -g -deprecation
#CLASSP   = -classpath "$(JAVA_HOME)/lib/classes.zip:.."
#JDCLASSP = -classpath "$(JAVA_HOME)/lib/classes.zip:.."
CLASSP   = -classpath ".."
JDCLASSP = -classpath ".."

JTAGS    = /usr/bin/ctags
JTFLAGS  = --java-types=+C -f -

MV       = mv -f


### java extensions

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $(CLASSP) $<


### all source files

classes = GlobalConstants.class \
	  Log.class \
	  URI.class \
	  MD5.class \
	  NVPair.class \
	  Cookie.class \
	  Cookie2.class \
	  Util.class \
	  Codecs.class \
	  LinkedList.class \
	  StreamDemultiplexor.class \
	  HTTPConnection.class \
	  HTTPResponse.class \
	  HttpOutputStream.class \
	  AuthorizationPrompter.class \
	  AuthorizationHandler.class \
	  AuthorizationInfo.class \
	  DefaultAuthHandler.class \
	  BufferedInputStream.class \
	  IdempotentSequence.class \
	  SocksClient.class \
	  RoRequest.class \
	  RoResponse.class \
	  Request.class \
	  Response.class \
	  CIHashtable.class \
	  HTTPClientModule.class \
	  HTTPClientModuleConstants.class \
	  RedirectionModule.class \
	  RetryModule.class \
	  RetryException.class \
	  AuthorizationModule.class \
	  CookieModule.class \
	  CookiePolicyHandler.class \
	  ContentMD5Module.class \
	  MD5InputStream.class \
	  HashVerifier.class \
	  DefaultModule.class \
	  TransferEncodingModule.class \
	  ContentEncodingModule.class \
	  ChunkedInputStream.class \
	  UncompressInputStream.class \
	  HttpHeaderElement.class \
	  FilenameMangler.class \
	  ModuleException.class \
	  ParseException.class \
	  ProtocolNotSuppException.class \
	  AuthSchemeNotImplException.class \
	  SocksException.class \
	  HttpURLConnection.class \
	  http/Handler.class \
	  https/Handler.class \
	  shttp/Handler.class


### targets 

all: $(classes)

doc::
	- $(RM) -r doc/api/*
	$(JDOC) $(JDCLASSP) -author -version -d doc/api -link http://java.sun.com/products/jdk/1.2/docs/api/ -windowtitle "HTTPClient API" HTTPClient HTTPClient.http HTTPClient.https
	find doc/api -type d -exec chmod 755 {} \;
	chmod -R a+r doc/api

tags::
	$(JTAGS) $(JTFLAGS) *.java *http*/*.java | perl -e '@lines = <>; print sort @lines;' > tags 2> /dev/null
#	should autosort, but sort seems screwed up
#	$(JTAGS) $(JTFLAGS) *.java *http*/*.java > /dev/null 2>&1

kit::
	- $(RM) HTTPClient.zip
	- $(RM) HTTPClient.tar.gz
	cd ../; zip -qr9 HTTPClient.zip HTTPClient
	cd ../; tar zhcf HTTPClient.tar.gz HTTPClient
	cd ../; $(MV) HTTPClient.zip HTTPClient
	cd ../; $(MV) HTTPClient.tar.gz HTTPClient


### Interface Dependencies

HTTPConnection.class \
Response.class \
StreamDemultiplexor.class \
DefaultModule.class \
RetryModule.class \
RespInputStream.class : GlobalConstants.class

Request.class \
HTTPClientModule.class \
CookiePolicyHandler.class \
AuthorizationHandler.class : RoRequest.class

Response.class \
HTTPClientModule.class \
CookiePolicyHandler.class \
AuthorizationHandler.class : RoResponse.class

HTTPConnection.class \
HTTPResponse.class \
RetryModule.class \
CookieModule.class \
RedirectionModule.class \
AuthorizationModule.class \
ContentMD5Module.class \
TransferEncodingModule.class \
ContentEncodingModule.class \
DefaultModule.class : HTTPClientModule.class

HTTPClientModule.class : HTTPClientModuleConstants.class

AuthorizationInfo.class \
AuthorizationModule.class \
DefaultAuthHandler.class : AuthorizationHandler.class

DefaultAuthHandler.class : AuthorizationPrompter.class

CookieModule.class : CookiePolicyHandler.class

ContentMD5Module.class \
MD5InputStream.class \
AuthorizationInfo.class : HashVerifier.class

Codecs.class : FilenameMangler.class



import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.URI;
import HTTPClient.NVPair;
import HTTPClient.AuthorizationInfo;
import HTTPClient.DefaultAuthHandler;
import HTTPClient.AuthorizationPrompter;


public class GetAuthInfo
{
    public static void main(String args[])  throws Exception
    {
	String pa_name = null, pa_pass = null;

	if (args.length == 4  &&  "-proxy_auth".startsWith(args[0]))
	{
	    pa_name = args[1];
	    pa_pass = args[2];

	    String[] tmp = { args[3] };
	    args = tmp;
	}

	if (args.length != 1  ||  args[0].equalsIgnoreCase("-help"))
	{
	    System.err.println("Usage: java GetAuthInfo [-proxy_auth <username> <password>] <url>");
	    System.exit(1);
	}

	URI url = new URI(args[0]);

	DefaultAuthHandler.setAuthorizationPrompter(new MyAuthPrompter(pa_name, pa_pass));
	HTTPConnection con = new HTTPConnection(url);
	HTTPResponse   rsp = con.Head(url.getPathAndQuery());

	int sts = rsp.getStatusCode();
	if (sts < 300)
	    System.out.println("No authorization required to access " + url);
	else if (sts >= 400  &&  sts != 401  &&  sts != 407)
	    System.out.println("Error trying to access " + url + ":\n" + rsp);
    }
}


class MyAuthPrompter implements AuthorizationPrompter
{
    private String  pa_name, pa_pass;
    private boolean been_here = false;

    MyAuthPrompter(String pa_name, String pa_pass)
    {
	this.pa_name = pa_name;
	this.pa_pass = pa_pass;
    }


    public NVPair getUsernamePassword(AuthorizationInfo challenge, boolean forProxy) {
	if (forProxy  &&  pa_name != null)
	{
	    if (been_here)
	    {
		System.out.println();
		System.out.println("Proxy authorization failed");
		return null;
	    }

	    been_here = true;
	    return new NVPair(pa_name, pa_pass);
	}

	if (been_here)
	{
	    System.out.println();
	    System.out.println("Proxy authorization succeeded");
	}


	// print out all challenge info

	System.out.println();
	if (forProxy)
	    System.out.println("The proxy requires authorization");
	else
	    System.out.println("The server requires authorization for this resource");

	System.out.println();
	System.out.println("Scheme: " + challenge.getScheme());
	System.out.println("Realm:  " + challenge.getRealm());

	System.out.println();
	System.out.println("Add the following line near the beginning of your application:");
	System.out.println();

	if (challenge.getScheme().equalsIgnoreCase("Basic"))
	    System.out.println("    AuthorizationInfo.addBasicAuthorization(\""+
			       challenge.getHost() + "\", " +
			       challenge.getPort() + ", \"" +
			       challenge.getRealm() + "\", " +
			       "<username>, <password>);");
	else if (challenge.getScheme().equalsIgnoreCase("Digest"))
	    System.out.println("    AuthorizationInfo.addDigestAuthorization(\"" +
			       challenge.getHost() + "\", " +
			       challenge.getPort() + ", \"" +
			       challenge.getRealm() + "\", " +
			       "<username>, <password>);");
	else
	    System.out.println("    AuthorizationInfo.addAuthorization(\"" +
			       challenge.getHost() + "\", " +
			       challenge.getPort() + ", \"" +
			       challenge.getScheme() + "\", \"" +
			       challenge.getRealm() + "\", " +
			       "...);");
	System.out.println();

	return null;
    }
}

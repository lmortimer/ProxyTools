
/*
 * This is a very simple example of how to use the HTTPClient package in an
 * Applet. It just POSTs a request to a cgi-script on the server when you
 * hit the 'Doit' button, and then displays the returned headers and data in
 * a text window.
 */

package HTTPClient.doc;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import HTTPClient.HTTPResponse;
import HTTPClient.HTTPConnection;

public class HTTPClientExample extends Applet implements Runnable, ActionListener
{
    private HTTPConnection con;
    private HTTPResponse   rsp = null;
    private String         script = "/cgi-bin/my_script.cgi";

    private String         disp = "";
    private Thread         thread = null;
    private boolean        done = false;
    private TextArea       text;


    public void init()
    {
	/* setup a text area and a button */

        setLayout(new BorderLayout());

	add("Center", text = new TextArea(60, 60));
	text.setEditable(false);

	Button doit = new Button("Doit");
	doit.addActionListener(this);
	add("South", doit);
 

	/* get an HTTPConnection */

        try
        {
	    con = new HTTPConnection(getCodeBase());
        }
        catch (Exception e)
        {
            disp = "Error creating HTTPConnection:\n" + e;
            repaint();
            return;
        }
    }

    public void start()
    {
	/* run the http request in a separate thread */

	if (thread == null)
	{
	    done   = false;
	    thread = new Thread(this);
	    thread.start();
	}
    }

    public void run()
    {
	try
	{
	    while (true)
	    {
		/* wait for the button to be pressed */

		waitForDoit();
		if (done)  break;

		/* POST something to the script */

		disp = "POSTing ...";
		repaint();
		rsp = con.Post(script, "Hello World again");
		repaint();
	    }
	}
	catch (Exception e)
	{
	    disp = "Error POSTing: " + e;
	    e.printStackTrace();
	    repaint();
	}
    }

    
    private synchronized void waitForDoit()
    {
	try { wait(); } catch (InterruptedException ie) { }
    }

    private synchronized void notifyDoit()
    {
	notify();
    }


    public void stop()
    {
	if (thread != null)
	{
	    done   = true;
	    notifyDoit();
	    thread = null;
	}
    }


    public void actionPerformed(ActionEvent evt)
    {
	notifyDoit();	// tell request thread to do the request
    }


    public void paint(Graphics g)
    {
	text.setText(disp + "\n");

	if (rsp == null) return;

	try
	{
	    text.append("\n---Headers:\n" + rsp.toString());
	    text.append("\n---Data:\n" + rsp.getText() + "\n");
	}
	catch (Exception e)
	{
	    text.append("\n---Got Exception:\n" + e + "\n");
	}
    }
}

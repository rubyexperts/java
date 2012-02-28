import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.xml.namespace.QName;

import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfCategory;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.TNWebServiceStringInputs;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.TNWebServiceStringInputsSoap;


public class Test {
public static void main(String[] args) throws MalformedURLException {
	System.out.println("started");
	
	URL url = new URL("http://tnwebservices.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL");
	
	Authenticator.setDefault( new Authenticator() {
		
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO Auto-generated method stub
			return new PasswordAuthentication("test_user","test_user".toCharArray());
		}
	});
	
	
	TNWebServiceStringInputs tns = new TNWebServiceStringInputs(url,  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
    TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();
    ArrayOfCategory array_of_categories = tnss.getCategoriesMasterList("568");
    System.out.println("completed");
}
}

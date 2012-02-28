import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.Authenticator;
import java.net.URL;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.namespace.QName;

import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfCategory;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfEvent;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfPerformer;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfPerformerPercent;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.ArrayOfVenue;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.Category;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.Event;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.Performer;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.PerformerPercent;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.TNWebServiceStringInputs;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.TNWebServiceStringInputsSoap;
import com.ticketnetwork.tnwebservices.tnwebservice.v3.Venue;

public class jdata3 {
        static boolean DEBUG=false;
        static String DB_ENGINE=" "+"ENGINE=MyISAM";
        //can only repair MyISAM and Archive tables in mySQL
        static boolean repair_tables = true;


        public static void main(String[] args) throws Exception {
        int numberOfPerformersReturned=0; //used to determine how many high sales performers to ask for
        Category[] Category_array = null;  //used to store categories to update cat table and event table
        boolean gotException = false;
        boolean incremental_update=true;
        ByteArrayOutputStream email = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(email);
        PrintStream log = null;      
        String smtp_server = "";
        String smtp_port = "25";
        String email_from = "";
        String email_to = "";
        String smtpUsername = "";
        String smtpPassword = "";
        String useSMTPAuth = "false";
        String jdata_props_filename = "jdata3.ini";
        String beginDate="";
        String endDate="";
        int SPORTS_CAT_ID=1;
        int oldEventsLimit = 0;
        String daysToDoFullRefresh="";
        boolean useOldEventsLimit=false;
        boolean useFutureEventsLimit=false;
        int futureEventsLimit = 0;
        String url_filename_space=" ";
        String url_dirname_space=" ";
        boolean useLowerCaseDirnames=true;
        boolean useLowerCaseFilenames=true;
        boolean sendMail = false;
       
        
        
        
        try {
            Properties team_name_mappings = new Properties();
            Properties country_mappings = new Properties();
            Properties city_mappings = new Properties();
            Properties state_province_mappings = new Properties();
            Properties props = new Properties();
            try{
                props.load(jdata3.class.getResourceAsStream(jdata_props_filename));
            }catch(Exception e){
                System.out.println("Error while jdata ini file: " + e);
                return;
            }
            String[] databases = delimitedStringToArray(props.getProperty("DatabaseFiles"), ",");
            String logfile_name = props.getProperty("logfile");
            sendMail = Boolean.valueOf(props.getProperty("sendMail"));
            smtp_server = props.getProperty("smtp_server");
            smtp_port = (props.getProperty("smtp_port")).trim();
            email_from = props.getProperty("email_from");
            email_to = props.getProperty("email_to");
            useSMTPAuth = props.getProperty("useSMTPAuth");
            smtpUsername = props.getProperty("smtpUsername");
            smtpPassword = props.getProperty("smtpPassword");
            boolean appendLog = Boolean.valueOf(props.getProperty("appendLog"));
            useOldEventsLimit = Boolean.valueOf(props.getProperty("useOldEventsLimit"));
            useFutureEventsLimit = Boolean.valueOf(props.getProperty("useFutureEventsLimit"));
            beginDate=props.getProperty("beginDate");
            endDate=props.getProperty("endDate");
            boolean restoreFromBackup = Boolean.valueOf(props.getProperty("restoreFromBackup"));
            useLowerCaseDirnames=Boolean.valueOf(props.getProperty("useLowerCaseDirnames"));
            useLowerCaseFilenames=Boolean.valueOf(props.getProperty("useLowerCaseFilenames"));
            SPORTS_CAT_ID=Integer.parseInt(props.getProperty("SPORTS_CAT_ID"));
            log = new PrintStream(new FileOutputStream(logfile_name, appendLog));
            
            try{
                city_mappings.load(jdata3.class.getResourceAsStream("city_mappings.ini"));
                state_province_mappings.load(jdata3.class.getResourceAsStream("state_province_mappings.ini"));
                country_mappings.load(jdata3.class.getResourceAsStream("country_mappings.ini"));
                team_name_mappings.load(jdata3.class.getResourceAsStream("team_name_mappings.ini"));
            }catch(Exception e){
                gotException = true;
                System.out.println("Error while opening string mappings files: " + e);
                out.println("Error while opening string mappings files: " + e);
                log.println("Error while opening string mappings files: " + e);
            }
          
            daysToDoFullRefresh=props.getProperty("daysToDoFullRefresh");      
            Calendar calendar_for_today = new GregorianCalendar();
            String days[] =  delimitedStringToArray(daysToDoFullRefresh,",");
            int dd = 0;
            while(dd < days.length){
                if ( Integer.parseInt(days[dd]) == calendar_for_today.get(calendar_for_today.DAY_OF_MONTH)){
                    incremental_update=false;
                }
                dd++;
            }
                       
            oldEventsLimit=Integer.parseInt(props.getProperty("oldEventsLimit"));
            futureEventsLimit=Integer.parseInt(props.getProperty("futureEventsLimit"));
            url_filename_space = props.getProperty("url_filename_space");
            url_dirname_space = props.getProperty("url_dirname_space");
            
            beginDate = props.getProperty("beginDate");
            if (beginDate != null) {

                            if (beginDate.compareToIgnoreCase("today") == 0) {
                                try {
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    java.util.Date date = new java.util.Date();
                                    String datetime = dateFormat.format(date);
                                    //System.out.println("Today : " + datetime);
                                    beginDate = datetime;
                                    props.setProperty("beginDate", beginDate);

                                } catch (Exception e) {
                                    gotException = true;                                    
                                    System.out.println("something went wrong in processing BeginDate: " + e);
                                    out.println("something went wrong in processing BeginDate: " + e);
                                    log.println("something went wrong in processing BeginDate: " + e);

                                }
                            }
            }
            if(useOldEventsLimit){
                                try {
                                
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    java.util.Date date = new java.util.Date();                               
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(date);
                                    calendar.add(Calendar.DAY_OF_MONTH,-1*oldEventsLimit);
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                    beginDate = sdf.format(calendar.getTime());
                                    props.setProperty("beginDate", beginDate);
                                } catch (Exception e) {
                                    gotException = true;                                   
                                    System.out.println("something went wrong in processing BeginDate in useOldEvents: " + e);
                                    out.println("something went wrong in processing BeginDate in useOldEvents: " + e);
                                    log.println("something went wrong in processing BeginDate in useOldEvents: " + e);

                                }
                             //System.out.println("Get Old Events BeginDate: " + props.getProperty("beginDate"));
            }
             
            
            if (useFutureEventsLimit) {
                            
                                                  
                                try {
                                
                                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                    java.util.Date date = new java.util.Date();                               
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.setTime(date);
                                    calendar.add(Calendar.DAY_OF_MONTH,futureEventsLimit);
                                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                    endDate = sdf.format(calendar.getTime());
                                    props.setProperty("endDate",endDate);
                                 } catch (Exception e) {
                                    gotException = true;
                                   
                                    System.out.println("something went wrong in processing EndDate in useFutureEvents: " + e);
                                    out.println("something went wrong in processing EndDate in useFutureEvents: " + e);
                                    log.println("something went wrong in processing EndDate in useFutureEvents: " + e);

                                }
                             //System.out.println("Get Future Events EndDate: " + props.getProperty("endDate"));
                            
             }
            
            
            
            
            
            out.println("############################################################################");
            out.println("--------------------------------------------------------------------");
            out.println("-----Begin-JData-Run------------------------------------------------");
            out.println("Jdata Global Settings");
            out.println("Events BeginDate: " + props.getProperty("beginDate"));
            out.println("Events EndDate: " + props.getProperty("endDate"));
            out.println("useFutureEventsLimit: " + props.getProperty("useFutureEventsLimit"));
            out.println("useOldEventsLimit: " + props.getProperty("useOldEventsLimit"));
            out.println("futureEventsLimit: " + props.getProperty("futureEventsLimit"));
            out.println("oldEventsLimit: " + props.getProperty("oldEventsLimit"));
            out.println("daysToDoFullRefresh: " + props.getProperty("daysToDoFullRefresh"));
            out.println("Incremental Refresh: " + incremental_update);          
            out.println("restoreFromBackup: " + restoreFromBackup);
            out.println("SPORTS_CAT_ID: " + SPORTS_CAT_ID);
            out.println("url_filename_space: " + url_filename_space);
            out.println("url_dirname_space: " + url_dirname_space);
            out.println("useLowerCaseDirnames: " + useLowerCaseDirnames);
            out.println("useLowerCaseFilenames: " + useLowerCaseFilenames);
            out.println("sendMail: " + sendMail);
            if (sendMail) {
                out.println("smtp_server: " + smtp_server);
                out.println("smtp_port: " + smtp_port);
                out.println("email_to: " + email_to);
                out.println("email_from: " + email_from);
                out.println("useSMTPAuth: " + useSMTPAuth);
                out.println("SMTP username: " + smtpUsername);
            }
            out.println("logfile: " + logfile_name);
            out.println("appendLog: " + appendLog);
            out.println("DatabaseFiles: " + props.getProperty("DatabaseFiles"));
            out.println("-----Beginning updates and loop over databases at: " + new java.util.Date() + "-");
                        
            System.out.println("############################################################################");                  
            System.out.println("--------------------------------------------------------------------");
            System.out.println("-----Begin-JData-Run------------------------------------------------");
            System.out.println("Jdata Global Settings");
            System.out.println("Events BeginDate: " + props.getProperty("beginDate"));
            System.out.println("Events EndDate: " + props.getProperty("endDate"));
            System.out.println("useFutureEventsLimit: " + props.getProperty("useFutureEventsLimit"));
            System.out.println("useOldEventsLimit: " + props.getProperty("useOldEventsLimit"));
            System.out.println("futureEventsLimit: " + props.getProperty("futureEventsLimit"));
            System.out.println("oldEventsLimit: " + props.getProperty("oldEventsLimit"));
            System.out.println("daysToDoFullRefresh: " + props.getProperty("daysToDoFullRefresh"));
            System.out.println("Incremental Refresh: " + incremental_update);
            System.out.println("restoreFromBackup: " + restoreFromBackup);
            System.out.println("SPORTS_CAT_ID: " + SPORTS_CAT_ID);
            System.out.println("url_filename_space: " + url_filename_space);
            System.out.println("url_dirname_space: " + url_dirname_space);
            System.out.println("useLowerCaseDirnames: " + useLowerCaseDirnames);
            System.out.println("useLowerCaseFilenames: " + useLowerCaseFilenames);
            System.out.println("sendMail: " + sendMail);
            if (sendMail) {
                System.out.println("smtp_server: " + smtp_server);
                System.out.println("smtp_port: " + smtp_port);
                System.out.println("email_to: " + email_to);
                System.out.println("email_from: " + email_from);
                System.out.println("useSMTPAuth: " + useSMTPAuth);
                System.out.println("SMTP username: " + smtpUsername);
            }
            System.out.println("logfile: " + logfile_name);
            System.out.println("appendLog: " + appendLog);
            System.out.println("DatabaseFiles: " + props.getProperty("DatabaseFiles"));
            System.out.println("-----Beginning updates and loop over databases at: " + new java.util.Date() + "-");
            
            log.println("############################################################################");
            log.println("--------------------------------------------------------------------");
            log.println("-----Begin-JData-Run------------------------------------------------");
            log.println("Jdata Global Settings");
            log.println("Events BeginDate: " + props.getProperty("beginDate"));
            log.println("Events EndDate: " + props.getProperty("endDate"));
            log.println("useFutureEventsLimit: " + props.getProperty("useFutureEventsLimit"));
            log.println("useOldEventsLimit: " + props.getProperty("useOldEventsLimit"));
            log.println("futureEventsLimit: " + props.getProperty("futureEventsLimit"));
            log.println("oldEventsLimit: " + props.getProperty("oldEventsLimit"));
            log.println("daysToDoFullRefresh: " + props.getProperty("daysToDoFullRefresh"));
            log.println("Incremental Refresh: " + incremental_update);
            log.println("restoreFromBackup: " + restoreFromBackup);
            log.println("SPORTS_CAT_ID: " + SPORTS_CAT_ID);
            log.println("url_filename_space: " + url_filename_space);
            log.println("url_dirname_space: " + url_dirname_space);
            log.println("useLowerCaseDirnames: " + useLowerCaseDirnames);
            log.println("useLowerCaseFilenames: " + useLowerCaseFilenames);
            log.println("sendMail: " + sendMail);
            if (sendMail) {
                log.println("smtp_server: " + smtp_server);
                log.println("smtp_port: " + smtp_port);
                log.println("email_to: " + email_to);
                log.println("email_from: " + email_from);
                log.println("useSMTPAuth: " + useSMTPAuth);
                log.println("SMTP username: " + smtpUsername);
            }
            log.println("logfile: " + logfile_name);
            log.println("appendLog: " + appendLog);
            log.println("DatabaseFiles: " + props.getProperty("DatabaseFiles"));      
            log.println("-----Beginning updates and loop over databases at: " + new java.util.Date() + "-");
            
            
            
            
            String db_conn_strings[] = new String[databases.length];
            int num_dbs = databases.length;
            int k = 0;
            //loop over each db in list
            while (k < num_dbs) {
                Properties db_props = new Properties();
                out.println("Reading properties for DB " + (k + 1));
                log.println("Reading properties for DB " + (k + 1));
                System.out.println("Reading properties for DB " + (k + 1));

                db_props.load(jdata3.class.getResourceAsStream(databases[k]));
                try {

                   
                    String driver = db_props.getProperty("driver");
                    String option = db_props.getProperty("option");
                    String server = db_props.getProperty("server");
                    String db = db_props.getProperty("db");
                    String dbport = db_props.getProperty("dbport");
                    String username = db_props.getProperty("username");
                    String password = db_props.getProperty("password");
                    String useCompression = db_props.getProperty("useCompression");
                    out.println("driver: " + driver);
                    out.println("server: " + server);
                    out.println("port: " + dbport);
                    out.println("db: " + db);
                    out.println("option: " + option);
                    out.println("username: " + username);
                    out.println("useCompression: " + useCompression);

                    System.out.println("driver: " + driver);
                    System.out.println("server: " + server);
                    System.out.println("port: " + dbport);
                    System.out.println("db: " + db);
                    System.out.println("option: " + option);
                    System.out.println("username: " + username);
                    System.out.println("useCompression: " + useCompression);

                    log.println("driver: " + driver);
                    log.println("server: " + server);
                    log.println("port: " + dbport);
                    log.println("db: " + db);
                    log.println("option: " + option);
                    log.println("username: " + username);
                    log.println("useCompression: " + useCompression);
              
                    String connection_string = "jdbc:" + driver + "://" + server + ":" + dbport + "/" + db + "?user=" + username + "&password="+password+ "&useCompression="+useCompression+"&Option=" + option;
                    db_conn_strings[k] = connection_string;
                    String printable_connection_string = "jdbc:" + driver + "://" + server + ":" + dbport + "/" + db + "?user=" + username + "&password=********"+ "&useCompression="+useCompression+"&Option=" + option;

                    out.println("Database Connection String for database " +(k+1)+ ": "+printable_connection_string );
                    System.out.println("Database Connection String for database " +(k+1)+ ": "+connection_string );
                    log.println("Database Connection String for database " +(k+1)+ ": "+printable_connection_string );
                  




                } catch (Exception e) {
                    gotException = true;
                    System.out.println("Error while configuring database: " + (k + 1) + " exception: " + e);
                    out.println("Error while configuring database: " + (k + 1) + " exception: " + e);
                    log.println("Error while configuring database: " + (k + 1) + " exception: " + e);
                }
                
                out.println("Configuration of database: " + (k + 1) + " complete" + "----------------------------");
                log.println("Configuration of database: " + (k + 1) + " complete" + "----------------------------");
                System.out.println("Configuration of database: " + (k + 1) + " complete" + "----------------------------");
                k++;
            }//end of loop over dbs


            out.println("---Completed-configuration-loop-over-databases-at: " + new java.util.Date() + "-");
            System.out.println("---Completed-configuration-loop-over-databases-at: " + new java.util.Date() + "-");
            log.println("---Completed-configuration-loop-over-databases-at: " + new java.util.Date() + "-");


    
           
            if (restoreFromBackup) {  //restore from backup

                out.println("Beginning restore process");
                System.out.println("Beginning restore process");
                log.println("Beginning restore process");
                
                k=0;
                while(k < num_dbs){
                    out.println("Restoring database: "+(k+1));
                    System.out.println("Restoring database: "+(k+1));
                    log.println("Restoring database: "+(k+1));
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{
                           
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            if(repair_tables){
                                stmt.executeUpdate("REPAIR TABLE backup_categories extended");
                                stmt.executeUpdate("REPAIR TABLE backup_venues extended");
                                stmt.executeUpdate("REPAIR TABLE backup_events extended");
                                stmt.executeUpdate("REPAIR TABLE backup_performers extended");
                                stmt.executeUpdate("REPAIR TABLE backup_sales extended");
                                stmt.executeUpdate("REPAIR TABLE backup_geo extended");
                                stmt.executeUpdate("REPAIR TABLE backup_metadata extended");
                            }
                            stmt.executeUpdate("DROP TABLE IF EXISTS categories");
                            stmt.executeUpdate(create_categories_table);
                            stmt.executeUpdate("insert into categories select * from backup_categories");
                            stmt.executeUpdate("DROP TABLE IF EXISTS venues");
                            stmt.executeUpdate(create_venues_table);
                            stmt.executeUpdate("insert into venues select * from backup_venues");
                            stmt.executeUpdate("DROP TABLE IF EXISTS events");
                            stmt.executeUpdate(create_events_table);
                            stmt.executeUpdate("insert into events select * from backup_events");
                            stmt.executeUpdate("DROP TABLE IF EXISTS performers");
                            stmt.executeUpdate(create_performers_table);
                            stmt.executeUpdate("insert into performers select * from backup_performers");
                            stmt.executeUpdate("DROP TABLE IF EXISTS sales");
                            stmt.executeUpdate(create_sales_table);
                            stmt.executeUpdate("insert into sales select * from backup_sales");
                            stmt.executeUpdate("DROP TABLE IF EXISTS geo");
                            stmt.executeUpdate(create_geo_table);
                            stmt.executeUpdate("insert into geo select * from backup_geo");
                            stmt.executeUpdate("DROP TABLE IF EXISTS metadata");
                            stmt.executeUpdate(create_metadata_table);
                            stmt.executeUpdate("insert into metadata select * from backup_metadata");
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempcategories");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempvenues");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempevents");                            
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempperformers");                          
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempsales");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempmetadata");
                            stmt.executeUpdate("create index venues_name_index using hash on venues (Name(120))");
                            stmt.executeUpdate("create index venues_city_index using hash on venues (City(120))");
                            stmt.executeUpdate("create index venues_stateprovince_index using hash on venues (StateProvince(120))");
                            stmt.executeUpdate("create index venues_location_index using hash on venues (location(120))");
                            stmt.executeUpdate("create index venues_abbrvstateprovince_index using hash on venues (AbbrvStateProvince(120))");
                            stmt.executeUpdate("create index venues_citystate_index using hash on venues (City(120),StateProvince(120))");
                            stmt.executeUpdate("create index venues_citystatecountry_index using hash on venues (City(120),StateProvince(120),Country(120))");
                            stmt.executeUpdate("create index venues_cityabbrvstate_index using hash on venues (City(120),AbbrvStateProvince(120))");
                            stmt.executeUpdate("create index venues_country_index using hash on venues (Country(120))");
                            stmt.executeUpdate("create index venues_abbrvcountry_index using hash on venues (AbbrvCountry(120))");
                            stmt.executeUpdate("OPTIMIZE TABLE venues");
                            stmt.executeUpdate("create index performers_name_index using hash on performers (Name(120))");
                            stmt.executeUpdate("create index performers_id_index on performers (ID)");
                            stmt.executeUpdate("create index performers_parent_index using hash on performers (ParentCategoryDescription(120))");
                            stmt.executeUpdate("create index performers_parentid_index on performers (ParentCategoryID)");
                            stmt.executeUpdate("create index performers_parentchild_index using hash on performers (ParentCategoryDescription(120),ChildCategoryDescription(120))");
                            stmt.executeUpdate("create index performers_parentchildid_index on performers (ParentCategoryID,ChildCategoryID)");
                            stmt.executeUpdate("create index performers_parentchildgrandchild_index using hash on performers (ParentCategoryDescription(120),ChildCategoryDescription(120),GrandchildCategoryDescription(120))");
                            stmt.executeUpdate("create index performers_parentchildgrandchildid_index on performers (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                            stmt.executeUpdate("OPTIMIZE TABLE performers");
                            stmt.executeUpdate("create index sales_name_index using hash on sales (Name(120))");
                            stmt.executeUpdate("create index sales_id_index on sales (ID)");
                            stmt.executeUpdate("create index sales_parent_index using hash on sales (ParentCategoryDescription(120))");
                            stmt.executeUpdate("create index sales_parentid_index on sales (ParentCategoryID)");
                            stmt.executeUpdate("create index sales_parentchild_index using hash on sales (ParentCategoryDescription(120),ChildCategoryDescription(120))");
                            stmt.executeUpdate("create index sales_parentchildid_index on sales (ParentCategoryID,ChildCategoryID)");
                            stmt.executeUpdate("create index sales_parentchildgrandchild_index using hash on sales (ParentCategoryDescription(120),ChildCategoryDescription(120),GrandchildCategoryDescription(120))");
                            stmt.executeUpdate("create index sales_parentchildgrandchildid_index on sales (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                            stmt.executeUpdate("create index sales_percent_index on sales (percent)");
                            stmt.executeUpdate("OPTIMIZE TABLE sales");
                            stmt.executeUpdate("create index events_name_index using hash on events (Name(120))");
                            stmt.executeUpdate("create index events_parentid_index on events (ParentCategoryID)");
                            stmt.executeUpdate("create index events_parentchildid_index on events (ParentCategoryID,ChildCategoryID)");
                            stmt.executeUpdate("create index events_parentchildgrandchildid_index on events (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                            stmt.executeUpdate("create index events_venuename_index using hash on events (Venue(120))");
                            stmt.executeUpdate("create index events_city_index using hash on events (City(120))");
                            stmt.executeUpdate("create index events_stateprovince_index using hash on events (LongStateProvince(120))");
                            stmt.executeUpdate("create index events_location_index using hash on events (location(120))");
                            stmt.executeUpdate("create index events_abbrvstateprovince_index using hash on events (StateProvince(120))");
                            stmt.executeUpdate("create index events_citystate_index using hash on events (City(120),LongStateProvince(120))");
                            stmt.executeUpdate("create index events_cityabbrvstate_index using hash on events (City(120),StateProvince(120))");
                            stmt.executeUpdate("create index events_country_index using hash on events (Country(120))");
                            stmt.executeUpdate("create index events_abbrvcountry_index using hash on events (AbbrvCountry(120))");
                            stmt.executeUpdate("create index events_venueid_index on events (VenueID)");
                            stmt.executeUpdate("create index events_altname_index using hash on events (AlternateName(120))");
                            stmt.executeUpdate("create index events_performer1_index using hash on events (Performer1(120))");
                            stmt.executeUpdate("create index events_performer2_index using hash on events (Performer2(120))");
                            stmt.executeUpdate("create index events_performer12_index using hash on events (Performer1(120),Performer2(120))");
                            stmt.executeUpdate("OPTIMIZE TABLE events");
                            stmt.executeUpdate("create index geo_city_index using hash on geo (City(120))");
                            stmt.executeUpdate("create index geo_stateprovince_index using hash on geo (StateProvince(120))");
                            stmt.executeUpdate("create index geo_location_index using hash on geo (location(120))");
                            stmt.executeUpdate("create index geo_abbrvstateprovince_index using hash on geo (AbbrvStateProvince(120))");
                            stmt.executeUpdate("create index geo_citystate_index using hash on geo (City(120),StateProvince(120))");
                            stmt.executeUpdate("create index geo_cityabbrvstate_index using hash on geo (City(120),AbbrvStateProvince(120))");
                            stmt.executeUpdate("create index geo_country_index using hash on geo (Country(120))");
                            stmt.executeUpdate("create index geo_abbrvcountry_index using hash on geo (AbbrvCountry(120))");
                            stmt.executeUpdate("OPTIMIZE TABLE geo");
                            conn.commit();
                            out.println("Restore complete for database: "+(k+1));
                            System.out.println("Restore complete for database: "+(k+1));
                            log.println("Restore complete for database: "+(k+1));
                            
                         
                      }catch (Exception e) {
                                System.out.println("An error occured while restoring database " + (k+1) + " from backup: " + e);
                                out.println("An error occured while restoring database " + (k+1) + " from backup: " + e);
                                log.println("An error occured while restoring database " + (k+1) + " from backup: " + e);
                                gotException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error while restoring database " + (k+1) + " from backup: " + e);
                                out.println("Error while restoring database " + (k+1) + " from backup: " + e);
                                log.println("Error while restoring database " + (k+1) + " from backup: " + e);
                                gotException = true;
                              
                                    
                     }
                    
                    
                     k++;
            }
           

                   
       }else {  //begin regular update
            out.println("Beginning update process");
            System.out.println("Beginning update process");
            log.println("Beginning update process");
            String update_date = "" + new java.util.Date() + "";   
            
             k=0;
             while(k < num_dbs){ 
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{                         
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempcategories");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempvenues");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempevents");                            
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempperformers");                          
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempsales");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");                           
                            stmt.executeUpdate("DROP TABLE IF EXISTS tempmetadata");                
                            stmt.executeUpdate(create_categories_table);
                            stmt.executeUpdate(create_venues_table);                 
                            stmt.executeUpdate(create_events_table);                          
                            stmt.executeUpdate(create_performers_table);                           
                            stmt.executeUpdate(create_sales_table);              
                            stmt.executeUpdate(create_geo_table);                         
                            stmt.executeUpdate(create_metadata_table);
                            stmt.executeUpdate(create_backup_categories_table);
                            stmt.executeUpdate(create_backup_venues_table);                 
                            stmt.executeUpdate(create_backup_events_table);                          
                            stmt.executeUpdate(create_backup_performers_table);                           
                            stmt.executeUpdate(create_backup_sales_table);              
                            stmt.executeUpdate(create_backup_geo_table);                         
                            stmt.executeUpdate(create_backup_metadata_table); 
                            stmt.executeUpdate(create_temp_metadata_table);
                            if(repair_tables){
                                stmt.executeUpdate("REPAIR TABLE categories extended");
                                stmt.executeUpdate("REPAIR TABLE venues extended");
                                stmt.executeUpdate("REPAIR TABLE events extended");
                                stmt.executeUpdate("REPAIR TABLE performers extended");
                                stmt.executeUpdate("REPAIR TABLE sales extended");
                                stmt.executeUpdate("REPAIR TABLE geo extended");
                                stmt.executeUpdate("REPAIR TABLE metadata extended");
                                stmt.executeUpdate("REPAIR TABLE backup_categories extended");
                                stmt.executeUpdate("REPAIR TABLE backup_venues extended");
                                stmt.executeUpdate("REPAIR TABLE backup_events extended");
                                stmt.executeUpdate("REPAIR TABLE backup_performers extended");
                                stmt.executeUpdate("REPAIR TABLE backup_sales extended");
                                stmt.executeUpdate("REPAIR TABLE backup_geo extended");
                                stmt.executeUpdate("REPAIR TABLE backup_metadata extended");
                            }
                            stmt.executeUpdate("insert into tempmetadata select * from metadata");
                            conn.commit();
                           
                            
                         
                      }catch (Exception e) {
                                System.out.println("An error occured for database " + (k+1) + ": " + e);
                                out.println("An error occured for database " + (k+1) + ": " + e);
                                log.println("An error occured for database " + (k+1) + ": " + e);
                                gotException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            }
             
             
            //update categories
             System.out.println("Updating categories");
             out.println("Updating categories");
             log.println("Updating categories");
             boolean getCatException = false;
             
             update_date = "" + new java.util.Date() + "";
             try{
                        String websiteConfigIDString = props.getProperty("websiteConfigID");
                        String useProxy = props.getProperty("useProxy");
                        String proxyHost = props.getProperty("proxyHost");
                        String proxyPort = props.getProperty("proxyPort");
                        if (Boolean.valueOf(useProxy)) {
                            Properties systemSettings = System.getProperties();
                            systemSettings.put("http.proxyHost", proxyHost);
                            systemSettings.put("http.proxyPort", proxyPort);
                            System.setProperties(systemSettings);

                        }
                        
                        Authenticator.setDefault( new Authenticator() {
                    		
                    		@Override
                    		protected java.net.PasswordAuthentication getPasswordAuthentication() {
                    			// TODO Auto-generated method stub
                    			return new java.net.PasswordAuthentication("test_user","test_user".toCharArray());
                    		}
                    	});

                        TNWebServiceStringInputs tns = new TNWebServiceStringInputs(new URL("http://tnwebservices-test.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL"),  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
                        TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();
                        ArrayOfCategory array_of_categories = tnss.getCategoriesMasterList(websiteConfigIDString);
                        if (array_of_categories == null) {
                                gotException = true;
                                getCatException = true;
                                throw new Exception("Error - no Categories found");

                        }
                        java.util.List<Category> Category_list = array_of_categories.getCategory();
                        Category_array = Category_list.toArray(new Category[0]);
                    
                      
                        if (Category_array.length == 0) {
                            gotException = true;
                            getCatException = true;
                            throw new Exception("Error - no Categories found");

                        }

                        
                 
                 
             }catch(Exception e){
                        System.out.println("Exception while downloading category data: " + e);
                        out.println("Exception while downloading category data: " + e);
                        log.println("Exception while downloading category data: " + e);
                        gotException = true;
                        getCatException = true;
             }
             
             k=0;

             while((k < num_dbs) && (!getCatException)){ 
                    try{
                        Connection conn = null;
                        PreparedStatement psi = null;
                        Statement stmt = null;
                       
                                 try{

                                        conn = getConnectionFromString(db_conn_strings[k]);
                                        conn.setAutoCommit(false);
                                        stmt = conn.createStatement();
                                       
                                                                    
                                        if(incremental_update){
                                             stmt.executeUpdate("DROP TABLE IF EXISTS backup_categories"); 
                                             stmt.executeUpdate(create_backup_categories_table);
                                             stmt.executeUpdate("insert into backup_categories select * from categories");
                                             psi = conn.prepareStatement("INSERT INTO categories (ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription) VALUES (?,?,?,?,?,?)ON DUPLICATE KEY UPDATE ParentCategoryDescription=VALUES(ParentCategoryDescription),ChildCategoryDescription=VALUES(ChildCategoryDescription),GrandchildCategoryDescription=VALUES(GrandchildCategoryDescription)");
                                       
                                        }else{
                                            stmt.executeUpdate("DROP TABLE IF EXISTS tempcategories");
                                            stmt.executeUpdate(create_temp_categories_table);                                      
                                            psi = conn.prepareStatement("INSERT INTO tempcategories (ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription) VALUES (?,?,?,?,?,?)");
                                            
                                        }
                                         
                                        
                                        
                                        int rows = Category_array.length;
                                        int i = 0;
                                        int inserts = 0;
                                        int updates = 0;
                                        int rows_affected = 0;
                                        while (i < rows) {
                                                String parent = Category_array[i].getParentCategoryDescription();
                                                String child = Category_array[i].getChildCategoryDescription();
                                                String grand_child = Category_array[i].getGrandchildCategoryDescription();
                                                int parentID = Category_array[i].getParentCategoryID();
                                                int childID = Category_array[i].getChildCategoryID();
                                                int grand_childID = Category_array[i].getGrandchildCategoryID();

                                                psi.setInt(1, parentID);
                                                psi.setInt(2, childID);
                                                psi.setInt(3, grand_childID);
                                                psi.setString(4, parent);
                                                psi.setString(5, child);
                                                psi.setString(6, grand_child);
                                                
                                                rows_affected = psi.executeUpdate();
                                                if(rows_affected != 1) {
                                                    updates++;
                                                }else{
                                                    inserts++;
                                                            
                                                }                                              
                                                i++;
                                        }
                                    if(incremental_update){
                                        
                                    }else{
                                       stmt.executeUpdate("DROP TABLE IF EXISTS backup_categories");
                                       stmt.executeUpdate("RENAME TABLE categories TO backup_categories, tempcategories TO categories;");          
                                    }
                                        
                                        
                                    stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate, NumberofEntries) VALUES ('categories', '"+ update_date +"', '"+inserts+"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"',NumberofEntries= '"+inserts+"'");
                                    conn.commit();
                                    System.out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    log.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");

                                  }catch (Exception e) {
                                            System.out.println("An error occured for database " + (k+1) + ": " + e);
                                            out.println("An error occured for database " + (k+1) + ": " + e);
                                            log.println("An error occured for database " + (k+1) + ": " + e);
                                            gotException = true;
                                            getCatException = true;
                                            conn.rollback();

                                 }finally{
                                   
                                     stmt.close();
                                     conn.close();
                                    
                                 }
                                 
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            }
            
             //we keep cataegory array for use in event updates.
             
             System.runFinalization();
                
            //updating venues
              
             System.out.println("Updating venues");
             out.println("Updating venues");
             log.println("Updating venues");
             boolean getVenuesException = false;
             Venue[] venue_array = null;
             update_date = "" + new java.util.Date() + "";
             try{
                        String venueID = "";
                        String websiteConfigIDString = props.getProperty("websiteConfigID");
                        String useProxy = props.getProperty("useProxy");
                        String proxyHost = props.getProperty("proxyHost");
                        String proxyPort = props.getProperty("proxyPort");
                        if (Boolean.valueOf(useProxy)) {
                            Properties systemSettings = System.getProperties();
                            systemSettings.put("http.proxyHost", proxyHost);
                            systemSettings.put("http.proxyPort", proxyPort);
                            System.setProperties(systemSettings);

                        }
                        TNWebServiceStringInputs tns = new TNWebServiceStringInputs(new URL("http://tnwebservices-test.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL"),  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
                        TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();
                        ArrayOfVenue array_of_venues = tnss.getVenue(websiteConfigIDString, venueID);
                            if (array_of_venues == null) {
                                
                                gotException = true;
                                getVenuesException=true;
                                throw new Exception("Error - no Venues found");

                            }
                            java.util.List<Venue> venue_list = array_of_venues.getVenue();
                            venue_array = venue_list.toArray(new Venue[0]);
                           
                       
                        if (venue_array.length == 0) {
                            
                            gotException = true;
                            getVenuesException=true;
                            throw new Exception("Error - no Venues found");

                        }
                        
                       
                        
                 
                 
             }catch(Exception e){
                        System.out.println("Exception while downloading venue data: " + e);
                        out.println("Exception while downloading venue data: " + e);
                        log.println("Exception while downloading venue data: " + e);
                        gotException = true;
                        getVenuesException=true;
             }
             
             k=0;
             while((k < num_dbs) && (!getVenuesException)){ 
                    try{
                        Connection conn = null;
                        PreparedStatement psi = null;
                        Statement stmt = null;
                                 try{

                                        conn = getConnectionFromString(db_conn_strings[k]);
                                        conn.setAutoCommit(false);
                                        stmt = conn.createStatement();
                                             
                                        if(incremental_update){
                                              stmt.executeUpdate("DROP TABLE IF EXISTS backup_venues"); 
                                              stmt.executeUpdate(create_backup_venues_table);
                                              stmt.executeUpdate("insert into backup_venues select * from venues");
                                              psi = conn.prepareStatement("INSERT INTO venues (ID, Name, URL, Notes, Street1, Street2,City,StateProvince,ZipCode,Capacity,Directions,Parking,PublicTransportation,BoxOfficePhone,WillCall,Rules,ChildRules,Country,location,AbbrvCountry,AbbrvStateProvince,URLName,URLParentName,URLChildName,URLGrandChildName,DefaultGeoName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update Name=values(Name), URL=VALUES(URL), Notes=VALUES(Notes), Street1=VALUES(Street1), Street2=VALUES(Street2),City=VALUES(City),StateProvince=VALUES(StateProvince),ZipCode=VALUES(ZipCode),Capacity=VALUES(Capacity),Directions=VALUES(Directions),Parking=VALUES(Parking),PublicTransportation=VALUES(PublicTransportation),BoxOfficePhone=VALUES(BoxOfficePhone),WillCall=VALUES(WillCall),Rules=VALUES(Rules),ChildRules=VALUES(ChildRules),Country=VALUES(Country),location=VALUES(location),AbbrvCountry=VALUES(AbbrvCountry),AbbrvStateProvince=VALUES(AbbrvStateProvince),URLName=VALUES(URLName),URLParentName=VALUES(URLParentName),URLChildName=VALUES(URLChildName),URLGrandChildName=VALUES(URLGrandChildName),DefaultGeoName=Values(DefaultGeoName)");
                                  
                                        }else{
                                              stmt.executeUpdate("DROP TABLE IF EXISTS tempvenues");
                                              stmt.executeUpdate(create_temp_venues_table);                                      
                                              psi = conn.prepareStatement("INSERT INTO tempvenues (ID, Name, URL, Notes, Street1, Street2,City,StateProvince,ZipCode,Capacity,Directions,Parking,PublicTransportation,BoxOfficePhone,WillCall,Rules,ChildRules,Country,location,AbbrvCountry,AbbrvStateProvince,URLName,URLParentName,URLChildName,URLGrandChildName,DefaultGeoName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                       
                                        }
                                         int rows_affected=0;
                                       int rows = venue_array.length;
                                        int i = 0;
                                        int inserts = 0;
                                        int updates = 0;
                    
                            while (i < rows) {


                            String name = venue_array[i].getName();
                            int id = venue_array[i].getID();
                            
                            String city = venue_array[i].getCity();
                            String StateProvince = venue_array[i].getStateProvince();
                            String Country = venue_array[i].getCountry();
                            String URL = venue_array[i].getURL();
                            String notes = venue_array[i].getNotes();
                            String street1 = venue_array[i].getStreet1();
                            String street2 = venue_array[i].getStreet2();
                            String zipcode = venue_array[i].getZipCode();
                            String capacity = "" + venue_array[i].getCapacity();
                            String directions = venue_array[i].getDirections();
                            String parking = venue_array[i].getParking();
                            String publictransportation = venue_array[i].getPublicTransportation();
                            String boxofficephone = venue_array[i].getBoxOfficePhone();
                            String willcall = venue_array[i].getWillCall();
                            String rules = venue_array[i].getRules();
                            String childrules = venue_array[i].getChildRules();
                            //this is to provide a unique mapping 
                            String location = city+" "+StateProvince;
                            location = replaceStringOrMakeNull(city_mappings, location);
                            if(location == null){
                                location = city;
                            }

                            String AbbrvCountry = Country;
                            String AbbrvStateProvince= StateProvince;
                            String DefaultGeoName = key2UrlFilename(location,url_filename_space,useLowerCaseFilenames);
                            
                            AbbrvCountry = replaceStringOrKeepSame(country_mappings,AbbrvCountry);
                            AbbrvStateProvince = replaceStringOrKeepSame(state_province_mappings,AbbrvStateProvince);

                            String URLName=key2UrlFilename(name,url_filename_space,useLowerCaseFilenames);
                            String URLParentName=key2UrlDir(AbbrvCountry,url_dirname_space,useLowerCaseDirnames);
                            String URLChildName=key2UrlDir(StateProvince,url_dirname_space,useLowerCaseDirnames);
                            String URLGrandChildName=key2UrlDir(location,url_dirname_space,useLowerCaseDirnames);
                           
                            psi.setInt(1, id);
                            psi.setString(2, name);
                            psi.setString(3, URL);
                            psi.setString(4, notes);
                            psi.setString(5, street1);
                            psi.setString(6, street2);
                            psi.setString(7, city);
                            psi.setString(8, StateProvince);
                            psi.setString(9, zipcode);
                            psi.setString(10, capacity);
                            psi.setString(11, directions);
                            psi.setString(12, parking);
                            psi.setString(13, publictransportation);
                            psi.setString(14, boxofficephone);
                            psi.setString(15, willcall);
                            psi.setString(16, rules);
                            psi.setString(17, childrules);
                            psi.setString(18, Country);
                            psi.setString(19, location);
                            psi.setString(20, AbbrvCountry);
                            psi.setString(21, AbbrvStateProvince);
                            psi.setString(22, URLName);
                            psi.setString(23, URLParentName);
                            psi.setString(24, URLChildName);
                            psi.setString(25, URLGrandChildName);
                            psi.setString(26, DefaultGeoName);
                            rows_affected = psi.executeUpdate();
                                                if(rows_affected != 1) {
                                                    updates++;
                                                }else{
                                                    inserts++;
                                                            
                                                }               
                            i++;

                        }
                                    
                                        
                                    if(incremental_update){
                                        
                                    }else{
                                        stmt.executeUpdate("DROP TABLE IF EXISTS backup_venues");
                                        stmt.executeUpdate("RENAME TABLE venues TO backup_venues, tempvenues TO venues;");   
                                        stmt.executeUpdate("create index venues_name_index using hash on venues (Name(120))");
                                        stmt.executeUpdate("create index venues_city_index using hash on venues (City(120))");
                                        stmt.executeUpdate("create index venues_stateprovince_index using hash on venues (StateProvince(120))");
                                        stmt.executeUpdate("create index venues_location_index using hash on venues (location(120))");
                                        stmt.executeUpdate("create index venues_abbrvstateprovince_index using hash on venues (AbbrvStateProvince(120))");
                                        stmt.executeUpdate("create index venues_citystate_index using hash on venues (City(120),StateProvince(120))");
                                        stmt.executeUpdate("create index venues_citystatecountry_index using hash on venues (City(120),StateProvince(120),Country(120))");
                                        stmt.executeUpdate("create index venues_cityabbrvstate_index using hash on venues (City(120),AbbrvStateProvince(120))");
                                        stmt.executeUpdate("create index venues_country_index using hash on venues (Country(120))");
                                        stmt.executeUpdate("create index venues_abbrvcountry_index using hash on venues (AbbrvCountry(120))");
                                       
                                    }
                                        
                                    stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate, NumberofEntries) VALUES ('venues', '"+ update_date +"', '"+inserts+"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"',NumberofEntries= '"+inserts+"'");
                                   
                                    stmt.executeUpdate("OPTIMIZE TABLE venues");
                                    conn.commit();




                                    System.out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    log.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");

                                  }catch (Exception e) {
                                            System.out.println("An error occured for database " + (k+1) + ": " + e);
                                            out.println("An error occured for database " + (k+1) + ": " + e);
                                            log.println("An error occured for database " + (k+1) + ": " + e);
                                            gotException = true;
                                            getVenuesException=true;
                                            conn.rollback();

                                 }finally{
                                     
                                     stmt.close();
                                     conn.close();
                                 }
                                 
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            }
            
             venue_array=null;
             System.runFinalization();
         //updating performers
              
             System.out.println("Updating performers");
             out.println("Updating performers");
             log.println("Updating performers");
             boolean getPerformersException = false;
             Performer[] performer_array = null;
             update_date = "" + new java.util.Date() + "";
             try{
                        String websiteConfigIDString = props.getProperty("websiteConfigID");
                        String hasEvent = "";
                        String numReturned = "";
                        String parentCategoryID = "";
                        String childCategoryID = "";
                        String grandchildCategoryID = "";
                        String useProxy = props.getProperty("useProxy");
                        String proxyHost = props.getProperty("proxyHost");
                        String proxyPort = props.getProperty("proxyPort");
                        if (Boolean.valueOf(useProxy)) {
                            Properties systemSettings = System.getProperties();
                            systemSettings.put("http.proxyHost", proxyHost);
                            systemSettings.put("http.proxyPort", proxyPort);
                            System.setProperties(systemSettings);

                        }
                        TNWebServiceStringInputs tns = new TNWebServiceStringInputs(new URL("http://tnwebservices-test.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL"),  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
                        TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();


                            ArrayOfPerformer array_of_performers = tnss.getPerformerByCategory(websiteConfigIDString, parentCategoryID, childCategoryID, grandchildCategoryID, hasEvent);
                            if (array_of_performers == null) {
                                gotException = true;
                                getPerformersException=true;
                                throw new Exception("Error - no Performers found");
                                
                            }

                            java.util.List<Performer> performer_list = array_of_performers.getPerformer();
                            performer_array = performer_list.toArray(new Performer[0]);
                            
                        


                      
                        if (performer_array.length == 0) {
                            gotException = true;
                            getPerformersException=true;
                            throw new Exception("Error - no Performers found");
                        }
                       
                        numberOfPerformersReturned = performer_array.length;
                 
             }catch(Exception e){
                        System.out.println("Exception while downloading performer data: " + e);
                        out.println("Exception while downloading performer data: " + e);
                        log.println("Exception while downloading performer data: " + e);
                        gotException = true;
                        getPerformersException=true;
             }
             
             k=0;
             while((k < num_dbs) && (!getPerformersException)){ 
                    try{
                        Connection conn = null;
                        PreparedStatement psi = null;
                        Statement stmt = null;
                                 try{

                                        conn = getConnectionFromString(db_conn_strings[k]);
                                        conn.setAutoCommit(false);
                                        stmt = conn.createStatement();
                                        
                                        
                                        if(incremental_update){
                                             stmt.executeUpdate("DROP TABLE IF EXISTS backup_performers"); 
                                             stmt.executeUpdate(create_backup_performers_table);
                                             stmt.executeUpdate("insert into backup_performers select * from performers");
                                             psi = conn.prepareStatement("INSERT INTO performers (ID, Name,ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription,URLName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update ParentCategoryDescription=VALUES(ParentCategoryDescription),ChildCategoryDescription=VALUES(ChildCategoryDescription),GrandchildCategoryDescription=VALUES(GrandchildCategoryDescription),URLName=VALUES(URLName),URLParentName=VALUES(URLParentName),URLChildName=VALUES(URLChildName),URLGrandChildName=VALUES(URLGrandChildName)");
                                            
                                        }else{
                                            stmt.executeUpdate("DROP TABLE IF EXISTS tempperformers");
                                            stmt.executeUpdate(create_temp_performers_table);                                      
                                            psi = conn.prepareStatement("INSERT INTO tempperformers (ID, Name,ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription,URLName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
                                              
                                        }
                                         int rows_affected=0;
                                        
                                        
                                        
                                        int rows = performer_array.length;
                                        int i = 0;
                                        int inserts = 0;
                                        int updates = 0;
                         while (i < rows) {
                            String name = performer_array[i].getDescription();
                            int id = performer_array[i].getID();
                            Category cat = performer_array[i].getCategory();
                            String ParentCategoryDescription = "" + cat.getParentCategoryDescription();
                            String ChildCategoryDescription = "" + cat.getChildCategoryDescription();
                            String GrandchildCategoryDescription = "" + cat.getGrandchildCategoryDescription();
                            int parentID = cat.getParentCategoryID();
                            int childID = cat.getChildCategoryID();
                            int grand_childID = cat.getGrandchildCategoryID();

                            String URLName= key2UrlFilename(name, url_filename_space,useLowerCaseFilenames);
                            String URLParentName= key2UrlDir(ParentCategoryDescription,url_dirname_space,useLowerCaseDirnames);
                            String URLChildName=key2UrlDir(ChildCategoryDescription,url_dirname_space,useLowerCaseDirnames);
                            String URLGrandChildName=key2UrlDir(GrandchildCategoryDescription,url_dirname_space,useLowerCaseDirnames);



                            psi.setInt(1, id);
                            psi.setString(2, name);
                            psi.setInt(3, parentID);
                            psi.setInt(4, childID);
                            psi.setInt(5, grand_childID);
                            psi.setString(6, ParentCategoryDescription);
                            psi.setString(7, ChildCategoryDescription);
                            psi.setString(8, GrandchildCategoryDescription);
                            psi.setString(9, URLName);
                            psi.setString(10, URLParentName);
                            psi.setString(11, URLChildName);
                            psi.setString(12, URLGrandChildName);
                            rows_affected = psi.executeUpdate();
                                                if(rows_affected != 1) {
                                                    updates++;
                                                }else{
                                                    inserts++;
                                                            
                                                }        
                          

                            i++;

                     

                        }
                                
                                        
                                    if(incremental_update){
                                               
                                    }else{
                                        stmt.executeUpdate("DROP TABLE IF EXISTS backup_performers");
                                        stmt.executeUpdate("RENAME TABLE performers TO backup_performers, tempperformers TO performers;");
                                        stmt.executeUpdate("create index performers_name_index using hash on performers (Name(120))");
                                        stmt.executeUpdate("create index performers_id_index on performers (ID)");
                                        stmt.executeUpdate("create index performers_parent_index using hash on performers (ParentCategoryDescription(120))");
                                        stmt.executeUpdate("create index performers_parentid_index on performers (ParentCategoryID)");
                                        stmt.executeUpdate("create index performers_parentchild_index using hash on performers (ParentCategoryDescription(120),ChildCategoryDescription(120))");
                                        stmt.executeUpdate("create index performers_parentchildid_index on performers (ParentCategoryID,ChildCategoryID)");
                                        stmt.executeUpdate("create index performers_parentchildgrandchild_index using hash on performers (ParentCategoryDescription(120),ChildCategoryDescription(120),GrandchildCategoryDescription(120))");
                                        stmt.executeUpdate("create index performers_parentchildgrandchildid_index on performers (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                           
                                    }
                                        
                                    stmt.executeUpdate("OPTIMIZE TABLE performers");
                                    conn.commit();
                                    System.out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                    log.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");

                                  }catch (Exception e) {
                                            System.out.println("An error occured for database " + (k+1) + ": " + e);
                                            out.println("An error occured for database " + (k+1) + ": " + e);
                                            log.println("An error occured for database " + (k+1) + ": " + e);
                                            gotException = true;
                                            getPerformersException=true;
                                            conn.rollback();

                                 }finally{
                                     
                                     stmt.close();
                                     conn.close();
                                 }
                                 
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            }
            
             performer_array=null;
             System.runFinalization();     
    
              //updating high sales performers
              
             System.out.println("Updating high sales performers");
             out.println("Updating high sales performers");
             log.println("Updating high sales performers");
             boolean getHighSalesPerformersException = false;
             PerformerPercent[] sales_performer_array = null;
             update_date = "" + new java.util.Date() + "";
             try{
                        String websiteConfigIDString = props.getProperty("websiteConfigID");
                        String numReturned = ""+(numberOfPerformersReturned+10);                   
                        String parentCategoryID = "";
                        String childCategoryID = "";
                        String grandchildCategoryID = "";
                        String useProxy = props.getProperty("useProxy");
                        String proxyHost = props.getProperty("proxyHost");
                        String proxyPort = props.getProperty("proxyPort");
                        if (Boolean.valueOf(useProxy)) {
                            Properties systemSettings = System.getProperties();
                            systemSettings.put("http.proxyHost", proxyHost);
                            systemSettings.put("http.proxyPort", proxyPort);
                            System.setProperties(systemSettings);

                        }
                        TNWebServiceStringInputs tns = new TNWebServiceStringInputs(new URL("http://tnwebservices-test.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL"),  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
                        TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();
                        ArrayOfPerformerPercent array_of_performers = tnss.getHighSalesPerformers(websiteConfigIDString, numReturned, parentCategoryID, childCategoryID, grandchildCategoryID);
                      
                        if (array_of_performers == null) {
                                   
                                gotException = true;
                                getHighSalesPerformersException=true;
                                throw new Exception("Error - no High Sales Performers found");

                         }
                            java.util.List<PerformerPercent> performer_list = array_of_performers.getPerformerPercent();
                            sales_performer_array = performer_list.toArray(new PerformerPercent[0]);
                               

                           
                            if (sales_performer_array.length == 0) {
                                gotException = true;
                                getHighSalesPerformersException=true;
                                throw new Exception("Error - no High Sales Performers found");                                
                            }
                        
                          
             }catch(Exception e){
                        System.out.println("Exception while downloading high sales performer data: " + e);
                        out.println("Exception while downloading high sales performer data: " + e);
                        log.println("Exception while downloading high sales performer data: " + e);
                        gotException = true;
                        getHighSalesPerformersException=true;
             }
             
             k=0;
             while((k < num_dbs) && (!getHighSalesPerformersException)){ 
                    try{
                        Connection conn = null;
                        PreparedStatement psi = null;
                        Statement stmt = null;
                                 try{

                                        conn = getConnectionFromString(db_conn_strings[k]);
                                        conn.setAutoCommit(false);
                                        stmt = conn.createStatement();
                                        
                                        
                                        if(incremental_update){
                                             stmt.executeUpdate("DROP TABLE IF EXISTS backup_sales"); 
                                             stmt.executeUpdate(create_backup_sales_table);
                                             stmt.executeUpdate("insert into backup_sales select * from sales");
                                             psi = conn.prepareStatement("INSERT INTO sales (ID, Name,percent,ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription,URLName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update percent=VALUES(percent),ParentCategoryDescription=VALUES(ParentCategoryDescription),ChildCategoryDescription=VALUES(ChildCategoryDescription),GrandchildCategoryDescription=VALUES(GrandchildCategoryDescription),URLName=VALUES(URLName),URLParentName=VALUES(URLParentName),URLChildName=VALUES(URLChildName),URLGrandChildName=VALUES(URLGrandChildName) ");
                                            
                                        }else{
                                            stmt.executeUpdate("DROP TABLE IF EXISTS tempsales");
                                            stmt.executeUpdate(create_temp_sales_table);                                      
                                            psi = conn.prepareStatement("INSERT INTO tempsales (ID, Name,percent,ParentCategoryID, ChildCategoryID, GrandchildCategoryID,ParentCategoryDescription,ChildCategoryDescription,GrandchildCategoryDescription,URLName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                                   
                                        }
                                         int rows_affected=0;
                                        
                                        
                                            int rows = sales_performer_array.length;
                                        int i = 0;
                                        int inserts = 0;
                                        int updates = 0;
                         while (i < rows) {
                                Performer performer = sales_performer_array[i].getPerformer();
                                String name = performer.getDescription();
                                int id = performer.getID();
                                Category cat = performer.getCategory();
                                String ParentCategoryDescription = "" + cat.getParentCategoryDescription();
                                String ChildCategoryDescription = "" + cat.getChildCategoryDescription();
                                String GrandchildCategoryDescription = "" + cat.getGrandchildCategoryDescription();
                                float percent = 0.0f;
                                try {
                                    percent = ((sales_performer_array[i]).getPercent()).floatValue();
                                } catch (Exception e) {
                                    percent = 0.0f;
                                }
                                int parentID = cat.getParentCategoryID();
                                int childID = cat.getChildCategoryID();
                                int grand_childID = cat.getGrandchildCategoryID();

                                String URLName= key2UrlFilename(name, url_filename_space,useLowerCaseFilenames);
                                String URLParentName= key2UrlDir(ParentCategoryDescription,url_dirname_space,useLowerCaseDirnames);
                                String URLChildName=key2UrlDir(ChildCategoryDescription,url_dirname_space,useLowerCaseDirnames);
                                String URLGrandChildName=key2UrlDir(GrandchildCategoryDescription,url_dirname_space,useLowerCaseDirnames);
                            
                                psi.setInt(1, id);
                                psi.setString(2, name);
                                psi.setFloat(3, percent);
                                psi.setInt(4, parentID);
                                psi.setInt(5, childID);
                                psi.setInt(6, grand_childID);
                                psi.setString(7, ParentCategoryDescription);
                                psi.setString(8, ChildCategoryDescription);
                                psi.setString(9, GrandchildCategoryDescription);
                                psi.setString(10, URLName);
                                psi.setString(11, URLParentName);
                                psi.setString(12, URLChildName);
                                psi.setString(13, URLGrandChildName);
                                 rows_affected = psi.executeUpdate();
                                                if(rows_affected != 1) {
                                                    updates++;
                                                }else{
                                                    inserts++;
                                                            
                                                }       
                          

                                i++;

                     

                        }
                                        
                                    if(incremental_update){
                                        
                                    }else{  
                                        stmt.executeUpdate("DROP TABLE IF EXISTS backup_sales");
                                        stmt.executeUpdate("RENAME TABLE sales TO backup_sales, tempsales TO sales;");
                                        stmt.executeUpdate("create index sales_name_index using hash on sales (Name(120))");
                                        stmt.executeUpdate("create index sales_id_index on sales (ID)");
                                        stmt.executeUpdate("create index sales_parent_index using hash on sales (ParentCategoryDescription(120))");
                                        stmt.executeUpdate("create index sales_parentid_index on sales (ParentCategoryID)");
                                        stmt.executeUpdate("create index sales_parentchild_index using hash on sales (ParentCategoryDescription(120),ChildCategoryDescription(120))");
                                        stmt.executeUpdate("create index sales_parentchildid_index on sales (ParentCategoryID,ChildCategoryID)");
                                        stmt.executeUpdate("create index sales_parentchildgrandchild_index using hash on sales (ParentCategoryDescription(120),ChildCategoryDescription(120),GrandchildCategoryDescription(120))");
                                        stmt.executeUpdate("create index sales_parentchildgrandchildid_index on sales (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                                        stmt.executeUpdate("create index sales_percent_index on sales (percent)");
                                 
                                    }
                                        
                                    
                                      stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate, NumberofEntries) VALUES ('sales', '"+ update_date +"', '"+inserts+"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"',NumberofEntries= '"+inserts+"'");
                                      stmt.executeUpdate("OPTIMIZE TABLE sales");
                                      conn.commit();
                                      System.out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                      out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");
                                      log.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for db: "+(k+1)+".");

                                  }catch (Exception e) {
                                            System.out.println("An error occured for database " + (k+1) + ": " + e);
                                            out.println("An error occured for database " + (k+1) + ": " + e);
                                            log.println("An error occured for database " + (k+1) + ": " + e);
                                            gotException = true;
                                            getHighSalesPerformersException=true;
                                            conn.rollback();

                                 }finally{
                                     
                                     stmt.close();
                                     conn.close();
                                 }
                                 
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            }
            
             sales_performer_array=null;
             System.runFinalization();
             //prep db for event update
             boolean getEventsException = false;
             k=0;
                while(k < num_dbs){ 
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{
                           
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            
                            
                            if(incremental_update){
                                             stmt.executeUpdate("DROP TABLE IF EXISTS backup_events"); 
                                             stmt.executeUpdate(create_backup_events_table);
                                             stmt.executeUpdate("insert into backup_events select * from events");
                                                     
                                        }else{
                                            stmt.executeUpdate("DROP TABLE IF EXISTS tempevents");
                                            stmt.executeUpdate(create_temp_events_table);  
                                        }
                            
                            
                            
                               
                            conn.commit();
                           
                            
                         
                      }catch (Exception e) {
                                System.out.println("An error occured for database " + (k+1) + ": " + e);
                                out.println("An error occured for database " + (k+1) + ": " + e);
                                log.println("An error occured for database " + (k+1) + ": " + e);
                                gotException = true;
                                getEventsException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                                getEventsException = true;
                              
                                    
                     }
                     k++;
            }

             
             
             
             //updating events
             System.out.println("Updating events");
             out.println("Updating events");
             log.println("Updating events");
             update_date = "" + new java.util.Date() + "";
             int index = 0;
             int total_number_of_rows[] = new int[num_dbs];
             k=0;
             while(k < num_dbs){
                        total_number_of_rows[k]=0;
                        k++;
             }
             k=0;
            

             if(DEBUG){
                     index = Category_array.length-5;
                     if(index <= 0) index = Category_array.length;
             } //this updates for one cat only if we want ot go fast

             while(index < Category_array.length){
                
                 String parentCategoryID =     ""+Category_array[index].getParentCategoryID();
                 String childCategoryID =      ""+Category_array[index].getChildCategoryID();
                 String grandchildCategoryID = ""+Category_array[index].getGrandchildCategoryID();
                 String parentCategory =       ""+Category_array[index].getParentCategoryDescription();
                 String childCategory =        ""+Category_array[index].getChildCategoryDescription();
                 String grandchildCategory =   ""+Category_array[index].getGrandchildCategoryDescription();
                 String full_category_name = parentCategory + " " + childCategory +" "+ grandchildCategory;

                 Event[] event_array = null;
                 try{
                        String websiteConfigIDString = props.getProperty("websiteConfigID");
                        beginDate = props.getProperty("beginDate");
                        endDate = props.getProperty("endDate");
                        String eventID = "";
                        String eventName = "";
                        String eventDate = "";
                        String venueID = "";
                        String venueName = "";
                        String stateProvince = "";
                        String stateProvinceID = "";
                        String cityZip = "";
                        String nearZip = "";
                        String performerID = "";
                        String performerName = "";
                        String noPerformers = "";
                        String lowPrice = "";
                        String highPrice = "";
                        String modificationDate = "";
                        String onlyMine = "";
                        String whereClause= "";
                        String orderByClause= "";        
                        String numberOfEvents = "";
                        
                        
                        String useProxy = props.getProperty("useProxy");
                        String proxyHost = props.getProperty("proxyHost");
                        String proxyPort = props.getProperty("proxyPort");
                        if (Boolean.valueOf(useProxy)) {
                            Properties systemSettings = System.getProperties();
                            systemSettings.put("http.proxyHost", proxyHost);
                            systemSettings.put("http.proxyPort", proxyPort);
                            System.setProperties(systemSettings);

                        }
                        TNWebServiceStringInputs tns = new TNWebServiceStringInputs(new URL("http://tnwebservices-test.ticketnetwork.com/TNWebservice/v3.0/TNWebserviceStringInputs.asmx?WSDL"),  new QName("http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0", "TNWebServiceStringInputs"));
                        TNWebServiceStringInputsSoap tnss = tns.getTNWebServiceStringInputsSoap();
                        ArrayOfEvent array_of_events = tnss.getEvents(websiteConfigIDString, numberOfEvents, eventID, eventName, eventDate, beginDate, endDate, venueID, venueName, stateProvince, stateProvinceID, cityZip, nearZip, parentCategoryID, childCategoryID, grandchildCategoryID, performerID, performerName, noPerformers, lowPrice, highPrice, modificationDate, onlyMine, whereClause,orderByClause);
                                
                                
                                if (array_of_events == null) {

                                    System.out.println("Null events for cat: " + full_category_name);
                                    out.println("Null events for cat: "+full_category_name);
                                    log.println("Null events for cat: "+full_category_name);
                                    gotException = true;
                                    getEventsException = true;
                                    
                                }                                
                                java.util.List<Event> event_list = array_of_events.getEvent();
                                event_array = event_list.toArray(new Event[0]);

                 }catch(Exception e){
                                System.out.println("Exception while downloading event data: " + e);
                                out.println("Exception while downloading event data: " + e);
                                log.println("Exception while downloading event data: " + e);
                                gotException = true;
                                getEventsException = true;
                                
                     }

                    k=0;
                    
                    while(k < num_dbs){
                      
                    try{
                        Connection conn = null;
                        PreparedStatement psi = null;
                        Statement stmt = null;
                                 try{

                                        conn = getConnectionFromString(db_conn_strings[k]);
                                        conn.setAutoCommit(false);
                                      
                                        
                                                                           
                                        if(incremental_update){
                                             
                                               psi = conn.prepareStatement("INSERT INTO events (ID,Name,Date,DisplayDate,Venue,City,StateProvince,ParentCategoryID,ChildCategoryID,GrandchildCategoryID,MapURL,VenueID,StateProvinceID,VenueConfigurationID,Clicks,isWomensEvent,DateString, TimeString,AlternateName,Performer1,Performer2,URLName,URLAlternateName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) on duplicate key update Name=VALUES(Name),Date=VALUES(Date),DisplayDate=VALUES(DisplayDate),Venue=VALUES(Venue),City=VALUES(City),StateProvince=VALUES(StateProvince),ParentCategoryID=VALUES(ParentCategoryID),ChildCategoryID=VALUES(ChildCategoryID),GrandchildCategoryID=VALUES(GrandchildCategoryID),MapURL=VALUES(MapURL),VenueID=VALUES(VenueID),StateProvinceID=VALUES(StateProvinceID),VenueConfigurationID=VALUES(VenueConfigurationID),Clicks=VALUES(Clicks),isWomensEvent=VALUES(isWomensEvent),DateString=VALUES(DateString), TimeString=VALUES(TimeString),AlternateName=VALUES(AlternateName),Performer1=VALUES(Performer1),Performer2=VALUES(Performer2),URLName=VALUES(URLName),URLAlternateName=VALUES(URLAlternateName),URLParentName=VALUES(URLParentName),URLChildName=VALUES(URLChildName),URLGrandChildName=VALUES(URLGrandChildName)");
                                          
                                        }else{
                                             psi = conn.prepareStatement("INSERT INTO tempevents (ID,Name,Date,DisplayDate,Venue,City,StateProvince,ParentCategoryID,ChildCategoryID,GrandchildCategoryID,MapURL,VenueID,StateProvinceID,VenueConfigurationID,Clicks,isWomensEvent,DateString, TimeString,AlternateName,Performer1,Performer2,URLName,URLAlternateName,URLParentName,URLChildName,URLGrandChildName) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                                       
                                        }
                                         int rows_affected=0;
                                                                           
                                        
                                        
                                        int rows = event_array.length;
                                        int i = 0;
                                        int inserts = 0;
                                        int updates = 0;
                         while (i < rows) {
                                int id = event_array[i].getID();
                                String name = event_array[i].getName();
                                //System.out.println(replaceStringWithString("" + event_array[i].getDate() + "", "T", " "));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                java.util.Date date1 = sdf.parse(replaceStringWithString("" + event_array[i].getDate() + "", "T", " "));
                                java.sql.Timestamp date = new java.sql.Timestamp(date1.getTime());
                                //System.out.println(date.toString());
                                String displaydate = event_array[i].getDisplayDate();
                                String venue = event_array[i].getVenue();
                                String city = event_array[i].getCity();
                                String StateProvince = event_array[i].getStateProvince();
                                int parentID = event_array[i].getParentCategoryID();
                                int childID = event_array[i].getChildCategoryID();
                                int grand_childID = event_array[i].getGrandchildCategoryID();
                                String mapURL = event_array[i].getMapURL();
                                int venueid = event_array[i].getVenueID();
                                int StateProvinceID = event_array[i].getStateProvinceID();
                                int venueConfigurationID = event_array[i].getVenueConfigurationID();
                                int clicks = event_array[i].getClicks();
                                boolean isWomensEvent = event_array[i].isIsWomensEvent();

                                String DateString = displaydate;
                                DateString=getDateString(DateString);
                                String TimeString = displaydate;
                                TimeString=getTimeString(TimeString);
                                String AlternateName=name;
                                String Performer1=name;
                                String Performer2=name;

                                String URLName= key2UrlFilename(name, url_filename_space,useLowerCaseFilenames);
                                String URLParentName=key2UrlDir(parentCategory,url_dirname_space,useLowerCaseDirnames);
                                String URLChildName=key2UrlDir(childCategory,url_dirname_space,useLowerCaseDirnames);
                                String URLGrandChildName=key2UrlDir(grandchildCategory,url_dirname_space,useLowerCaseDirnames);




                                //do some special processing for sports
                                if(parentID == SPORTS_CAT_ID){
                                    String teams[] = getTeams(Performer1);
                                    if(teams.length >= 2){
                                        Performer1 = teams[0];
                                        Performer2= teams[1];
                                    }
                                    Enumeration keys = team_name_mappings.keys();
                                    while (keys.hasMoreElements()) {

                                        String team = (String) keys.nextElement();
                                        if (AlternateName.contains(team)) {
                                            AlternateName = AlternateName.replaceAll(team, team_name_mappings.getProperty(team));

                                        }

                                    }
                                }
                                String URLAlternateName= key2UrlFilename(AlternateName, url_filename_space,useLowerCaseFilenames);

                                psi.setInt(1, id);
                                psi.setString(2, name);
                                psi.setTimestamp(3, date);
                                psi.setString(4, displaydate);
                                psi.setString(5, venue);
                                psi.setString(6, city);
                                psi.setString(7, StateProvince);
                                psi.setInt(8, parentID);
                                psi.setInt(9, childID);
                                psi.setInt(10, grand_childID);
                                psi.setString(11, mapURL);
                                psi.setInt(12, venueid);
                                psi.setInt(13, StateProvinceID);
                                psi.setInt(14, venueConfigurationID);
                                psi.setInt(15, clicks);
                                psi.setBoolean(16, isWomensEvent);
                                psi.setString(17, DateString);
                                psi.setString(18, TimeString);
                                psi.setString(19, AlternateName);
                                psi.setString(20, Performer1);
                                psi.setString(21, Performer2);
                                psi.setString(22, URLName);
                                psi.setString(23, URLAlternateName);
                                psi.setString(24, URLParentName);
                                psi.setString(25, URLChildName);
                                psi.setString(26, URLGrandChildName);
                               rows_affected = psi.executeUpdate();
                                                if(rows_affected != 1) {
                                                    updates++;
                                                }else{
                                                    inserts++;
                                                            
                                                }              
                          

                                i++;

                     

                        }
                                    
                                  
                                    conn.commit();
                                    System.out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows + " for cat number: " +(index+1) + " of " + Category_array.length + " = " +  full_category_name + " for db: "+(k+1));
                                    out.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows+ " for cat number: " +(index+1) + " of " + Category_array.length + " = " +  full_category_name+ " for db: "+(k+1));
                                    log.println("Performed " + inserts + " inserts and " + updates + " updates on " + rows+ " for cat number: " +(index+1) + " of " + Category_array.length + " = " +  full_category_name+ " for db: "+(k+1));
                                    total_number_of_rows[k] = total_number_of_rows[k] + rows;
                                  }catch (Exception e) {
                                            System.out.println("An error occured for database " + (k+1) + ": " + e);
                                            out.println("An error occured for database " + (k+1) + ": " + e);
                                            log.println("An error occured for database " + (k+1) + ": " + e);
                                            gotException = true;
                                            getEventsException = true;
                                            conn.rollback();

                                 }finally{
                                     
                                     
                                     conn.close();
                                 }
                                 
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                                getEventsException = true;
                              
                                    
                     }
                     k++;
            }
                 
                 
                 
                 //end of event updates for this category
                 event_array = null;
                 System.runFinalization();
                 index++;
             } 
             System.runFinalization();
             
             //take care of events table
                 k=0;
                while(k < num_dbs && !getEventsException && !getVenuesException){
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{
                           
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            
                            if(incremental_update){
                                        stmt.executeUpdate("update events set events.AbbrvCountry=(select AbbrvCountry from venues where venues.ID=events.venueID)");
                                        stmt.executeUpdate("update events set events.Country=(select Country from venues where venues.ID=events.venueID)");
                                        stmt.executeUpdate("update events set events.LongStateProvince=(select StateProvince from venues where venues.ID=events.venueID)");
                                        stmt.executeUpdate("update events set events.location=(select location from venues where venues.ID=events.venueID)");
                                        

                            }else{
                                     stmt.executeUpdate("update tempevents set tempevents.AbbrvCountry=(select AbbrvCountry from venues where venues.ID=tempevents.venueID)");
                                     stmt.executeUpdate("update tempevents set tempevents.Country=(select Country from venues where venues.ID=tempevents.venueID)");
                                     stmt.executeUpdate("update tempevents set tempevents.LongStateProvince=(select StateProvince from venues where venues.ID=tempevents.venueID)");
                                     stmt.executeUpdate("update tempevents set tempevents.location=(select location from venues where venues.ID=tempevents.venueID)");
                                     stmt.executeUpdate("DROP TABLE IF EXISTS backup_events");
                                     stmt.executeUpdate("RENAME TABLE events TO backup_events, tempevents TO events;"); 
                                     stmt.executeUpdate("create index events_name_index using hash on events (Name(120))");
                                     stmt.executeUpdate("create index events_parentid_index on events (ParentCategoryID)");
                                     stmt.executeUpdate("create index events_parentchildid_index on events (ParentCategoryID,ChildCategoryID)");
                                     stmt.executeUpdate("create index events_parentchildgrandchildid_index on events (ParentCategoryID,ChildCategoryID,GrandchildCategoryID)");
                                     stmt.executeUpdate("create index events_venuename_index using hash on events (Venue(120))");
                                     stmt.executeUpdate("create index events_city_index using hash on events (City(120))");
                                     stmt.executeUpdate("create index events_stateprovince_index using hash on events (LongStateProvince(120))");
                                     stmt.executeUpdate("create index events_location_index using hash on events (location(120))");
                                     stmt.executeUpdate("create index events_abbrvstateprovince_index using hash on events (StateProvince(120))");
                                     stmt.executeUpdate("create index events_citystate_index using hash on events (City(120),LongStateProvince(120))");
                                     stmt.executeUpdate("create index events_cityabbrvstate_index using hash on events (City(120),StateProvince(120))");
                                     stmt.executeUpdate("create index events_country_index using hash on events (Country(120))");
                                     stmt.executeUpdate("create index events_abbrvcountry_index using hash on events (AbbrvCountry(120))");
                                     stmt.executeUpdate("create index events_venueid_index on events (VenueID)");
                                     stmt.executeUpdate("create index events_altname_index using hash on events (AlternateName(120))");
                                     stmt.executeUpdate("create index events_performer1_index using hash on events (Performer1(120))");
                                     stmt.executeUpdate("create index events_performer2_index using hash on events (Performer2(120))");
                                     stmt.executeUpdate("create index events_performer12_index using hash on events (Performer1(120),Performer2(120))");
                            }
                                        
                            
                         
                            
                            stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate, NumberofEntries) VALUES ('events', '"+ update_date +"', '"+total_number_of_rows[k]+"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"',NumberofEntries= '"+total_number_of_rows[k]+"'");
                            stmt.executeUpdate("OPTIMIZE TABLE events");
                            
                           
                            
                            conn.commit();
                           
                            
                         
                      }catch (Exception e) {
                                System.out.println("An error occured for database " + (k+1) + ": " + e);
                                out.println("An error occured for database " + (k+1) + ": " + e);
                                log.println("An error occured for database " + (k+1) + ": " + e);
                                gotException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
            System.out.println("Processed " + total_number_of_rows[k] + " events for db: "+(k+1)+".");
            out.println("Processed " + total_number_of_rows[k] + " events for db: "+(k+1)+".");
            log.println("Processed " + total_number_of_rows[k] + " events for db: "+(k+1)+".");
                     k++;
            }

            

            System.runFinalization(); 
             //update geo table
             update_date = "" + new java.util.Date() + "";
             System.out.println("Updating geographic data");
             out.println("Updating geographic data");
             log.println("Updating geographic data");
            k=0;
                while(k < num_dbs && !getVenuesException){
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{
                           
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            if(incremental_update){
                                             stmt.executeUpdate("DROP TABLE IF EXISTS backup_geo"); 
                                             stmt.executeUpdate(create_backup_geo_table);
                                             stmt.executeUpdate("insert into backup_geo select * from geo");
                                             stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");
                                             stmt.executeUpdate(create_temp_geo_table);
                                             stmt.executeUpdate("insert into tempgeo select * from geo");
                                             stmt.executeUpdate("insert into tempgeo select City, URLName,URLParentName, URLChildName, URLGrandChildName, StateProvince, location, AbbrvCountry, AbbrvStateProvince, Country from venues");
                                             stmt.executeUpdate("update tempgeo set tempgeo.URLName=(select Distinct DefaultGeoName from venues where (tempgeo.City=venues.City and tempgeo.StateProvince=venues.StateProvince and tempgeo.Country=venues.Country))");
                                             stmt.executeUpdate("delete from geo");
                                             stmt.executeUpdate("insert into geo select distinct City, URLName, URLParentName, URLChildName, URLGrandChildName, StateProvince, location, AbbrvCountry, AbbrvStateProvince, Country from tempgeo");
                                             stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");
                           
                                             
                            }else{
                                stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");
                                stmt.executeUpdate(create_temp_geo_table);
                                stmt.executeUpdate("insert into tempgeo select City, URLName, URLParentName, URLChildName, URLGrandChildName, StateProvince, location, AbbrvCountry, AbbrvStateProvince, Country from venues");
                                stmt.executeUpdate("update tempgeo set tempgeo.URLName=(select Distinct DefaultGeoName from venues where (tempgeo.City=venues.City and tempgeo.StateProvince=venues.StateProvince and tempgeo.Country=venues.Country))");
                                stmt.executeUpdate("DROP TABLE IF EXISTS backup_geo");
                                stmt.executeUpdate("RENAME TABLE geo TO backup_geo");
                                stmt.executeUpdate(create_geo_table);
                                stmt.executeUpdate("insert into geo select distinct City, URLName, URLParentName, URLChildName, URLGrandChildName, StateProvince, location, AbbrvCountry, AbbrvStateProvince, Country from tempgeo");
                                stmt.executeUpdate("DROP TABLE IF EXISTS tempgeo");
                           
                                stmt.executeUpdate("create index geo_city_index using hash on geo (City(120))");
                                stmt.executeUpdate("create index geo_stateprovince_index using hash on geo (StateProvince(120))");
                                stmt.executeUpdate("create index geo_location_index using hash on geo (location(120))");
                                stmt.executeUpdate("create index geo_abbrvstateprovince_index using hash on geo (AbbrvStateProvince(120))");
                                stmt.executeUpdate("create index geo_citystate_index using hash on geo (City(120),StateProvince(120))");
                                stmt.executeUpdate("create index geo_cityabbrvstate_index using hash on geo (City(120),AbbrvStateProvince(120))");
                                stmt.executeUpdate("create index geo_country_index using hash on geo (Country(120))");
                                stmt.executeUpdate("create index geo_abbrvcountry_index using hash on geo (AbbrvCountry(120))");
                         
                            }
                            stmt.executeUpdate("OPTIMIZE TABLE geo");
                            stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate) VALUES ('geo', '"+ update_date +"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"'");
               
                            conn.commit();
                                                
                      }catch (Exception e) {
                                System.out.println("An error occured for database " + (k+1) + ": " + e);
                                out.println("An error occured for database " + (k+1) + ": " + e);
                                log.println("An error occured for database " + (k+1) + ": " + e);
                                gotException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            } 
            System.runFinalization();   
             
              //update metadata table
              System.out.println("Updating metadata");
              out.println("Updating metadata");
              log.println("Updating metadata");
              k=0;
                while(k < num_dbs){ 
                    try{
                        Connection conn = null;
                        Statement stmt = null;
                        try{
                           
                            conn = getConnectionFromString(db_conn_strings[k]);
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            stmt.executeUpdate("INSERT INTO tempmetadata (updatetype, updatedate) VALUES ('metadata', '"+ update_date +"')ON DUPLICATE KEY UPDATE updatedate = '"+ update_date +"'");
                            stmt.executeUpdate("DROP TABLE IF EXISTS backup_metadata");                              
                            stmt.executeUpdate("RENAME TABLE metadata TO backup_metadata, tempmetadata TO metadata;");
                            
                                
                            conn.commit();
                                                
                      }catch (Exception e) {
                                System.out.println("An error occured for database " + (k+1) + ": " + e);
                                out.println("An error occured for database " + (k+1) + ": " + e);
                                log.println("An error occured for database " + (k+1) + ": " + e);
                                gotException = true;
                                conn.rollback();
                                    
                     }finally{
                         stmt.close();
                         conn.close();
            
                     }
                     }catch (Exception e) {
                                System.out.println("Error in database " + (k+1) + ": " + e);
                                out.println("Error in database " + (k+1) + ": " + e);
                                log.println("Error in database " + (k+1) + ": " + e);
                                gotException = true;
                              
                                    
                     }
                     k++;
            } 
               
             
         
         System.runFinalization();   
             
        
       }//end of update
           
            
       } catch (Exception endex) {
            gotException = true;
            out.println("Exception in main: " + endex);
            log.println("Exception in main: " + endex);
            System.out.println("Exception in main: " + endex);

       }


        out.println("-----Ending updates at: " + new java.util.Date() + "-");
        System.out.println("-----Ending updates at: " + new java.util.Date() + "-");
        log.println("-----Ending updates at: " + new java.util.Date() + "-");
        log.println("-----End---JData-Run------------------------------------------------");
        
        System.out.println("-----End---JData-Run------------------------------------------------");
     
        out.println("-----End---JData-Run------------------------------------------------");
        out.println("--------------------------------------------------------------------");
        out.println("############################################################################");
        if (sendMail) {
            try {
                out.flush();
                out.close();
                String subject = "JData Update ";
                if (gotException) {
                    subject = subject + "has Exceptions ";
                } else {
                    subject = subject + "Successful ";
                }
                subject = subject + (new java.util.Date());
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                sendMessage(email_to, subject, email.toString(), email_from, smtp_server, smtp_port, useSMTPAuth, smtpUsername, smtpPassword);
            //System.out.println("Email sent.");
            } catch (Exception e) {

                log.println("Exception while sending jdata email: " + e);
                System.out.println("Exception while sending jdata email: " + e);
            }
        }
       
        log.println("--------------------------------------------------------------------");
        log.println("############################################################################");

        log.flush();
        log.close();
       
        System.out.println("--------------------------------------------------------------------");
        System.out.println("############################################################################");


    } //end of main                
                                  
                    public static Connection getConnectionFromString(String connection_string)throws Exception{
                    Connection conn = null;
                    Properties systemSettings = System.getProperties();
                    systemSettings.put("http.proxyHost", "");
                    systemSettings.put("http.proxyPort", "");
                    System.setProperties(systemSettings);
                    conn = DriverManager.getConnection(connection_string);
                    return conn;

    }  

                    
 public static void sendMessage(String recipient, String subject, String message, String from, String SMTP_HOST_NAME, String SMTP_PORT, String useAuth, final String username, final String password) throws MessagingException {

        if (useAuth.compareTo("true") == 0) {
            boolean debug = true;
            String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            //props.put("mail.debug", "true");
            props.put("mail.smtp.auth", useAuth);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.put("mail.smtp.socketFactory.fallback", "false");

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            //session.setDebug(debug);

            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            String[] recipients = delimitedStringToArray(recipient, ",");
            InternetAddress[] addressTo = new InternetAddress[recipients.length];

            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            //InternetAddress addressTo = new InternetAddress(recipient);
            //msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } else {
            boolean debug = true;

            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.socketFactory.port", SMTP_PORT);

            props.put("mail.smtp.socketFactory.fallback", "false");

            Session session = Session.getDefaultInstance(props);




            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);


            String[] recipients = delimitedStringToArray(recipient, ",");
            InternetAddress[] addressTo = new InternetAddress[recipients.length];

            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            //InternetAddress addressTo = new InternetAddress(recipient);
            //msg.setRecipient(Message.RecipientType.TO, addressTo);

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        }
    }//end of sendMessage
                    
                    static String create_metadata_table = "CREATE TABLE IF NOT EXISTS metadata(updatetype       VARCHAR(128) not null,PRIMARY KEY (updatetype),updatedate  TEXT,NumberofEntries  TEXT)"+DB_ENGINE;
                    static String create_backup_metadata_table = "CREATE TABLE IF NOT EXISTS backup_metadata(updatetype       VARCHAR(128) not null,PRIMARY KEY (updatetype),updatedate  TEXT,NumberofEntries  TEXT)"+DB_ENGINE;
                    static String create_temp_metadata_table = "CREATE TABLE IF NOT EXISTS tempmetadata(updatetype       VARCHAR(128) not null,PRIMARY KEY (updatetype),updatedate  TEXT,NumberofEntries  TEXT)"+DB_ENGINE;
                  
                    static String create_geo_table = "CREATE  TABLE IF NOT EXISTS  geo(" + "City         TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "StateProvince        TEXT, " + "location     TEXT, " + "AbbrvCountry     TEXT, " +  "AbbrvStateProvince     TEXT, " +"Country          TEXT)"+DB_ENGINE;
                    static String create_temp_geo_table = "CREATE  TABLE IF NOT EXISTS  tempgeo(" + "City         TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "StateProvince        TEXT, " + "location     TEXT, " + "AbbrvCountry     TEXT, " +  "AbbrvStateProvince     TEXT, " +"Country          TEXT)"+DB_ENGINE;
                    static String create_backup_geo_table = "CREATE  TABLE IF NOT EXISTS  backup_geo(" + "City         TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "StateProvince        TEXT, " + "location     TEXT, " + "AbbrvCountry     TEXT, " +  "AbbrvStateProvince     TEXT, " +"Country          TEXT)"+DB_ENGINE;
                    
                    static String create_events_table = "CREATE  TABLE IF NOT EXISTS  events(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " +"Date         DATETIME, " + "DisplayDate     TEXT, " + "Venue     TEXT, "  + "City         TEXT, " + "StateProvince        TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER, " + "MapURL     TEXT, "  +"VenueID   INTEGER, " + "StateProvinceID  INTEGER, " + "VenueConfigurationID  INTEGER, " + "Clicks  INTEGER, " + "URLName         TEXT, "+  "URLAlternateName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "+"AlternateName     TEXT, " + "Performer1     TEXT, "+ "Performer2     TEXT, "+ "AbbrvCountry     TEXT, "+ "LongStateProvince     TEXT, " + "DateString     TEXT, " + "TimeString     TEXT, "+ "location     TEXT, " + "Country     TEXT, "+ "IsWomensEvent          BOOL)"+DB_ENGINE;
                    
                    static String create_performers_table = "CREATE  TABLE IF NOT EXISTS  performers(" + "ID       INTEGER NOT NULL," + "Name          VARCHAR(767) NOT NULL, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_categories_table = "CREATE  TABLE IF NOT EXISTS  categories(" + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + " PRIMARY KEY(ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_venues_table = "CREATE  TABLE IF NOT EXISTS  venues(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " + "URL          TEXT, " + "Notes          TEXT, " +
                            "Street1          TEXT, " + "Street2          TEXT, " + "City          TEXT, " + "StateProvince          TEXT, " + "AbbrvStateProvince     TEXT, " + "AbbrvCountry     TEXT, " + "location     TEXT, " +"ZipCode          TEXT, " + "Capacity          TEXT, " + "Directions          TEXT, " + "Parking          TEXT, " + "PublicTransportation          TEXT, " + "BoxOfficePhone          TEXT, " + "WillCall          TEXT, " + "Rules          TEXT, " +
                            "ChildRules          TEXT, " + "DefaultGeoName          TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "Country          TEXT)"+DB_ENGINE;
                    static String create_sales_table = "CREATE  TABLE IF NOT EXISTS  sales(" + "ID       INTEGER NOT NULL," + "Name           VARCHAR(767) NOT NULL, " + "percent          FLOAT, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;

                            
                   static String create_temp_events_table = "CREATE  TABLE IF NOT EXISTS  tempevents(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " +"Date         DATETIME, " + "DisplayDate     TEXT, " + "Venue     TEXT, "  + "City         TEXT, " + "StateProvince        TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER, " + "MapURL     TEXT, "  +"VenueID   INTEGER, " + "StateProvinceID  INTEGER, " + "VenueConfigurationID  INTEGER, " + "Clicks  INTEGER, " + "URLName         TEXT, "+  "URLAlternateName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "+"AlternateName     TEXT, " + "Performer1     TEXT, "+ "Performer2     TEXT, "+ "AbbrvCountry     TEXT, "+ "LongStateProvince     TEXT, " + "DateString     TEXT, " + "TimeString     TEXT, "+ "location     TEXT, " + "Country     TEXT, "+ "IsWomensEvent          BOOL)"+DB_ENGINE;

                    static String create_temp_performers_table = "CREATE  TABLE IF NOT EXISTS  tempperformers(" + "ID       INTEGER NOT NULL," + "Name          VARCHAR(767) NOT NULL, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_temp_categories_table = "CREATE  TABLE IF NOT EXISTS  tempcategories(" + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + " PRIMARY KEY(ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_temp_venues_table = "CREATE  TABLE IF NOT EXISTS  tempvenues(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " + "URL          TEXT, " + "Notes          TEXT, " +
                            "Street1          TEXT, " + "Street2          TEXT, " + "City          TEXT, " + "StateProvince          TEXT, " + "AbbrvStateProvince     TEXT, " + "AbbrvCountry     TEXT, " + "location     TEXT, " +"ZipCode          TEXT, " + "Capacity          TEXT, " + "Directions          TEXT, " + "Parking          TEXT, " + "PublicTransportation          TEXT, " + "BoxOfficePhone          TEXT, " + "WillCall          TEXT, " + "Rules          TEXT, " +
                            "ChildRules          TEXT, " + "DefaultGeoName          TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "Country          TEXT)"+DB_ENGINE;
                    static String create_temp_sales_table = "CREATE  TABLE IF NOT EXISTS  tempsales(" + "ID       INTEGER NOT NULL," + "Name           VARCHAR(767) NOT NULL, " + "percent          FLOAT, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;


                    static String create_backup_events_table = "CREATE  TABLE IF NOT EXISTS  backup_events(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " +"Date         DATETIME, " + "DisplayDate     TEXT, " + "Venue     TEXT, "  + "City         TEXT, " + "StateProvince        TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER, " + "MapURL     TEXT, "  +"VenueID   INTEGER, " + "StateProvinceID  INTEGER, " + "VenueConfigurationID  INTEGER, " + "Clicks  INTEGER, " + "URLName         TEXT, "+  "URLAlternateName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "+"AlternateName     TEXT, " + "Performer1     TEXT, "+ "Performer2     TEXT, "+ "AbbrvCountry     TEXT, "+ "LongStateProvince     TEXT, " + "DateString     TEXT, " + "TimeString     TEXT, "+ "location     TEXT, " + "Country     TEXT, "+ "IsWomensEvent          BOOL)"+DB_ENGINE;

                    static String create_backup_performers_table = "CREATE  TABLE IF NOT EXISTS  backup_performers(" + "ID       INTEGER NOT NULL," + "Name          VARCHAR(767) NOT NULL, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_backup_categories_table = "CREATE  TABLE IF NOT EXISTS  backup_categories(" + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + " PRIMARY KEY(ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;
                    static String create_backup_venues_table = "CREATE  TABLE IF NOT EXISTS  backup_venues(" + "ID       INTEGER NOT NULL," + "PRIMARY KEY (ID)," + "Name          TEXT NOT NULL, " + "URL          TEXT, " + "Notes          TEXT, " +
                            "Street1          TEXT, " + "Street2          TEXT, " + "City          TEXT, " + "StateProvince          TEXT, " + "AbbrvStateProvince     TEXT, " + "AbbrvCountry     TEXT, " + "location     TEXT, " +"ZipCode          TEXT, " + "Capacity          TEXT, " + "Directions          TEXT, " + "Parking          TEXT, " + "PublicTransportation          TEXT, " + "BoxOfficePhone          TEXT, " + "WillCall          TEXT, " + "Rules          TEXT, " +
                            "ChildRules          TEXT, " + "DefaultGeoName          TEXT, " + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + "Country          TEXT)"+DB_ENGINE;
                    static String create_backup_sales_table = "CREATE  TABLE IF NOT EXISTS  backup_sales(" + "ID       INTEGER NOT NULL," + "Name           VARCHAR(767) NOT NULL, " + "percent          FLOAT, " + "ParentCategoryDescription     TEXT, " + "ChildCategoryDescription          TEXT, " + "GrandchildCategoryDescription         TEXT, " + "ParentCategoryID        INTEGER, " + "ChildCategoryID          INTEGER, " + "GrandchildCategoryID          INTEGER," + "URLName         TEXT, "+"URLParentName         TEXT, "+ "URLChildName         TEXT, "+ "URLGrandChildName         TEXT, "  + " PRIMARY KEY(ID, Name, ParentCategoryID,ChildCategoryID,GrandchildCategoryID))"+DB_ENGINE;












                    public static String replaceStringWithString(String s, String p, String r) {

        StringTokenizer st = new StringTokenizer(s, p, false);
        String t = "";
        boolean beginning = true;
        while (st.hasMoreElements()) {
            if (beginning) {
                t += st.nextElement();
                beginning =
                        false;
            } else {
                t += r;
                t +=
                        st.nextElement();
            }

        }
        return t;
    }
                   public static String replaceString(Properties p, String s) {
        String temp = p.getProperty(s.trim());
        if (temp == null) {
            temp = "";
        }
        return temp.trim();


    }//end replace string
                   public static String replaceStringOrKeepSame(Properties p, String s) {
        String temp = p.getProperty(s.trim());
        if (temp == null) {
            return s;
        }
        return temp.trim();


    }//end replace string or keep same
                   public static String replaceStringOrMakeNull(Properties p, String s) {
        String temp = p.getProperty(s.trim());
        if (temp == null) {
            return temp;
        }
        return temp.trim();


    }//end replace string or make null
                   public static String[] delimitedStringToArray(String s, String delimiter) {
        //System.out.println("Parsing: " + s );
        //System.out.println("Delimiter " + delimiter );
        StringTokenizer st = new StringTokenizer(s, delimiter, false);
        int i = 0;
        while (st.hasMoreElements()) {
            String temp = (String) st.nextElement();

            i++;

        }
        //System.out.println("Getting ready to tokenize1" );
        if (i == 0) {
            return (new String[0]);
        }
//System.out.println("Getting ready to tokenize2" );
        String[] array = new String[i];
        st =
                new StringTokenizer(s, delimiter, false);
        i =
                0;
        while (st.hasMoreElements()) {
            array[i] = (String) st.nextElement();
            i++;

        }


        return array;


    } //end of delimitedStringToArray
                   public static String[][] doubleDelimitedStringToArray(String s, String outer_delimiter, String inner_delimiter) {

        StringTokenizer ost = new StringTokenizer(s, outer_delimiter, false);
        //System.out.println("Parsing: " + s );
        //System.out.println("Delimiter " + outer_delimiter );
        int i = 0;
        while (ost.hasMoreElements()) {
            String temp = (String) ost.nextElement();
            //System.out.println("" +i + " th String: "  + temp );
            i++;

        }


        if (i == 0) {
            return (new String[0][0]);
        }

        ost = new StringTokenizer(s, outer_delimiter, false);
        String array[][] = new String[i][];
        i =
                0;
        //System.out.println("Entering loop");
        while (ost.hasMoreElements()) {
            String temp = (String) ost.nextElement();
            // System.out.println("" +i + " th String in 2nd loop: "  + temp.trim() );
            array[i] = delimitedStringToArray(temp.trim(), inner_delimiter);
            //System.out.println("" + i + " " + array[i][0] + " , " + array[i][1]);
            i++;

        }

        return array;


    } //end of doubledelimitedStringToArray
                   static String getTimeString(String date_time){

                    //fix time/date here
                    String raw_date = "date";
                    String time_string = "TBA";
                    String date_string = "";
                    try {


                        if (!date_time.contains("TBA")) {
                            String date_pattern_str = "[0-9]+/[0-9]+/[0-9]+";
                            String time_pattern_str = "[0-9]+:[0-9]+";

                            Pattern date_pattern = Pattern.compile(date_pattern_str);
                            Pattern time_pattern = Pattern.compile(time_pattern_str);

                            Matcher date_matcher = date_pattern.matcher(date_time);
                            Matcher time_matcher = time_pattern.matcher(date_time);

                            date_matcher.find();
                            time_matcher.find();

                            raw_date = date_matcher.group();
                            String raw_time = time_matcher.group();
                            String date[] = raw_date.split("/");
                            String time[] = raw_time.split(":");
                            time_string = "";
                            date_string = "";
                            String am_or_pm = "";

                            int hour = Integer.parseInt(time[0].trim(), 10);
                            int min = Integer.parseInt(time[1].trim(), 10);
                            int month = Integer.parseInt(date[0].trim(), 10);
                            int day = Integer.parseInt(date[1].trim(), 10);
                            int year = Integer.parseInt(date[2].trim(), 10);


                            String month_string = " ";
                            switch (month) {
                                case 1:
                                    month_string = "January";
                                    break;
                                case 2:
                                    month_string = "February";
                                    break;
                                case 3:
                                    month_string = "March";
                                    break;
                                case 4:
                                    month_string = "April";
                                    break;
                                case 5:
                                    month_string = "May";
                                    break;
                                case 6:
                                    month_string = "June";
                                    break;
                                case 7:
                                    month_string = "July";
                                    break;
                                case 8:
                                    month_string = "August";
                                    break;
                                case 9:
                                    month_string = "September";
                                    break;
                                case 10:
                                    month_string = "October";
                                    break;
                                case 11:
                                    month_string = "November";
                                    break;
                                case 12:
                                    month_string = "December";
                                    break;
                                default:
                                    month_string = "Invalid month.";
                                    break;
                            }



                            if (date_time.contains("AM")) {
                                am_or_pm = "a.m.";
                                if (min > 9) {
                                    time_string = hour + ":" + min + " " + am_or_pm;
                                } else {
                                    time_string = hour + ":0" + min + " " + am_or_pm;
                                }
                            } else if (date_time.contains("PM")) {
                                am_or_pm = "p.m.";
                                if (min > 9) {
                                    time_string = hour + ":" + min + " " + am_or_pm;
                                } else {
                                    time_string = hour + ":0" + min + " " + am_or_pm;
                                }

                            } else {
                                time_string = "unknown";
                            }


                            date_string = month_string + " " + day + ", " + year;
                            if (time_string.equalsIgnoreCase("3:00 a.m.") || time_string.equalsIgnoreCase("3:30 a.m.")) {

                                time_string = "TBA";
                            //System.out.println("**GOT A 3am TBA**");

                            }

                        } else {

                            String date_pattern_str = "[0-9]+/[0-9]+/[0-9]+";


                            Pattern date_pattern = Pattern.compile(date_pattern_str);


                            Matcher date_matcher = date_pattern.matcher(date_time);


                            date_matcher.find();


                            raw_date = date_matcher.group();

                            String date[] = raw_date.split("/");



                            int month = Integer.parseInt(date[0].trim(), 10);
                            int day = Integer.parseInt(date[1].trim(), 10);
                            int year = Integer.parseInt(date[2].trim(), 10);

                            String month_string = " ";
                            switch (month) {
                                case 1:
                                    month_string = "January";
                                    break;
                                case 2:
                                    month_string = "February";
                                    break;
                                case 3:
                                    month_string = "March";
                                    break;
                                case 4:
                                    month_string = "April";
                                    break;
                                case 5:
                                    month_string = "May";
                                    break;
                                case 6:
                                    month_string = "June";
                                    break;
                                case 7:
                                    month_string = "July";
                                    break;
                                case 8:
                                    month_string = "August";
                                    break;
                                case 9:
                                    month_string = "September";
                                    break;
                                case 10:
                                    month_string = "October";
                                    break;
                                case 11:
                                    month_string = "November";
                                    break;
                                case 12:
                                    month_string = "December";
                                    break;
                                default:
                                    month_string = "Invalid month.";
                                    break;
                            }




                            //System.out.println("**GOT A TBA**");
                            date_string = month_string + " " + day + ", " + year;

                            //keys[k][5] = new String(date_string);
                            //keys[k][6] = new String(time_string);



                        }



                    } catch (Exception e) {

                        System.out.println("something went wrong while taking care of the date-time " + e);
                        System.out.println("Using returned date-time");
                        date_string = date_time;
                        time_string = date_time;




                    }
                    return time_string;

         }//end getTimeString
                   static String getDateString(String date_time){

                    //fix time/date here
                    String raw_date = "date";
                    String time_string = "TBA";
                    String date_string = "";
                    try {


                        if (!date_time.contains("TBA")) {
                            String date_pattern_str = "[0-9]+/[0-9]+/[0-9]+";
                            String time_pattern_str = "[0-9]+:[0-9]+";

                            Pattern date_pattern = Pattern.compile(date_pattern_str);
                            Pattern time_pattern = Pattern.compile(time_pattern_str);

                            Matcher date_matcher = date_pattern.matcher(date_time);
                            Matcher time_matcher = time_pattern.matcher(date_time);

                            date_matcher.find();
                            time_matcher.find();

                            raw_date = date_matcher.group();
                            String raw_time = time_matcher.group();
                            String date[] = raw_date.split("/");
                            String time[] = raw_time.split(":");
                            time_string = "";
                            date_string = "";
                            String am_or_pm = "";

                            int hour = Integer.parseInt(time[0].trim(), 10);
                            int min = Integer.parseInt(time[1].trim(), 10);
                            int month = Integer.parseInt(date[0].trim(), 10);
                            int day = Integer.parseInt(date[1].trim(), 10);
                            int year = Integer.parseInt(date[2].trim(), 10);


                            String month_string = " ";
                            switch (month) {
                                case 1:
                                    month_string = "January";
                                    break;
                                case 2:
                                    month_string = "February";
                                    break;
                                case 3:
                                    month_string = "March";
                                    break;
                                case 4:
                                    month_string = "April";
                                    break;
                                case 5:
                                    month_string = "May";
                                    break;
                                case 6:
                                    month_string = "June";
                                    break;
                                case 7:
                                    month_string = "July";
                                    break;
                                case 8:
                                    month_string = "August";
                                    break;
                                case 9:
                                    month_string = "September";
                                    break;
                                case 10:
                                    month_string = "October";
                                    break;
                                case 11:
                                    month_string = "November";
                                    break;
                                case 12:
                                    month_string = "December";
                                    break;
                                default:
                                    month_string = "Invalid month.";
                                    break;
                            }



                            if (date_time.contains("AM")) {
                                am_or_pm = "a.m.";
                                if (min > 9) {
                                    time_string = hour + ":" + min + " " + am_or_pm;
                                } else {
                                    time_string = hour + ":0" + min + " " + am_or_pm;
                                }
                            } else if (date_time.contains("PM")) {
                                am_or_pm = "p.m.";
                                if (min > 9) {
                                    time_string = hour + ":" + min + " " + am_or_pm;
                                } else {
                                    time_string = hour + ":0" + min + " " + am_or_pm;
                                }

                            } else {
                                time_string = "unknown";
                            }


                            date_string = month_string + " " + day + ", " + year;
                            if (time_string.equalsIgnoreCase("3:00 a.m.") || time_string.equalsIgnoreCase("3:30 a.m.")) {

                                time_string = "TBA";
                            //System.out.println("**GOT A 3am TBA**");

                            }

                        } else {

                            String date_pattern_str = "[0-9]+/[0-9]+/[0-9]+";


                            Pattern date_pattern = Pattern.compile(date_pattern_str);


                            Matcher date_matcher = date_pattern.matcher(date_time);


                            date_matcher.find();


                            raw_date = date_matcher.group();

                            String date[] = raw_date.split("/");



                            int month = Integer.parseInt(date[0].trim(), 10);
                            int day = Integer.parseInt(date[1].trim(), 10);
                            int year = Integer.parseInt(date[2].trim(), 10);

                            String month_string = " ";
                            switch (month) {
                                case 1:
                                    month_string = "January";
                                    break;
                                case 2:
                                    month_string = "February";
                                    break;
                                case 3:
                                    month_string = "March";
                                    break;
                                case 4:
                                    month_string = "April";
                                    break;
                                case 5:
                                    month_string = "May";
                                    break;
                                case 6:
                                    month_string = "June";
                                    break;
                                case 7:
                                    month_string = "July";
                                    break;
                                case 8:
                                    month_string = "August";
                                    break;
                                case 9:
                                    month_string = "September";
                                    break;
                                case 10:
                                    month_string = "October";
                                    break;
                                case 11:
                                    month_string = "November";
                                    break;
                                case 12:
                                    month_string = "December";
                                    break;
                                default:
                                    month_string = "Invalid month.";
                                    break;
                            }




                            //System.out.println("**GOT A TBA**");
                            date_string = month_string + " " + day + ", " + year;

                            //keys[k][5] = new String(date_string);
                            //keys[k][6] = new String(time_string);



                        }



                    } catch (Exception e) {

                        System.out.println("something went wrong while taking care of the date-time " + e);
                        System.out.println("Using returned date-time");
                        date_string = date_time;
                        time_string = date_time;


                    }
                    return date_string;

         }//end getDateString
                   static String[] getTeams(String name){

                        String teams[] = name.split("(?i)vs.");
                        if(teams.length < 2){
                            teams = name.split("(?i)vs");
                        }
                       
                        return teams;

                    }//end getTeams
                   public static String replaceSpaces(String s, String r) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";
        boolean beginning = true;
        while (st.hasMoreElements()) {
            if (beginning) {
                t += st.nextElement();
                beginning =
                        false;
            } else {
                t += r;
                t +=
                        st.nextElement();
            }

        }
        return t;
    }//end of removeSpaces
                   public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";

        while (st.hasMoreElements()) {

            t += st.nextElement();



        }

        return t;
    } //end of removeSpaces
                   public static String key2UrlFilename(String key, String url_filename_space, boolean useLowerCaseFilenames) {


        Pattern r = Pattern.compile("[\\p{Space}]+");
        Matcher m = r.matcher(key.trim());
        //first, set the filename equal to the key with all spaces replaced by the spacer
        if (url_filename_space == null || url_filename_space.compareTo("") == 0) {
            url_filename_space = " ";
        }

        String filename = m.replaceAll(url_filename_space);
        //take care of some chars that shouldn't be in the filename
        filename =
                filename.replace('/', url_filename_space.charAt(0));
        filename =
                filename.replace('.', url_filename_space.charAt(0));
        filename =
                filename.replace(',', url_filename_space.charAt(0));
        filename =
                filename.replace('`', url_filename_space.charAt(0));
        
        filename = filename.replace("\'s", "s");
        filename = filename.replace("\'", "");
        //filename =
          //      filename.replace('&', url_filename_space.charAt(0));
        filename = replaceStringWithString(filename, "&", "and");
        filename = filename.replace('\\', url_filename_space.charAt(0));
        filename = filename.replace('"', url_filename_space.charAt(0));
        filename =
                filename.replace('(', url_filename_space.charAt(0));
        filename =
                filename.replace(')', url_filename_space.charAt(0));
        filename =
                filename.replace(':', url_filename_space.charAt(0));
        filename =
                filename.replace('$', url_filename_space.charAt(0));
        filename =
                filename.replace('?', url_filename_space.charAt(0));
        filename =
                filename.replace('!', url_filename_space.charAt(0));
        //handle some others by choice
        //finally add .extension, usually one of: .htm, .html, .asp or .jsp

        String spacer = url_filename_space;
       filename = filename.replaceAll(spacer + "[" + spacer + "]+", spacer);
        //remove . or the filename space if it's the first char to prevent generation of hidden files (sometimes)
        if (url_filename_space.compareTo(" ") == 0) {
            //filename = removeSpaces(filename);
            //url_filename_space ="";
        } else {
            if (filename.charAt(0) == '.' || filename.charAt(0) == url_filename_space.charAt(0)) {
                filename = filename.substring(1, filename.length());
            }
            if(filename.length() > 0)
            if (url_filename_space.charAt(0) == filename.charAt(filename.length()-1)){
                filename = filename.substring(0, filename.length()-1);
            }
        }
        if (useLowerCaseFilenames) {
            filename = filename.toLowerCase();
        }
        
        return filename.trim();


    }
                   public static String key2UrlDir(String key, String url_filename_space,boolean useLowerCaseDirnames) {

        Pattern r = Pattern.compile("[\\p{Space}]+");
        Matcher m = r.matcher(key.trim());
        if (url_filename_space == null || url_filename_space.compareTo("") == 0) {
            url_filename_space = " ";
        }

        String dirname = m.replaceAll(url_filename_space);

        
        dirname =
                dirname.replace('\'', url_filename_space.charAt(0));

        dirname =
                dirname.replace('/', url_filename_space.charAt(0));

        dirname =
                dirname.replace('\'', url_filename_space.charAt(0));
        dirname =
                dirname.replace('`', url_filename_space.charAt(0));
        dirname =
                dirname.replace('-', url_filename_space.charAt(0));
        dirname = dirname.replace("\'s", "s");
        dirname = dirname.replace("\'","");
        
       
        //dirname = replaceStringWithString(dirname, "&", "And");
        dirname =
                dirname.replace('&', url_filename_space.charAt(0));
        dirname =
                dirname.replace('.', url_filename_space.charAt(0));
        dirname =
                dirname.replace(',', url_filename_space.charAt(0));
        dirname =
                dirname.replace(':', url_filename_space.charAt(0));
        dirname =
                dirname.replace('\\', url_filename_space.charAt(0));
        dirname = dirname.replace('"', url_filename_space.charAt(0));
        dirname =
                dirname.replace('(', url_filename_space.charAt(0));
        dirname =
                dirname.replace(')', url_filename_space.charAt(0));
        dirname =
                dirname.replace('$', url_filename_space.charAt(0));
        dirname =
                dirname.replace('?', url_filename_space.charAt(0));
        dirname =
                dirname.replace('!', url_filename_space.charAt(0));
        //dirname = dirname.replaceAll("-[-]+", "-");
        String spacer = url_filename_space;
        dirname =
                dirname.replaceAll(spacer + "[" + spacer + "]+", spacer);

        if (url_filename_space.compareTo(" ") == 0) {
            //dirname = removeSpaces(dirname);
            //url_filename_space ="";
        } else {
            if (dirname.charAt(0) == '.' || dirname.charAt(0) == url_filename_space.charAt(0)) {
                dirname = dirname.substring(1, dirname.length());
            }
            if(dirname.length() > 0)
            if (dirname.charAt(dirname.length()-1) == url_filename_space.charAt(0)) {
                dirname = dirname.substring(0, dirname.length()-1);
            }

        }
        if (useLowerCaseDirnames) {
            dirname = dirname.toLowerCase();
        }

        return dirname.trim();


    }
   
    
    } //end of jdata class
package BigQuery;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static String teradata_datechars="(?i)(MMMM|M4|MMM|M3|MM|DDD|D3|DD|YYYY|Y4|YY|EEEE|E4|EEE|E3|HH|MI|SS|T)";
    public static String teradata_datebreakchars="([/]|[Bb]|[,]|[']|[:]|[.]|[-]|[h]|[m]|[s]|[\\s]+)";

    public static void main(String[] args) {
        String inputformat="MM/DD/YYYY";
        int exitcode= 0;
        List<String> dateformattokens= Arrays.asList(inputformat.split(teradata_datebreakchars));
        String outputformat=inputformat;

        for(String token : dateformattokens){
            String inptext=token;
            String outputtext="";
            while(inptext.length()>0){
                Pattern p= Pattern.compile("^"+teradata_datechars);
                Matcher m=p.matcher(inptext);
                if(m.find()){
                    String match=m.group();
                    System.out.println("Match : "+match);
                    inptext=inptext.substring(m.end());
                    //get the map for bigquery
                    outputtext=outputtext+translateTeradataBQdateliteral(match);
                }else{
                    break;
                }
            }
            if(inptext.length()>0){
                System.out.println("Couldnt match");
                exitcode=1;
            }else{
                System.out.println("Token matched");
                outputformat=outputformat.replaceFirst(token,outputtext);
            }
        };
        if(exitcode==1){
            System.out.println("Conversion failed");
        }else{
            System.out.println("Output format "+outputformat);
        }
    }


    public static String translateTeradataBQdateliteral(String teradataliteral){
        switch (teradataliteral){
            case "MMMM" : return "%B";
            case "M4" : return "%B";
            case "MMM" : return "%b";
            case "M3" : return "%b";
            case "MM" : return "%m";
            case "M2" : return "%m";
            case "DDD" : return "%j";
            case "DD" : return "%d";
            case "YYYY" : return "%Y";
            case "Y4" : return "%Y";
            case "YY" : return "%y";
            case "EEEE" : return "%w";
            case "E4" : return "%w";
            case "EEE" : return "%w";
            case "E3" : return "%w";
            case "HH" : return "%H";
            case "MI" : return "%M";
            case "SS" : return "%S";
            case "T" : return "%r";
            default : return teradataliteral;
        }
    }

}

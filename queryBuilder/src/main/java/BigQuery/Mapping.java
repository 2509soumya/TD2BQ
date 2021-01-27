package BigQuery;

import org.json.JSONArray;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapping {
    Map<String, String> data = new HashMap<String, String>();
    HashMap map = new HashMap();

    public static String teradata_datechars="(?i)(MMMM|M4|MMM|MON|M3|MM|DDD|D3|DD|YYYY|Y4|YY|EEEE|E4|EEE|E3|HH|MI|SS|T)";
    public static String teradata_datebreakchars="([/]|[Bb]|[,]|[']|[:]|[.]|[-]|[h]|[m]|[s]|[\\s]+)";

    public String getBigQueryKeywordFrom(String teraFunc)
    {
        if(teraFunc.startsWith("char"))
        {
            teraFunc="char";
        }
        map.put("BITAND","BIT_AND");
        map.put("BITNOT","~");
        map.put("ADD_MONTHS","DATE_ADD");
        map.put("TO_DATE","PARSE_DATE");
        map.put("SUBSTRING","SUBSTR");
        map.put("RANDOM","RAND");
        map.put("ZEROIFNULL","IFNULL");
        map.put("NULLIFZERO","NULLIF");


        String bqfun="";
        try {
            bqfun=map.get(teraFunc).toString();
        }
        catch(Exception ex)
        {
            bqfun=teraFunc;
        }
        return bqfun;
    }

    public static String getBigQueryFunc(String funcname, JSONArray arguments) throws IOException {
        ExpressionBuilder eb = new ExpressionBuilder();
        List<String> argumentarr=new ArrayList<>();
        for (int j = 0; j < arguments.length(); j++) {
            JSONArray arrayArg = arguments.getJSONArray(j);
            String eachArgument = "";
            eachArgument = eachArgument + eb.Expression(arrayArg);
            argumentarr.add(eachArgument);
        }

        String finalvalue;
        switch(funcname.toUpperCase()){
            case "SUBSTRING" :  finalvalue="SUBSTR"+ "("+String.join(",",argumentarr)+")";break;
            case "ZEROIFNULL" : argumentarr.add("0");
                                finalvalue="COALESCE"+ "("+String.join(",",argumentarr)+")";break;
            case "ADD_MONTHS" : String monthaddfctr=argumentarr.get(argumentarr.size()-1);
                                String addmonthstr="INTERVAL "+monthaddfctr+" MONTH";
                                argumentarr.set(argumentarr.size()-1,addmonthstr);
                                finalvalue="DATE_ADD"+ "("+String.join(",",argumentarr)+")";break;
            case "TO_CHAR" :    if(argumentarr.size()>1){
                                   String inpformat=convertTeradataDateFormatToBQ(argumentarr.get(argumentarr.size()-1));
                                   if(inpformat.equalsIgnoreCase("FAIL")){
                                       finalvalue=funcname + "("+String.join(",",argumentarr)+")";
                                   }else{
                                       argumentarr.set(argumentarr.size()-1,inpformat);
                                       Collections.reverse(argumentarr);
                                       finalvalue="FORMAT_DATETIME"+ "("+String.join(",",argumentarr)+")";
                                   }
                                }else{
                                      finalvalue=funcname + "("+String.join(",",argumentarr)+")";
                                }
                                break;
            default : finalvalue=funcname + "("+String.join(",",argumentarr)+")";
        }
        return finalvalue;
    }

    public static String translateTeraDataToBigQDateFormat(String teradata_dateformat){
         String inputformat=teradata_dateformat;
         String outputformat="";

         List<String> dateformats= Arrays.asList(inputformat.split(teradata_datebreakchars));
         dateformats.forEach(a-> System.out.println("Dateformat: "+a));
         return "";
    }

    public static String getBigQueryDataType(String datatype)
    {
        if(datatype.matches("(?i)CHAR[\\s]*[(][\\s]*[\\d]+[\\s]*[)]")){
            datatype="CHAR";
        }
        switch(datatype){
            case "CHAR": return "STRING";
            case "DECIMAL" : return "NUMERIC";
            default : return datatype;
        }
    }

    public static String convertTeradataDateFormatToBQ(String inputformat){
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
                exitcode=1;
            }else{
                outputformat=outputformat.replaceFirst(token,outputtext);
            }
        };
        if(exitcode==1){
            System.out.println("Conversion failed");
            return "FAIL";
        }else{
            System.out.println("Output format "+outputformat);
            return outputformat;
        }
    }

    public static String translateTeradataBQdateliteral(String teradataliteral){
        switch (teradataliteral){
            case "MMMM" : return "%B";
            case "M4" : return "%B";
            case "MMM" : return "%b";
            case "MON" : return "%b";
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


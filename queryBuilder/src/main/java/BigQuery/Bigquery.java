package BigQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class Bigquery {


    public String Read() throws java.io.IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get("/Users/s0r0282/eclipse-workspace/queryBuilder/src/main/resources/testjson.json"), StandardCharsets.US_ASCII)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();

    }

    public String objectParser(String s) throws IOException {
        TableBuilder tb = new TableBuilder();
        String query = "SELECT ";
        String resulTQuery = "";
        JSONArray jsonArray1 = new JSONArray(s);
        JSONObject jsonObject=jsonArray1.getJSONObject(0).getJSONObject("selectionobject");
        JSONArray jsonArrayColumns;

        List<String> aggfuncs=new ArrayList<>();
        if(jsonObject.getJSONArray("columnaggregate").length()>0)
        {
            for(int i=0;i<jsonObject.getJSONArray("columnaggregate").length();i++)
            {
                aggfuncs.add(jsonObject.getJSONArray("columnaggregate").getJSONObject(0).get("aggproj").toString());
            }
        }
        query=query+" "+String.join(",",aggfuncs)+" ";

        jsonArrayColumns = jsonObject.getJSONArray("columns");
        ExpressionBuilder eb = new ExpressionBuilder();
        for (int i = 0; i < jsonArrayColumns.length(); i++) {
            String colalias = "";
            JSONArray expression = jsonArrayColumns.getJSONObject(i).getJSONArray("expression");
            try {
                colalias = jsonArrayColumns.getJSONObject(i).get("colalias").toString();
            } catch (Exception e) {
                colalias = "";
            }
            String express = "";

            express = express + eb.Expression(expression);
            if (colalias.equals("")) {
                if (i == jsonArrayColumns.length() - 1) {
                    resulTQuery = resulTQuery + express;
                } else {
                    resulTQuery = resulTQuery + express + ",";
                }
            } else {
                if (i == jsonArrayColumns.length() - 1) {
                    resulTQuery = resulTQuery + express + " AS " + colalias;
                } else {
                    resulTQuery = resulTQuery + express + " AS " + colalias + " , ";
                }
            }
        }
        String from="";
        JSONArray tableArray = jsonObject.getJSONArray("tables");
        if(tableArray.length()>0)
            from = " FROM ";

        for (int i = 0; i < tableArray.length(); i++) {
               /* if(i==tableArray.length()-1)
                {
                    from = from + tb.TableBuilder(tableArray.getJSONObject(i));
                }
                else {
                    from = from + tb.TableBuilder(tableArray.getJSONObject(i)) + " " + "," + " ";
                }*/
            from = from + tb.TableBuilder(tableArray.getJSONObject(i));
        }
        JSONArray predicates = jsonObject.getJSONArray("predicates");
        String where = " WHERE ";
        String pred = "";
        Predicatebulider keywordsHandler = new Predicatebulider();
        pred=keywordsHandler.handlePred(predicates);
        if(pred.equals(""))
        {
            where="";
        }
        else {
            where = where + pred;
        }
        JSONArray groupAgregates = jsonObject.getJSONArray("aggregates");
        String groupAggregate="";
        Aggregate agg= new Aggregate();
        for(int i=0;i<groupAgregates.length();i++)
        {
            String args =agg.buildAggregate(jsonObject.getJSONArray("aggregates").getJSONObject(i).getJSONArray("arguments"));
            String func=jsonObject.getJSONArray("aggregates").getJSONObject(i).getString("func");
            groupAggregate=groupAggregate+" "+func+" "+args+" ";
        }
        return query + resulTQuery+from+where+groupAggregate;
    }

    public static void main(String args[]) throws IOException {
        Bigquery bigquery = new Bigquery();
        String s = bigquery.Read();
        FileWriter fw =new FileWriter("newquery.txt");
        fw.write(bigquery.objectParser(s));
        fw.close();
        System.out.println(bigquery.objectParser(s));
    }
}

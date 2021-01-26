package BigQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FunctionImpl {

    String gres = "";
    LiteralImpl literal = new LiteralImpl();
    Mapping mp= new Mapping();



    public String parser(JSONObject fundef) throws IOException {
        ExpressionBuilder eb = new ExpressionBuilder();
        String functionName = fundef.get("func_name").toString();
        if (functionName.equalsIgnoreCase("cast")) {
            JSONArray functionArgs = fundef.getJSONArray("arguments");
            String res = "";
            for (int i = 0; i < functionArgs.length(); i++) {
                JSONObject js = functionArgs.getJSONObject(i);
                JSONArray expression = js.getJSONArray("expression");
                String finalExpression = "";

                finalExpression = finalExpression + eb.Expression(expression);
                String keyword="";
                if(js.getJSONObject("datatype").has("keyword"))
                {
                keyword=js.getJSONObject("datatype").getString("keyword");
                keyword=Mapping.getBigQueryDataType(keyword);
                    res = functionName + "(" + finalExpression + " AS " + keyword + ")";
                }
                else {
                    String func_name = js.getJSONObject("datatype").get("func_name").toString();
                    JSONArray argument = js.getJSONObject("datatype").getJSONArray("arguments");
                    StringBuilder func_val = new StringBuilder();
                    for (int j = 0; j < argument.length(); j++) {
                        String temp = literal.literalBuilder(argument.getJSONArray(j).getJSONObject(0).getJSONObject("def"));
                        if (j != argument.length() - 1) {
                            func_val.append(temp).append(",");
                        } else {
                            func_val.append(temp);
                        }
                    }
                    func_name=mp.getBigQueryKeywordFrom(func_name);
                    res = functionName + "(" + finalExpression + " AS " + func_name + "(" + func_val + ")" + ")";
                }
            }
            gres = res;
        } else {
            String fun_name = fundef.get("func_name").toString();
            JSONArray arguments = fundef.getJSONArray("arguments");
            gres = Mapping.getBigQueryFunc(fun_name,arguments);
        }
        return gres;
    }
}

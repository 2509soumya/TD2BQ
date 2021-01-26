package BigQuery;

import org.json.JSONArray;

import java.io.IOException;

public class ExpressionBuilder {
    public String Expression(JSONArray expression) throws IOException {
        String stoadd = "";
        StringBuilder express = new StringBuilder();
        FunctionImpl function = new FunctionImpl();
        EntityImpl entity = new EntityImpl();
        LiteralImpl literal = new LiteralImpl();
        Bigquery bq= new Bigquery();
        ExpressionBuilder eb= new ExpressionBuilder();
        for (int j = 0; j < expression.length(); j++) {
            if(expression.getJSONObject(j).getString("expr_type").equals("operand") && expression.getJSONObject(j).getString("type").equals("case_when")) {
                JSONArray then;
                JSONArray when;
                JSONArray esel;
                JSONArray casearray = expression.getJSONObject(j).getJSONArray("def");
                String thenpart = "";
                String whenpart = "";
                String elsepart="";
                String finalpart="";
                String s= " CASE ";
                String res="";
                for (int l = 0; l < casearray.length(); l++) {
                    int len = expression.getJSONObject(j).getJSONArray("def").getJSONObject(l).length();
                    if (len == 2) {
                        then = expression.getJSONObject(j).getJSONArray("def").getJSONObject(l).getJSONArray("then");
                        thenpart = thenpart + Expression(then);
                        thenpart = " THEN " + thenpart;

                        when = expression.getJSONObject(j).getJSONArray("def").getJSONObject(l).getJSONArray("when");
                        Predicatebulider pb = new Predicatebulider();
                        whenpart = whenpart + pb.handlePred(when);
                        whenpart = " WHEN " + whenpart;

                    }
                else {
                        esel = expression.getJSONObject(j).getJSONArray("def").getJSONObject(l).getJSONArray("else");
                            elsepart=elsepart+Expression(esel);
                        elsepart=" ELSE "+elsepart;
                        }

                    finalpart=" "+whenpart +thenpart+elsepart;
                    elsepart="";
                    thenpart="";
                    whenpart="";
                    s=s+finalpart;
                    }
                stoadd=stoadd+s+" END";
            }
            else if (expression.getJSONObject(j).getString("expr_type").equals("operator")) {
                stoadd = expression.getJSONObject(j).getString("operator");
                stoadd=" "+ stoadd+ " ";
            }
            else {
                String type = expression.getJSONObject(j).getString("type");
                if (type.equals("function")) {
                    stoadd = function.parser(expression.getJSONObject(j).getJSONObject("def"));
                } else if (type.equals("entity")) {
                    stoadd = entity.entityBuilder(expression.getJSONObject(j).getJSONObject("def"));
                } else if (type.equals("literal")) {
                    stoadd = literal.literalBuilder(expression.getJSONObject(j).getJSONObject("def"));
                }
                else if(type.equals("expression")) {
                    stoadd="("+eb.Expression(expression.getJSONObject(j).getJSONArray("def"))+")";
                }
                else if(type.equals("keyword")) {
                    stoadd=expression.getJSONObject(j).getJSONObject("def").getString("key");
                }
                else if(type.equals("extractfunc"))
                {
                    String tmpfun=eb.Expression(expression.getJSONObject(j).getJSONObject("def").getJSONArray("dateexpression"));
                    stoadd="EXTRACT("+expression.getJSONObject(j).getJSONObject("def").getString("datekeyword")+" from "+tmpfun+")";

                }
                else if(type.equals("selectentity"))
                {
                        stoadd="("+bq.objectParser(expression.getJSONObject(j).get("def").toString())+")";

                }
            }
            String named_additional="";
            String format_additional="";
            String cast_additional="";

            if(expression.getJSONObject(j).has("namedalias"))
            {
                named_additional=" "+expression.getJSONObject(j).getString("namedalias")+" ";
            }
            if(expression.getJSONObject(j).has("format"))
            {
                format_additional=" "+" FORMAT "+expression.getJSONObject(j).getString("format")+" ";
            }

            if(expression.getJSONObject(j).has("castdatatype"))
            {
                String castdttype=Mapping.getBigQueryDataType(expression.getJSONObject(j).getString("castdatatype"));
                String finalexpress="CAST("+stoadd+" AS "+castdttype+")";
                express.append(finalexpress);
            }else{
                express.append(stoadd);
            }
        }
        return express.toString();
    }
    public String FromExpress(JSONArray expression) throws IOException {
        String stoadd = "";
        StringBuilder express = new StringBuilder();
        for (int j = 0; j < expression.length(); j++) {
            if (expression.getJSONObject(j).getString("expr_type").equals("operator")) {
                stoadd = expression.getJSONObject(j).getString("operator");
                stoadd=" "+stoadd+" ";
            } else {
                    stoadd = Expression(expression.getJSONObject(j).getJSONArray("expression"));
            }
            express=express.append(stoadd);
        }
        return express.toString();
    }
}

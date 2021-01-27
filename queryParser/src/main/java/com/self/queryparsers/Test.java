package com.self.queryparsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class Test {	

	/*
	public static void main(String[] args) {
		String teradatadatatypes="(ARRAY(?![\\w]+)|VARRAY(?![\\w]+)|BYTE(?![\\w]+)|VARBYTE(?![\\w]+)|BLOB(?![\\w]+)|CHAR[(][\\d]*[)](?![\\w]+)|VARCHAR(?![\\w]+)|CLOB(?![\\w]+)|AVRO(?![\\w]+)|DATE(?![\\w]+)|TIME(?![\\w]+)|TIMESTAMP(?![\\w]+)|INTERVAL YEAR(?![\\w]+)|INTERVAL YEAR TO MONTH(?![\\w]+)|INTERVAL MONTH(?![\\w]+)|INTERVAL DAY(?![\\w]+)|INTERVAL DAY TO HOUR(?![\\w]+)|INTERVAL DAY TO MINUTE(?![\\w]+)|INTERVAL DAY TO SECOND(?![\\w]+)|INTERVAL HOUR(?![\\w]+)|INTERVAL HOUR TO MINUTE(?![\\w]+)|INTERVAL HOUR TO SECOND(?![\\w]+)|INTERVAL MINUTE(?![\\w]+)|INTERVAL MINUTE TO SECOND(?![\\w]+)|INTERVAL SECOND(?![\\w]+)|JSON(?![\\w]+)|BYTEINT(?![\\w]+)|SMALLINT(?![\\w]+)|INTEGER(?![\\w]+)|BIGINT(?![\\w]+)|DECIMAL([(][^()]*[)])|NUMERIC(?![\\w]+)|NUMBER(?![\\w]+)|PERIOD(?![\\w]+)|XML(?![\\w]+))";
		String formatdatatyperegex="^(?i)[(]("+teradatadatatypes+"),"+"[\\s]*FORMAT[\\s]+([\'][^\']+[\'])"+"[\\s]*[)]";	
		String query="(DATE, FORMAT 'MM/DD/YYYY')";
        if(query.matches(formatdatatyperegex+"(.*)")) {
	        Pattern p=Pattern.compile(formatdatatyperegex);
			Matcher m=p.matcher(query);
			if(m.find()) {
				System.out.println(m.group(4));
				System.out.println(m.group(2));
			}
		}
		
	}*/

	public static void main(String[] args) {
		String query="SELECT CAST('WALMART' AS CHAR(20)) TENANT,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"UPC                                   (CHAR(40)) UPC,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"TOTAL_OH        (FORMAT '-ZZ,ZZZ,ZZZ,ZZZ,ZZ9.9999')\n" +
				"(CHAR(24)) \"TOTAL_OH\",\n" +
				"'|'                                   (CHAR(01)),\n" +
				"IS_PARENT                             (CHAR(01)) IS_PARENT,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"CATALOG_ITEM_ID (FORMAT '-Z(9)9')     (CHAR(11)) ITEM_ID,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"DESCRIPTION                           (CHAR(1600)) DESCRIPTION,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"DIVISION_NAME                         (CHAR(50)) DIVISION_NAME,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"SUPER_DEPARTMENT_NAME                 (CHAR(50))\n" +
				"SUPER_DEPARTMENT_NAME,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"DEPT                                  (CHAR(50)) DEPT,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"CAT                                   (CHAR(50)) CAT,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"SUBSTR(SUB_CAT,0,50)                   (CHAR(50)) \"SUB_CAT\",\n" +
				"'|$'                                  (CHAR(02)),\n" +
				"AMT_ITEM_PRICE  (FORMAT '-ZZ,ZZZ,ZZZ,ZZ9.9999999')\n" +
				"(CHAR(23)) \"CURRENT_RETAIL\",\n" +
				"'|$'                                  (CHAR(02)),\n" +
				"COST            (FORMAT '-ZZ,ZZZ,ZZZ,ZZ9.9999999')\n" +
				"(CHAR(23)) \"ITEM_COST\",\n" +
				"'|$'                                  (CHAR(02)),\n" +
				"EXT_COST        (FORMAT '-ZZ,ZZZ,ZZZ,ZZ9.9999999')\n" +
				"(CHAR(23)) \"EXT_COST\",\n" +
				"'|'                                   (CHAR(01)),\n" +
				"RESERVE         (FORMAT '-ZZ,ZZZ,ZZZ,ZZZ,ZZ9.9999')\n" +
				"(CHAR(24)) RESERVE,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"STYLE_ID_NUM                          (CHAR(50)) \"STYLE_ID_NUM\",\n" +
				"'|'                                   (CHAR(01)),\n" +
				"ATTRIBUTE_VALUE_1                     (CHAR(4000)) COLOR,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"ATTRIBUTE_VALUE_2                     (CHAR(4000)) \"SIZE\",\n" +
				"'|'                                   (CHAR(01)),\n" +
				"WEIGHT          (FORMAT '-ZZ,ZZZ,ZZZ,ZZ9.9999999')\n" +
				"(CHAR(23)) WEIGHT,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"''                                    (CHAR(01)) BUNDLE_ID,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"IS_CON                                (CHAR(01)) IS_CON,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"IS_S2S_ELIGIBLE                       (CHAR(01)) IS_S2S_ELIGIBLE,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"IS_S2S_ACTIVE                         (CHAR(01)) IS_S2S_ACTIVE,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"IS_BACKHAUL_ELIGIBLE                  (CHAR(01))\n" +
				"IS_BACKHAUL_ELIGIBLE,\n" +
				"'|'                                   (CHAR(01)),\n" +
				"TO_CHAR(END_DATE,'MM/DD/YYYY')        (CHAR(10)) END_DATE,\n" +
				"'|'                                   (CHAR(01))\n" +
				"FROM\n" +
				"(\n" +
				"SELECT Y.UPC,\n" +
				"SUM(TOTAL_OH) TOTAL_OH,\n" +
				"CASE WHEN Y.UPC=Y.PARENT_UPC THEN 'Y' ELSE 'N' END IS_PARENT,\n" +
				"Y.CATLG_ITEM_ID CATALOG_ITEM_ID,\n" +
				"DESCRIPTION DESCRIPTION,\n" +
				"DIVISION_NAME DIVISION_NAME,\n" +
				"SUPER_DEPARTMENT_NAME SUPER_DEPARTMENT_NAME,\n" +
				"DEPT,\n" +
				"CAT,\n" +
				"SUB_CAT,\n" +
				"AMT_ITEM_PRICE,\n" +
				"MAX(COST) COST,\n" +
				"SUM(EXT_COST) EXT_COST,\n" +
				"SUM(RESERVE) RESERVE,\n" +
				"STYLE_ID_NUM STYLE_ID_NUM,\n" +
				"ATTRIBUTE_VALUE_1,\n" +
				"ATTRIBUTE_VALUE_2,\n" +
				"WEIGHT,\n" +
				"CASE WHEN INBOUND_ITEM_CLASS = 'C' THEN 'Y' ELSE 'N' END AS IS_CON,\n" +
				"CASE WHEN S2S_IND=1 THEN 'Y' ELSE 'N' END AS IS_S2S_ELIGIBLE,\n" +
				"CASE WHEN S2S_ACTV_IND=1 THEN 'Y' ELSE 'N' END AS  IS_S2S_ACTIVE,\n" +
				"CASE WHEN BACKHAUL_ELIG_IND=1 THEN 'Y' ELSE 'N' END AS\n" +
				"IS_BACKHAUL_ELIGIBLE,\n" +
				"PARENT_UPC PARENT_UPC,\n" +
				"Y.END_DATE\n" +
				"FROM\n" +
				"(\n" +
				"SELECT UPC,\n" +
				"PARENT_UPC,\n" +
				"PROD_KEY,\n" +
				"OH.CATLG_ITEM_ID,\n" +
				"WM_ITEM_NUM,\n" +
				"PROD_NM AS DESCRIPTION,\n" +
				"DIV_NM AS DIVISION_NAME,\n" +
				"SUP_DEPT_NM AS SUPER_DEPARTMENT_NAME,\n" +
				"DEPT_NM AS DEPT,\n" +
				"CATEG_NM AS CAT,\n" +
				"SUB_CATEG_NM AS SUB_CAT,\n" +
				"PROD_WT AS WEIGHT,\n" +
				"INBOUND_ITEM_CLASS,\n" +
				"OH.COLOR ATTRIBUTE_VALUE_1,\n" +
				"OH.SIZE ATTRIBUTE_VALUE_2,\n" +
				"STYLE_ID_NUM,\n" +
				"SUM(SUM_OH) TOTAL_OH,\n" +
				"SUM(RTL_PRICE) SUM_AMT_ITEM_PRICE,\n" +
				"AVG(RTL_PRICE) RETAIL_ITEM_PRICE,\n" +
				"SUM(COST) SUM_COST,\n" +
				"AVG(COST) COST,\n" +
				"SUM(EXT_COST) EXT_COST,\n" +
				"MIN(GLBL_AVAIL.RESERVED_QTY) RESERVE,\n" +
				"AVG(RTL_PRICE) AMT_ITEM_PRICE,\n" +
				"MAX(CAST(TO_CHAR(OH.OFFR_END_TS,'YYYY-MM-DD') AS DATE)) END_DATE\n" +
				"FROM wmt-edw-prod.WW_GEC_VM.INVT_INB_ON_HAND_DTL OH\n" +
				"LEFT JOIN (SELECT CATLG_ITEM_ID,LEGACY_SLR_ID,MIN(AVLBL_STS)\n" +
				"AVLBL_STS,MIN(RESERVED_QTY) RESERVED_QTY FROM\n" +
				"wmt-edw-prod.WW_GEC_VM.INVT_GLBL_AVLBL GLBL\n" +
				"LEFT JOIN wmt-edw-prod.WW_GEC_VM.INVT_ITEM_LKP ITEM_LKP ON\n" +
				"GLBL.PROD_OFFR_ID=ITEM_LKP.SRC_ITEM_ID\n" +
				"LEFT JOIN (SELECT INVT_ITEM_KEY,TENANT_ORG_ID,\n" +
				"SUM(CASE WHEN INVT_TYP_ID=30 THEN QTY ELSE 0 END )\n" +
				"RESERVED_QTY FROM wmt-edw-prod.WW_GEC_VM.INVT_OUTBOUND_SUMM\n" +
				"WHERE TENANT_ORG_ID=4571 AND INVT_TYP_ID=30\n" +
				"GROUP BY INVT_ITEM_KEY,TENANT_ORG_ID )SUMM ON\n" +
				"ITEM_LKP.INVT_ITEM_KEY=SUMM.INVT_ITEM_KEY\n" +
				"WHERE GLBL.CHNL='S2H' AND GLBL.LEGACY_SLR_ID=0\n" +
				"GROUP BY 1,2) GLBL_AVAIL ON\n" +
				"OH.CATLG_ITEM_ID=GLBL_AVAIL.CATLG_ITEM_ID\n" +
				"WHERE RPT_DT=CURRENT_DATE - 1\n" +
				"GROUP BY UPC,\n" +
				"PARENT_UPC,\n" +
				"PROD_KEY,\n" +
				"OH.CATLG_ITEM_ID,\n" +
				"WM_ITEM_NUM,\n" +
				"PROD_NM,\n" +
				"DIV_NM,\n" +
				"SUPER_DEPARTMENT_NAME,\n" +
				"DEPT_NM,\n" +
				"CATEG_NM,\n" +
				"SUB_CATEG_NM,\n" +
				"PROD_WT,\n" +
				"INBOUND_ITEM_CLASS,\n" +
				"STYLE_ID_NUM,\n" +
				"OH.COLOR,\n" +
				"OH.SIZE ) Y\n" +
				"LEFT JOIN wmt-edw-prod.WW_GEC_VM.PROD_TRANSIENT_MASTER PTM ON\n" +
				"PTM.CATLG_ITEM_ID=Y.CATLG_ITEM_ID\n" +
				"GROUP BY UPC ,\n" +
				"Y.CATLG_ITEM_ID,\n" +
				"PARENT_UPC ,\n" +
				"DESCRIPTION ,\n" +
				"STYLE_ID_NUM ,\n" +
				"ATTRIBUTE_VALUE_1 ,\n" +
				"ATTRIBUTE_VALUE_2 ,\n" +
				"DIVISION_NAME ,\n" +
				"SUPER_DEPARTMENT_NAME ,\n" +
				"DEPT ,\n" +
				"CAT ,\n" +
				"SUB_CAT ,\n" +
				"AMT_ITEM_PRICE ,\n" +
				"INBOUND_ITEM_CLASS ,\n" +
				"WEIGHT ,\n" +
				"S2S_IND ,\n" +
				"S2S_ACTV_IND ,\n" +
				"BACKHAUL_ELIG_IND ,\n" +
				"Y.END_DATE\n" +
				") RT\n" +
				"ORDER BY TENANT DESC,UPC,ITEM_ID";

		query=query.replaceAll("\n"," ");
		System.out.println(query);
	}





}

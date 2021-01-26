package com.self.queryconverter.controller;

import BigQuery.Bigquery;
import com.self.queryparsers.QueryParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


@RestController
@RequestMapping(value = "teradataparser/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParserController {

	@RequestMapping(value = "parse", method = RequestMethod.POST)
	public ResponseEntity<String>  generateQuery(@RequestBody String query) throws IOException {
	  System.out.println("Formulating parsed json ..");
	  QueryParser parser=new QueryParser(query);
	  JSONObject response=parser.parse();
	  String message=response.getString("message");
	  if(message.contains("Successfully")){
		  Bigquery bigquery = new Bigquery();
		  String convertedquery=bigquery.objectParser(response.getJSONArray("parsedobject").toString());
		  System.out.println("Converted query : "+convertedquery);
		  response.remove("parsedobject");
		  response.put("convertedquery",convertedquery);
		  response.put("message","Successfully converted query");
	  }
	  return new ResponseEntity<>(response.toString(), HttpStatus.OK);
	}
}

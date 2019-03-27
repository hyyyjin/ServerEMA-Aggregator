package com.mir.ven.simulator;

import java.awt.Toolkit;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.mir.ems.globalVar.global;
import com.mir.ven.VENImpl;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHandler.STATE;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import com.ning.http.client.filter.FilterContext;
import com.ning.http.client.filter.FilterContext.FilterContextBuilder;
import com.ning.http.client.filter.FilterException;
import com.ning.http.client.filter.ResponseFilter;

public class TEST {

	public static <T> void main(String[] args) throws InterruptedException, IllegalArgumentException,
			ParserConfigurationException, SAXException, TransformerException {

		global.vtnURL = "http://166.104.28.51:8080/OpenADR2/Simple/2.0b/";

		String subPath = "EiRegisterParty";
		for (int i = 0; i < 1000; i++) {
			AsyncHttpClient client = new AsyncHttpClient();
			try {

				client.preparePost(global.vtnURL + subPath)
						.setBody(new VENImpl().QueryRegistration("ba4d8f5c0a").getBytes())
						.setHeader("Content-Type", "application/xml").execute();

			} catch (IOException e) {

			}

		}

		AsyncHttpClientConfig.Builder b = new AsyncHttpClientConfig.Builder();
		b.addResponseFilter(new ResponseFilter() {

			public FilterContextBuilder filter(FilterContext ctx) throws FilterException {

				if (ctx.getResponseStatus().getStatusCode() == 503) {
					return new FilterContextBuilder(ctx)
							.request(new RequestBuilder("GET").setUrl("http://google.com").build());
				}
			}
		});

		AsyncHttpClient c = new AsyncHttpClient(b.build());

	}
}

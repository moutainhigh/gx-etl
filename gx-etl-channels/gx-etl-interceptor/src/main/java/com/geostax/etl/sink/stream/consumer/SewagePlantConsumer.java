package com.geostax.etl.sink.stream.consumer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.FileChooserUI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SewagePlantConsumer implements Consumer {

	@Override
	public List<Observation> transform(String content) {

		List<Observation> observation_list = new ArrayList<>();
		try {
			// System.out.println(xml);
			FileUtils.writeStringToFile(new File("SewagePlant.xml"), content.replace("utf-8", "utf8"));
			String messageStr = null;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat df2 = new SimpleDateFormat("yyyy-MM");
			

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Document document = db.parse(new
			// File("xml/SewagePlantRealTimeData.xml"));

			Document document = db.parse(new File("SewagePlant.xml"));

			NodeList list = document.getElementsByTagName("subObject");
			Date current;
			for (int i = 0; i < list.getLength(); i++) {
				// Date current = df.parse("2017-05-24 14:00:00");
				Observation ob = new Observation();

				double codvalue = Double.NaN;
				double NH4value = Double.NaN;
				double pfl = Double.NaN;
				Element element = (Element) list.item(i);

				String subID = element.getElementsByTagName("SubID").item(0).getFirstChild().getNodeValue();
				current = df.parse(element.getElementsByTagName("DateTime").item(0).getFirstChild().getNodeValue());
				ob.setTimestamp(current);
				String year_month = df2.format(current);
				ob.setStationid(subID);
				Map<String, Object> values = new HashMap<>();
				values.put("cod", 0.0);
				values.put("nh4", 0.0);
				values.put("pfl", 0.0);
				if (element.getElementsByTagName("codvalue").item(0).hasChildNodes()) {
					codvalue = Double.parseDouble(
							element.getElementsByTagName("codvalue").item(0).getFirstChild().getNodeValue());
					values.put("cod", codvalue);
				}
				if (element.getElementsByTagName("NH4value").item(0).hasChildNodes()) {
					NH4value = Double.parseDouble(
							element.getElementsByTagName("NH4value").item(0).getFirstChild().getNodeValue());
					values.put("nh4", NH4value);
				}
				if (element.getElementsByTagName("pfl").item(0).hasChildNodes()) {
					pfl = Double
							.parseDouble(element.getElementsByTagName("pfl").item(0).getFirstChild().getNodeValue());
					values.put("pfl", pfl);
				}
				ob.setRecords(values);
				observation_list.add(ob);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return observation_list;
	}
}

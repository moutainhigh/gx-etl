package com.geostax.etl.interceptor.stream;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geostax.etl.Channel;
import com.geostax.etl.ChannelException;
import com.geostax.etl.Context;
import com.geostax.etl.Transaction;
import com.geostax.etl.conf.Configurable;
import com.geostax.etl.interceptor.BBoxInterceptor;
import com.geostax.etl.interceptor.Interceptor;
import com.geostax.etl.interceptor.util.JSONUtil;
import com.geostax.etl.interceptor.BBoxInterceptor.Constants;
import com.geostax.etl.sink.AbstractSink;
import com.geostax.etl.sink.stream.consumer.Consumer;
import com.geostax.etl.sink.stream.consumer.Observation;
import com.geostax.etl.sink.stream.consumer.SewagePlantConsumer;

public class GxStreamInterceptor implements Interceptor {

	private String source_name;
	private Consumer consumer = null;

	public GxStreamInterceptor() {
		// TODO Auto-generated constructor stub
	}
	
	public GxStreamInterceptor(String source_name) {
		this.source_name = source_name;
		init();
	}
	

	@Override
	public void initialize() {
		// no-op
	}

	
	public void init() {
		if (source_name.equals("SewagePlant")) {
			consumer = new SewagePlantConsumer();
		}
		else{
			consumer = new SewagePlantConsumer();
		}
	}

	/**
	 * Modifies events in-place.
	 */
	@Override
	public Event intercept(Event event) {

		if (event != null) {
			String content = new String(event.getBody());
			List<Observation> observation_list = consumer.transform(content);
			
			Map<String, String> head=event.getHeaders();
			System.out.println(JSONUtil.toJson(observation_list));
			event = EventBuilder.withBody(JSONUtil.toJson(observation_list), Charset.forName("UTF-8"));
			event.setHeaders(head);
		}
		//System.out.println(">>>>>> " + new String(event.getBody()));
		return event;
	}

	/**
	 * Delegates to {@link #intercept(Event)} in a loop.
	 * 
	 * @param events
	 * @return
	 */
	@Override
	public List<Event> intercept(List<Event> events) {
		for (Event event : events) {
			intercept(event);
		}
		return events;
	}

	@Override
	public void close() {
		// no-op
	}

	/**
	 * Builder which builds new instances of the TimestampInterceptor.
	 */
	public static class Builder implements Interceptor.Builder {

		private String sourcename="SewagePlant";

		@Override
		public Interceptor build() {
			return new GxStreamInterceptor(sourcename);
		}

		@Override
		public void configure(Context context) {
			sourcename = context.getString("sourcename");
		}

	}

}

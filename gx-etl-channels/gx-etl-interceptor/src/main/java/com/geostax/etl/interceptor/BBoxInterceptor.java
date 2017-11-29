package com.geostax.etl.interceptor;

import java.util.List;

import org.apache.flume.Event;

import com.geostax.etl.Context;

public class BBoxInterceptor implements Interceptor {

	private double north;
	private double south;
	private double east;
	private double west;

	/**
	 * Only {@link TimestampInterceptor.Builder} can build me
	 */
	private BBoxInterceptor(double north, double south, double east, double west) {
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}

	@Override
	public void initialize() {
		// no-op
	}

	/**
	 * Modifies events in-place.
	 */
	@Override
	public Event intercept(Event event) {

		String geom = new String(event.getBody());

		System.out.println(">>>>>> " + new String(event.getBody()));
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

		private double north;
		private double south;
		private double east;
		private double west;

		@Override
		public Interceptor build() {
			return new BBoxInterceptor(north, south, east, west);
		}

		@Override
		public void configure(Context context) {
			north = Double.parseDouble(context.getString(Constants.NORTH, "90.0"));
			south = Double.parseDouble(context.getString(Constants.SOUTH, "90.0"));
			east = Double.parseDouble(context.getString(Constants.EAST, "90.0"));
			west = Double.parseDouble(context.getString(Constants.WEST, "90.0"));
		}

	}

	public static class Constants {
		public static final String NORTH = "north";
		public static final String SOUTH = "south";
		public static final String EAST = "east";
		public static final String WEST = "west";

	}
}

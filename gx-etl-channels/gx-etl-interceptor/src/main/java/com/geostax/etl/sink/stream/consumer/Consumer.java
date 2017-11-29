package com.geostax.etl.sink.stream.consumer;

import java.util.List;

public interface Consumer {
	public List<Observation> transform(String content);
}

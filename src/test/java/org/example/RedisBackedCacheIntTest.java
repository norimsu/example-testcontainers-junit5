package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import redis.clients.jedis.Jedis;

class RedisBackedCacheIntTest {

	private RedisBackedCache cache;

	@BeforeEach
	public void setUp() {
		final Jedis jedis = new Jedis("localhost", 6379);

		cache = new RedisBackedCache(jedis, "test");
	}

	@Test
	void testSimplePutAndGet() {
		cache.put("foo", "bar");

		Optional<String> retrieved = cache.get("foo", String.class);

		assertTrue(retrieved.isPresent());
		assertEquals("bar", retrieved.get());
	}

	@Test
	void testNotFoundWhenNotInserted() {
		final Optional<String> retrieved = cache.get("bar", String.class);

		assertFalse(retrieved.isPresent());
	}
}
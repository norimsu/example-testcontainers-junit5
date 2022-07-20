package org.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import redis.clients.jedis.Jedis;

@Testcontainers
class RedisBackedCacheIntTest {

	@Container
	private final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

	private RedisBackedCache cache;

	@BeforeEach
	public void setUp() {
		final String address = redis.getHost();
		final Integer port = redis.getFirstMappedPort();

		final Jedis jedis = new Jedis(address, port);

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
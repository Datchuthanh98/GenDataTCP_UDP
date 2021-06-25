package com.example.demo.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisFactory {
	private static JedisPool jedisPool;
	private static JedisFactory instance;

	public JedisFactory() {
		JedisFactory.jedisPool = new JedisPool((GenericObjectPoolConfig) this.getPoolConfig(), "localhost",6379, 2000);
	}

	private JedisPoolConfig getPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(2);
		poolConfig.setMaxTotal(2);
		poolConfig.setMaxWaitMillis(2000);
		return poolConfig;
	}

	public JedisPool getJedisPool() {
		return JedisFactory.jedisPool;
	}

	public static JedisFactory getInstance() {
		if (JedisFactory.instance == null) {
			JedisFactory.instance = new JedisFactory();
		}
		return JedisFactory.instance;
	}
}
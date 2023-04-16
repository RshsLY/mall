package com.ly.mall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MallOrderApplicationTests {
	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private RabbitTemplate rabbitTemplate;


	@Test
	public void testCreateQueue() {
		Queue queue = new Queue("hello-java-queue",true,false,false);
		amqpAdmin.declareQueue(queue);
		//log.info("Queue[{}]创建成功：","hello-java-queue");
	}

}

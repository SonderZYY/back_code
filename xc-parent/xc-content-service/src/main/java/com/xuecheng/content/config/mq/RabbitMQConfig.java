package com.xuecheng.content.config.mq;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * <p></p>
 *
 */
@Configuration
public class RabbitMQConfig {


    /*
        # 课程发布 交互级名称
        course.publish.exchange = course_pub.direct
        # 课程发布 页面生成队列名称
        course.publish.queue = course_page.queue
        course.publish.routingkey= publish.page
    */
    @Value("${course.publish.exchange}")
    private String exchange;
    @Value("${course.publish.queue}")
    private String queue;
    @Value("${course.publish.routingkey}")
    private String routingkey;
    @Bean("coursePubEx")
    public DirectExchange simpleExchange(){
        // 三个参数：交换机名称、是否持久化、当没有queue与其绑定时是否自动删除
        return new DirectExchange(exchange, true, false);
    }
    @Bean("coursePubQueue")
    public Queue simpleQueue(){
        // 使用QueueBuilder构建队列，durable就是持久化的
        return QueueBuilder.durable(queue).build();
    }
    @Bean
    public Binding errorBinding(Queue coursePubQueue, DirectExchange coursePubEx){
        return BindingBuilder.bind(coursePubQueue).to(coursePubEx).with(routingkey);
    }
}
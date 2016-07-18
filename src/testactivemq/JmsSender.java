/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testactivemq;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 *
 * @author LIN NP
 */
public class JmsSender
{
  private ConnectionFactory connectionFactory = null;
  private Connection connection = null;
  private Session session = null;
  private Destination destination = null;
  private MessageProducer producer = null;
  private static final int SEND_NUMBER = 5;
  /**
   * 
   */
  public void init()
  {
    // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
    connectionFactory = new ActiveMQConnectionFactory(
        ActiveMQConnection.DEFAULT_USER,
        ActiveMQConnection.DEFAULT_PASSWORD,"tcp://121.42.189.183:61616");	// ActiveMQ默认使用的TCP连接端口是61616
    try{
      // 构造从工厂得到连接对象
      connection = connectionFactory.createConnection();
     // connection.start();
      // 获取操作连接
      session = connection.createSession(Boolean.TRUE,  Session.AUTO_ACKNOWLEDGE);
      /**
      * 第一种方式：Queue
      */
//			destination = session.createQueue("xkey");		// "xkey"可以取其他的。
//			producer = session.createProducer(destination); // 得到消息生成者【发送者】
      /**
       * 第二种方式：Topic
       */			
      Destination topic = session.createTopic("xkey.Topic");
       producer = session.createProducer(topic);
       /**
        * 
        */
      // 设置不持久化，此处学习，实际根据项目决定
      producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
      // 构造消息，此处写死，项目就是参数，或者方法获取
      sendMessage(session,producer);
      session.commit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      try 
      {
        connection.close();
      } 
      catch (JMSException e) 
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  private void sendMessage(Session session,MessageProducer producer) throws JMSException
  {
    for (int i = 1; i <= SEND_NUMBER; i ++) 
    {  
      TextMessage message = session.createTextMessage("qiuwanchi ActiveMq 发送的消息: " + i);  
      // 发送消息
      System.out.println("发送消息：qiuwanchi" + "ActiveMq 发送的消息: " + i);  
      producer.send(message);  
    }  
  }
  /**
   * @param args
   */
  public static void main(String[] args) 
  {
    // TODO Auto-generated method stub
    JmsSender jms = new JmsSender();
    jms.init();
  }
}
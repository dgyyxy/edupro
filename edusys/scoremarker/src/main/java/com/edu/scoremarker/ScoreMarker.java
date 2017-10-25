package com.edu.scoremarker;

import com.edu.common.dao.pojo.AnswerSheet;
import com.edu.scoremarker.config.ScoreMarkConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Gary on 2017/6/8.
 */
@Component
public class ScoreMarker {
    private static boolean stop = false;

    @Autowired
    private QueueingConsumer consumer;
    @Autowired
    private ScoreCalcuService scoreCalcuService;
    @Autowired
    private ObjectMapper mapper;
    private static boolean waitingForMessage = false;

    private static final Logger LOGGER = Logger.getLogger(ScoreMarker.class);

    public void init() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ScoreMarkConfig.class);
        context.refresh();
        scoreCalcuService = context.getBean(ScoreCalcuService.class);
        consumer = context.getBean(QueueingConsumer.class);
        mapper = context.getBean(ObjectMapper.class);
        LOGGER.info("ScoreMarker daemon init done.");
    }

    public ScoreMarker(){

    }
    public static void start(String[] args) {


        final ScoreMarker scoreMarker = new ScoreMarker();
        scoreMarker.init();
        System.out.println("start");
        Thread thread = new Thread(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scoreMarker.run();
            }
        };
        thread.start();
    }

    public static void stop(String[] args) {
        System.out.println("stop");
        stop = true;
        System.exit(0);
    }

    public static void main(String[] args) {
        ScoreMarker scoreMarker = new ScoreMarker();
        scoreMarker.init();
        scoreMarker.run();
    }

    protected void run() {
        while(!stop){
            try {
                LOGGER.info("scoreMaker checking next delivery from message queue");
                waitingForMessage = true;
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                waitingForMessage = false;
                AnswerSheet answerSheet = mapper.readValue(delivery.getBody(),
                        AnswerSheet.class);
                scoreCalcuService.calcuScore(answerSheet);

            } catch (ShutdownSignalException e) {
                LOGGER.error("scoreMaker received ShutdownSignalException: ", e);
                try {
                    // Sleep some time to release the CPU time slices if
                    // connection with RabbitMQ server
                    // is disconnected, otherwise CPU usage will be high if
                    // connection is disconnected.
                    Thread.sleep(100);
                } catch (Throwable ex) {
                    LOGGER.error("scoreMaker sleep exception: ", ex);
                }
            } catch (Exception e) {
                LOGGER.error("scoreMaker received exception", e);
            }
        }


    }
}

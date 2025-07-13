//package io.github.Bochakms
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import io.github.Bochakms.dto.UserEventMessage;
//import io.github.Bochakms.service.EmailService;
//
//@SpringBootTest
//@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
//class NotificationServiceIntegrationTest {
//    @Autowired
//    private KafkaTemplate<String, UserEventMessage> kafkaTemplate;
//    
//    @Autowired
//    private EmailService emailService;
//    
//    @Autowired
//    private JavaMailSender mailSender;
//    
//    @Value("${spring.kafka.consumer.group-id}")
//    private String groupId;
//    
//    @Value("${app.email.from}")
//    private String fromEmail;
//    
//    @Test
//    void whenUserCreatedEvent_thenSendWelcomeEmail() throws Exception {
//        // Подготовка тестового почтового сервера
//        GreenMail greenMail = new GreenMail(ServerSetup.SMTP);
//        greenMail.start();
//        greenMail.setUser(fromEmail, "user", "password");
//        
//        try {
//            // Отправка сообщения в Kafka
//            NotificationMessage message = new NotificationMessage(UserEvent.CREATED, "test@example.com");
//            kafkaTemplate.send("user-events", message);
//            
//            // Ожидание обработки сообщения
//            await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
//                MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
//                assertThat(receivedMessages.length).isEqualTo(1);
//                
//                MimeMessage receivedMessage = receivedMessages[0];
//                assertThat(receivedMessage.getSubject()).contains("Аккаунт успешно создан");
//                assertThat(receivedMessage.getContent().toString()).contains("был успешно создан");
//                assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("test@example.com");
//            });
//        } finally {
//            greenMail.stop();
//        }
//    }
//    
//    @Test
//    void whenSendNotificationViaApi_thenSendEmail() throws Exception {
//        // Подготовка тестового почтового сервера
//        GreenMail greenMail = new GreenMail(ServerSetup.SMTP);
//        greenMail.start();
//        greenMail.setUser(fromEmail, "user", "password");
//        
//        try {
//            // Вызов API
//            TestRestTemplate restTemplate = new TestRestTemplate();
//            ResponseEntity<String> response = restTemplate.postForEntity(
//                "http://localhost:8080/api/notifications/send",
//                new NotificationMessage(UserEvent.DELETED, "delete@example.com"),
//                String.class);
//            
//            // Проверки
//            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//            
//            await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
//                MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
//                assertThat(receivedMessages.length).isEqualTo(1);
//                
//                MimeMessage receivedMessage = receivedMessages[0];
//                assertThat(receivedMessage.getSubject()).contains("Аккаунт удален");
//                assertThat(receivedMessage.getContent().toString()).contains("был удалён");
//                assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("delete@example.com");
//            });
//        } finally {
//            greenMail.stop();
//        }
//    }
//}
package vn.edu.iuh.fit.chat_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.chat_backend.models.*;
import vn.edu.iuh.fit.chat_backend.repositories.AccountRepository;
import vn.edu.iuh.fit.chat_backend.repositories.UserRepository;
import net.datafaker.Faker;
import vn.edu.iuh.fit.chat_backend.services.MessageService;
import vn.edu.iuh.fit.chat_backend.services.UserService;
import vn.edu.iuh.fit.chat_backend.types.Gender;
import vn.edu.iuh.fit.chat_backend.types.MessageType;

import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class ChatBackEndApplicationTests {


}

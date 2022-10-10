package sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.startsWith;
import static ru.netology.entity.Country.RUSSIA;

public class MessageSenderImplTest {
        @Test
        @DisplayName("Тест проверки русского языка отправляемого сообщения, если ip относится к российскому сегменту адресов")

        void testMessageRussia() {
            Location location = new Location(null, RUSSIA, null, 0);
            GeoService geoService = Mockito.mock(GeoService.class);
            Mockito.when(geoService.byIp(startsWith("172.")))
                    .thenReturn(location);
            LocalizationService localizationService = Mockito.mock(LocalizationService.class);
            Mockito.when(localizationService.locale(RUSSIA))
                    .thenReturn("Добро пожаловать " + "\n");
            MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
            Map<String, String> headers = new HashMap<>();
            headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
            String preferences = messageSender.send(headers);
            Assertions.assertEquals("Добро пожаловать " + "\n", preferences);
        }

        @Test
        @DisplayName("Тест проверки английского языка отправляемого сообщения, если ip относится к американскому сегменту адресов")

        void testMessageUSA() {
            Location location = new Location(null, Country.USA, null, 0);
            GeoService geoService = Mockito.mock(GeoService.class);
            Mockito.when(geoService.byIp(startsWith("96.")))
                    .thenReturn(location);
            LocalizationService localizationService = Mockito.mock(LocalizationService.class);
            Mockito.when(localizationService.locale(Country.USA))
                    .thenReturn("Welcome " + "\n");
            MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
            Map<String, String> headers = new HashMap<>();
            headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.123.12.19");
            String preferences = messageSender.send(headers);
            Assertions.assertEquals("Welcome " + "\n", preferences);
        }

        @Test
        @DisplayName("Тест проверки полного IP по адресу")

        void  testMassageMoscowIp() {
            Location location = new Location("Moscow", RUSSIA, "Lenina", 15);
            GeoService geoService = Mockito.mock(GeoService.class);
            Mockito.when(geoService.byIp(startsWith("172.123.12.19.")))
                    .thenReturn(location);
            LocalizationService localizationService = Mockito.mock(LocalizationService.class);
            Mockito.when(localizationService.locale(RUSSIA))
                    .thenReturn("Добро пожаловать " + "\n");
            MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
            Map<String, String> headers = new HashMap<>();
            headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19.");
            String preferences = messageSender.send(headers);
            Assertions.assertEquals("Добро пожаловать " + "\n", preferences);
        }
    }
package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class OrderCardDeliveryTest {

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {

        var validUser = DataGenerator.Registration.generateUser("ru-RU");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // First meeting
        $("[data-test-id='city'] .input__control").setValue(validUser.getCity());
        $("[data-test-id='date'] .input__control").sendKeys(
                Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE
        );
        $("[data-test-id='date'] .input__control").setValue(firstMeetingDate);
        $("[data-test-id='name'] .input__control").setValue(validUser.getName());
        $("[data-test-id='phone'] .input__control").setValue(validUser.getPhone());
        $("[data-test-id='agreement'] span").click();
        $("button .button__text").shouldBe(Condition.text("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content").shouldBe(
                Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)
        );

        // Second meeting
        $("[data-test-id='date'] .input__control").sendKeys(
                Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE
        );
        $("[data-test-id='date'] .input__control").setValue(secondMeetingDate);
        $("button .button__text").shouldBe(Condition.text("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(
                Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15)
        );
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").shouldBe(
                Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)
        );
    }
}

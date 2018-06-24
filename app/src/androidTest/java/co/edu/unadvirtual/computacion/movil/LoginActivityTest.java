package co.edu.unadvirtual.computacion.movil;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;
import co.edu.unadvirtual.computacion.movil.iam.RegistrationActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private static final String PASSWORD_MSG = "Por favor digite su contraseña. Debe tener una letra minúscula, una letra mayúscula, un dígito y ser mayor o igual a 8 caracteres";
    private static final String EMAIL_MSG = "Debe ingresar una dirección de correo electrónico válido";

    @Rule
    public IntentsTestRule<LoginActivity> testRule = new IntentsTestRule<>(
            LoginActivity.class);

    @Test
    public void allFieldsAreRequired() {
        onView(
                withId(R.id.buttonLogin)
        ).perform(click());

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );

        onView(
                withId(R.id.editTextPassword)
        ).check(
                matches(
                        hasErrorText(PASSWORD_MSG)
                )
        );
    }

    @Test
    public void enforcePasswordPolicy01() {
        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("1")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextPassword)
        ).check(
                matches(
                        hasErrorText(PASSWORD_MSG)
                )
        );
    }

    @Test
    public void enforcePasswordPolicy02() {
        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("AAAAAAAA")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextPassword)
        ).check(
                matches(
                        hasErrorText(PASSWORD_MSG)
                )
        );
    }

    @Test
    public void enforcePasswordPolicy03() {
        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("12345678")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextPassword)
        ).check(
                matches(
                        hasErrorText(PASSWORD_MSG)
                )
        );
    }

    @Test
    public void enforcePasswordPolicy04() {
        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("1234aaaa")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextPassword)
        ).check(
                matches(
                        hasErrorText(PASSWORD_MSG)
                )
        );
    }

    @Test
    public void enforceEmailPolicy01() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pablo")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );
    }

    @Test
    public void enforceEmailPolicy02() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pablo@")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );
    }

    @Test
    public void enforceEmailPolicy03() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pablo@pablo")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );
    }

    @Test
    public void enforceEmailPolicy04() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("@pablo")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );
    }

    @Test
    public void unsuccessfulLogin01() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pablosalgado@gmail.com")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("Unad2018")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withText("Credenciales no válidas.")
        ).inRoot(
                withDecorView(
                        not(
                                testRule.getActivity().getWindow().getDecorView()
                        )
                )
        ).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void unsuccessfulLogin02() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pabloasalgado@gmail.com")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("Unad2017")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        onView(
                withText("Credenciales no válidas.")
        ).inRoot(
                withDecorView(
                        not(
                                testRule.getActivity().getWindow().getDecorView()
                        )
                )
        ).check(
                matches(
                        isDisplayed()
                )
        );
    }

    @Test
    public void successfulLogin() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pabloasalgado@gmail.com")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("Unad2017")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonLogin)
        ).perform(
                click()
        );

        intending(
                allOf(
                        hasComponent(hasShortClassName(".MainActivity")),
                        toPackage("co.edu.unadvirtual.computacion.movil")
                )
        );
    }

    @Test
    public void loadRegistrationActivity() {
        onView(
                withId(R.id.buttonRegister)
        ).perform(
                click()
        );

        intending(
                allOf(
                        hasComponent(hasShortClassName(".RegistrationActivity")),
                        toPackage("co.edu.unadvirtual.computacion.movil.aim")
                )
        );
    }

    @Test
    public void loadPasswordRecoveryActivity() {
        onView(
                withId(R.id.buttonRecovery)
        ).perform(
                click()
        );

        intending(
                allOf(
                        hasComponent(hasShortClassName(".PasswordRecoveryActivity")),
                        toPackage("co.edu.unadvirtual.computacion.movil.aim")
                )
        );
    }
}

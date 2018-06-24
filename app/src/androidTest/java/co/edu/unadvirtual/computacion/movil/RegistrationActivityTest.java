package co.edu.unadvirtual.computacion.movil;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.edu.unadvirtual.computacion.movil.iam.RegistrationActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {

    private static final String PASSWORD_MSG = "Por favor digite su contraseña. Debe tener una letra minúscula, una letra mayúscula, un dígito y ser mayor o igual a 8 caracteres";
    private static final String FIRST_NAME_MSG = "Debe ingresar su(s) nombre(s)";
    private static final String LAST_NAME_MSG = "Debe ingresar su(s) apellido(s)";
    private static final String EMAIL_MSG = "Debe ingresar una dirección de correo electrónico válido";

    @Rule
    public ActivityTestRule<RegistrationActivity> testRule = new ActivityTestRule<>(
            RegistrationActivity.class);

    @Test
    public void allFieldsAreRequired() {
        onView(
                withId(R.id.buttonSend)
        ).perform(click());

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
                )
        );

        onView(
                withId(R.id.editTextFirstName)
        ).check(
                matches(
                        hasErrorText(FIRST_NAME_MSG)
                )
        );

        onView(
                withId(R.id.editTextLastName)
        ).check(
                matches(
                        hasErrorText(LAST_NAME_MSG)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
    public void enforceFirstNamePolicy01() {
        onView(
                withId(R.id.editTextFirstName)
        ).perform(
                typeText("p")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonSend)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextFirstName)
        ).check(
                matches(
                        hasErrorText(FIRST_NAME_MSG)
                )
        );
    }

    @Test
    public void enforceFirstNamePolicy02() {
        onView(
                withId(R.id.editTextFirstName)
        ).perform(
                typeText("pp")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonSend)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextFirstName)
        ).check(
                matches(
                        hasErrorText(FIRST_NAME_MSG)
                )
        );
    }

    @Test
    public void enforceLastNamePolicy01() {
        onView(
                withId(R.id.editTextLastName)
        ).perform(
                typeText("p")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonSend)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextLastName)
        ).check(
                matches(
                        hasErrorText(LAST_NAME_MSG)
                )
        );
    }

    @Test
    public void enforceLastNamePolicy02() {
        onView(
                withId(R.id.editTextLastName)
        ).perform(
                typeText("pp")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonSend)
        ).perform(
                click()
        );

        onView(
                withId(R.id.editTextLastName)
        ).check(
                matches(
                        hasErrorText(LAST_NAME_MSG)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
                withId(R.id.buttonSend)
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
    public void alreadyRegistered() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pabloasalgado@gmail.com")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextPassword)
        ).perform(
                typeText("Ab123456")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextFirstName)
        ).perform(
                typeText("Pablo")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.editTextLastName)
        ).perform(
                typeText("Salgado")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonSend)
        ).perform(
                click()
        );

        onView(
                withText("La dirección de correo ya está registrada.")
        ).inRoot(
                withDecorView(
                        not(
                                testRule.getActivity().getWindow().getDecorView()
                        )
                )
        ) .check(
                matches(
                        isDisplayed()
                )
        );
    }
}
package co.edu.unadvirtual.computacion.movil;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.edu.unadvirtual.computacion.movil.iam.LoginActivity;
import co.edu.unadvirtual.computacion.movil.iam.PasswordRecoveryActivity;

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
public class PasswordRecoveryActivityTest {
    private static final String EMAIL_MSG = "Debe ingresar una dirección de correo electrónico válido";

    @Rule
    public IntentsTestRule<PasswordRecoveryActivity> testRule = new IntentsTestRule<>(
            PasswordRecoveryActivity.class);

    @Test
    public void allFieldsAreRequired() {
        onView(
                withId(R.id.buttonPasswordRecovery)
        ).perform(click());

        onView(
                withId(R.id.editTextEmail)
        ).check(
                matches(
                        hasErrorText(EMAIL_MSG)
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
                withId(R.id.buttonPasswordRecovery)
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
                withId(R.id.buttonPasswordRecovery)
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
                withId(R.id.buttonPasswordRecovery)
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
                withId(R.id.buttonPasswordRecovery)
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
    public void successfulRecovery() {
        onView(
                withId(R.id.editTextEmail)
        ).perform(
                typeText("pabloasalgado@gmail.com")
        );

        closeSoftKeyboard();

        onView(
                withId(R.id.buttonPasswordRecovery)
        ).perform(
                click()
        );

        onView(
                withText("Te hemos enviado un correo electrónico a la dirección registrada con las instrucciones para recuperar el acceso")
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
}

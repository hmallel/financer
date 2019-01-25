package de.raphaelmuesseler.financer.client.javafx.main;

import com.jfoenix.controls.JFXListView;
import de.raphaelmuesseler.financer.client.format.I18N;
import de.raphaelmuesseler.financer.shared.model.BaseCategory;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class FixedTransactionTest extends AbstractFinancerApplicationTest {

    @BeforeEach
    public void setUpEach() throws Exception {
        super.setUpEach();
        ApplicationTest.launch(FinancerApplication.class);
    }

    @Test
    public void testAddFixedTransaction() {
        register(user, password);
        category.setCategoryClass(BaseCategory.CategoryClass.FIXED_EXPENSES);
        addCategory(category);
        addFixedTransaction(fixedTransaction);

        sleep(500);
        // TODO: related to issue #14
//        clickOn(find((Label label) -> label.getText().contains(fixedTransaction.getCategoryTree().getValue().getName())));
//        sleep(500);

        clickOn((JFXListView) find("#categoriesListView"));
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);
        press(KeyCode.DOWN).release(KeyCode.DOWN);

        Assertions.assertNotNull(find((Label label) -> label.getText().contains(I18N.get("active"))));
        Assertions.assertNotNull(find((Label label) -> label.getText().contains((-fixedTransaction.getAmount() + "0")
                .replace(".", ","))));
        Assertions.assertNotNull(find((Label label) -> label.getText().contains(I18N.get("since") + " " +
                fixedTransaction.getStartDate().toString())));
    }
}

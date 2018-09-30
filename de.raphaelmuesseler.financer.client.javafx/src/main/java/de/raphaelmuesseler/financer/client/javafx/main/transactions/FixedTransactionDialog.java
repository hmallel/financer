package de.raphaelmuesseler.financer.client.javafx.main.transactions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.sun.javafx.scene.control.IntegerField;
import de.raphaelmuesseler.financer.client.format.Formatter;
import de.raphaelmuesseler.financer.client.format.I18N;
import de.raphaelmuesseler.financer.client.javafx.components.DoubleField;
import de.raphaelmuesseler.financer.client.javafx.dialogs.FinancerDialog;
import de.raphaelmuesseler.financer.shared.model.Category;
import de.raphaelmuesseler.financer.shared.model.transactions.FixedTransaction;
import de.raphaelmuesseler.financer.shared.model.transactions.TransactionAmount;
import de.raphaelmuesseler.financer.shared.util.collections.SerialTreeItem;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.time.LocalDate;

public class FixedTransactionDialog extends FinancerDialog<FixedTransaction> {

    private Category category;
    private SerialTreeItem<Category> tree;
    private Label categoryLabel;
    private IntegerField dayField;
    private JFXDatePicker startDateField, endDateField;
    private CheckBox isVariableCheckbox;
    private DoubleField amountField;
    private VBox transactionAmountContainer;
    private JFXListView<TransactionAmount> transactionAmountListView;

    public FixedTransactionDialog(FixedTransaction value, Category category) {
        super(value);

        this.category = category;

        this.setHeaderText(I18N.get("fixedTransactions"));

        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        this.getDialogPane().getButtonTypes().add(ButtonType.OK);

        this.prepareDialogContent();
    }

    @Override
    protected Node setDialogContent() {
        HBox hBox = new HBox();
        hBox.setSpacing(15);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(120);
        gridPane.setVgap(10);

        gridPane.add(new Label(I18N.get("category")), 0, 0);
        this.categoryLabel = new Label();
        gridPane.add(this.categoryLabel, 1, 0);

        gridPane.add(new Label(I18N.get("valueDate")), 0, 1);
        this.dayField = new IntegerField();
        this.dayField.setMaxValue(30);
        gridPane.add(this.dayField, 1, 1);

        gridPane.add(new Label(I18N.get("startDate")), 0, 2);
        this.startDateField = new JFXDatePicker();
        this.startDateField.setValue(LocalDate.now());
        gridPane.add(this.startDateField, 1, 2);

        gridPane.add(new Label(I18N.get("endDate")), 0, 3);
        this.endDateField = new JFXDatePicker();
        gridPane.add(this.endDateField, 1, 3);

        gridPane.add(new Label(I18N.get("isVariable")), 0, 4);
        this.isVariableCheckbox = new CheckBox();
        this.isVariableCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (this.amountField != null) {
                this.amountField.setDisable(newValue);
            }
            if (this.getDialogPane() != null) {
                Platform.runLater(() -> {
                    toggleTransactionAmountContainer();
                    getDialogPane().getScene().getWindow().sizeToScene();
                });
            }
        });
        gridPane.add(this.isVariableCheckbox, 1, 4);

        gridPane.add(new Label(I18N.get("amount")), 0, 5);
        this.amountField = new DoubleField();
        gridPane.add(this.amountField, 1, 5);

        hBox.getChildren().add(gridPane);

        this.transactionAmountContainer = new VBox();
        this.transactionAmountContainer.setSpacing(10);
        this.transactionAmountContainer.setPrefHeight(200);
        this.transactionAmountContainer.getChildren().add(new Label(I18N.get("transactionAmounts")));

        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        JFXButton newTransactionAmountBtn = new JFXButton(I18N.get("new"), fontAwesome.create(FontAwesome.Glyph.PLUS));
        JFXButton editTransactionAmountBtn = new JFXButton(I18N.get("edit"), fontAwesome.create(FontAwesome.Glyph.EDIT));
        JFXButton deleteTransactionAmountBtn = new JFXButton(I18N.get("delete"), fontAwesome.create(FontAwesome.Glyph.TRASH));

        newTransactionAmountBtn.setOnAction(event -> {
            TransactionAmount transactionAmount = new TransactionAmountDialog(null, getValue().getTransactionAmounts()).showAndGetResult();
            if (transactionAmount != null) {
                transactionAmountListView.getItems().add(transactionAmount);
                getValue().getTransactionAmounts().add(transactionAmount);
                getValue().sortTransactionAmounts();
            }
        });
        editTransactionAmountBtn.setOnAction(event -> {
            if (transactionAmountListView.getSelectionModel().getSelectedItem() != null) {
                TransactionAmount transactionAmount = new TransactionAmountDialog(transactionAmountListView.getSelectionModel().getSelectedItem(),
                        getValue().getTransactionAmounts())
                        .showAndGetResult();
                if (transactionAmount != null) {

                    for (int i = 0; i < getValue().getTransactionAmounts().size(); i++) {
                        if (transactionAmount.getId() == getValue().getTransactionAmounts().get(i).getId()) {
                            getValue().getTransactionAmounts().get(i).setValueDate(transactionAmount.getValueDate());
                            getValue().getTransactionAmounts().get(i).setAmount(transactionAmount.getAmount());
                            getValue().sortTransactionAmounts();
                            break;
                        }
                    }
                }
            }
        });
        deleteTransactionAmountBtn.setOnAction(event -> {
            if (transactionAmountListView.getSelectionModel().getSelectedItem() != null) {
                transactionAmountListView.getItems().remove(transactionAmountListView.getSelectionModel().getSelectedItem());
                getValue().getTransactionAmounts().remove(transactionAmountListView.getSelectionModel().getSelectedItem());
            }
        });

        HBox toolBox = new HBox();
        toolBox.setSpacing(8);
        toolBox.getChildren().add(newTransactionAmountBtn);
        toolBox.getChildren().add(editTransactionAmountBtn);
        toolBox.getChildren().add(deleteTransactionAmountBtn);

        this.transactionAmountContainer.getChildren().add(toolBox);

        this.transactionAmountListView = new JFXListView<>();
        this.transactionAmountListView.setCellFactory(param -> new ListCell<TransactionAmount>() {
            @Override
            protected void updateItem(TransactionAmount item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    BorderPane borderPane = new BorderPane();
                    borderPane.getStyleClass().add("transactions-list-item");
                    borderPane.setLeft(new Label(item.getValueDate().toString()));
                    Label amountLabel = new Label(Double.toString(item.getAmount()));
                    if (item.getAmount() < 0) {
                        amountLabel.getStyleClass().add("neg-amount");
                    } else {
                        amountLabel.getStyleClass().add("pos-amount");
                    }
                    borderPane.setRight(amountLabel);
                    setGraphic(borderPane);
                }
            }
        });
        this.transactionAmountContainer.getChildren().add(this.transactionAmountListView);
        hBox.getChildren().add(this.transactionAmountContainer);

        return hBox;
    }

    @Override
    protected void prepareDialogContent() {
        this.categoryLabel.setText(Formatter.formatCategoryName(this.category));
        if (this.getValue() != null) {
            this.dayField.setValue(this.getValue().getDay());
            this.startDateField.setValue(this.getValue().getStartDate());
            this.endDateField.setValue(this.getValue().getEndDate());
            if (this.getValue().isVariable()) {
                this.isVariableCheckbox.setSelected(this.getValue().isVariable());
                this.toggleTransactionAmountContainer(false);

                if (this.getValue().getTransactionAmounts() != null && this.getValue().getTransactionAmounts().size() > 0) {
                    this.transactionAmountListView.getItems().addAll(this.getValue().getTransactionAmounts());
                }
                this.amountField.setDisable(true);
            } else {
                this.amountField.setText(Double.toString(this.getValue().getAmount()));
                this.toggleTransactionAmountContainer(false);
            }
        } else {
            this.toggleTransactionAmountContainer(false);
        }
    }

    private void toggleTransactionAmountContainer() {
        this.transactionAmountContainer.setManaged(!this.transactionAmountContainer.isManaged());
        this.transactionAmountContainer.setVisible(!this.transactionAmountContainer.isVisible());
    }

    private void toggleTransactionAmountContainer(boolean visible) {
        this.transactionAmountContainer.setManaged(visible);
        this.transactionAmountContainer.setVisible(visible);
    }

    @Override
    protected boolean checkConsistency() {
        return true;
    }

    @Override
    protected FixedTransaction onConfirm() {
        if (this.getValue() == null) {
            this.setValue(new FixedTransaction(-1,
                    Double.valueOf(this.amountField.getText()),
                    this.category,
                    "", "",
                    this.startDateField.getValue(),
                    this.endDateField.getValue(),
                    this.isVariableCheckbox.isSelected(),
                    this.dayField.getValue(),
                    (this.isVariableCheckbox.isSelected() ? this.getValue().getTransactionAmounts() : null)));
        } else {
            this.getValue().setStartDate(this.startDateField.getValue());
            this.getValue().setEndDate(this.endDateField.getValue());
            this.getValue().setVariable(this.isVariableCheckbox.isSelected());
            this.getValue().setDay(this.dayField.getValue());
            this.getValue().setAmount(Double.valueOf(this.amountField.getText()));
        }

        return super.onConfirm();
    }
}
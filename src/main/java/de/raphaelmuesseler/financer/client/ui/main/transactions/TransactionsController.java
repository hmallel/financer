package de.raphaelmuesseler.financer.client.ui.main.transactions;

import com.jfoenix.controls.JFXButton;
import de.raphaelmuesseler.financer.client.connection.ServerRequestHandler;
import de.raphaelmuesseler.financer.client.local.LocalStorage;
import de.raphaelmuesseler.financer.client.ui.I18N;
import de.raphaelmuesseler.financer.shared.connection.AsyncConnectionCall;
import de.raphaelmuesseler.financer.shared.connection.ConnectionResult;
import de.raphaelmuesseler.financer.shared.model.Category;
import de.raphaelmuesseler.financer.shared.model.User;
import de.raphaelmuesseler.financer.shared.model.transactions.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionsController implements Initializable {


    public JFXButton refreshTransactionsBtn;
    public JFXButton newTransactionBtn;
    public JFXButton editTransactionBtn;
    public JFXButton deleteTransactionBtn;
    public TableView<Transaction> transactionsTableView;

    private User user;
    private Logger logger = Logger.getLogger("FinancerApplication");
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ObservableList<Transaction> transactions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.user = LocalStorage.getLoggedInUser();

        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
        this.refreshTransactionsBtn.setGraphic(fontAwesome.create(FontAwesome.Glyph.REFRESH));
        this.refreshTransactionsBtn.setGraphicTextGap(8);
        this.newTransactionBtn.setGraphic(fontAwesome.create(FontAwesome.Glyph.PLUS));
        this.newTransactionBtn.setGraphicTextGap(8);
        this.editTransactionBtn.setGraphic(fontAwesome.create(FontAwesome.Glyph.EDIT));
        this.editTransactionBtn.setGraphicTextGap(8);
        this.deleteTransactionBtn.setGraphic(fontAwesome.create(FontAwesome.Glyph.TRASH));
        this.deleteTransactionBtn.setGraphicTextGap(8);

        TableColumn<Transaction, Integer> idColumn = new TableColumn<>(I18N.get("id"));
        TableColumn<Transaction, Date> valueDateColumn = new TableColumn<>(I18N.get("valueDate"));
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>(I18N.get("amount"));
        TableColumn<Transaction, Category> categoryColumn = new TableColumn<>(I18N.get("category"));
        TableColumn<Transaction, String> productColumn = new TableColumn<>(I18N.get("product"));
        TableColumn<Transaction, String> purposeColumn = new TableColumn<>(I18N.get("purpose"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")).;
        valueDateColumn.setCellValueFactory(new PropertyValueFactory<>("valueDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));

        idColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(18));
        valueDateColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(6));
        amountColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(12));
        categoryColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(6));
        productColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(4));
        purposeColumn.prefWidthProperty().bind(this.transactionsTableView.widthProperty().divide(4));

        valueDateColumn.setSortType(TableColumn.SortType.DESCENDING);
        this.transactionsTableView.getColumns().addAll(idColumn, valueDateColumn, amountColumn, categoryColumn, productColumn, purposeColumn);

        this.handleRefreshTransactions();
    }

    public void handleRefreshTransactions() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", this.user);

        this.executor.execute(new ServerRequestHandler("getTransactions", parameters, new AsyncConnectionCall() {
            @Override
            public void onSuccess(ConnectionResult result) {
                transactions = FXCollections.observableArrayList((List<Transaction>) result.getResult());
                Platform.runLater(() -> transactionsTableView.setItems(transactions));
            }

            @Override
            public void onFailure(Exception exception) {
                logger.log(Level.SEVERE, exception.getMessage(), exception);
                AsyncConnectionCall.super.onFailure(exception);
            }
        }));
    }

    public void handleNewTransaction() {

    }

    public void handleEditTransaction() {

    }

    public void handleDeleteTransaction() {

    }
}
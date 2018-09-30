package de.raphaelmuesseler.financer.client.javafx.dialogs;

import de.raphaelmuesseler.financer.client.format.Formatter;
import de.raphaelmuesseler.financer.shared.exceptions.FinancerException;

import java.net.ConnectException;
import java.net.UnknownHostException;

public class FinancerExceptionDialog extends FinancerAlert {
    public FinancerExceptionDialog(String header, Exception exception) {
        super(AlertType.ERROR);

        this.setHeaderText(header);

        String message = "Something went wrong. Please try again later.";
        try {
            throw exception.getCause();
        } catch (UnknownHostException e) {
            message = "The database is not available at the moment. Please try again later";
        } catch (ConnectException connectException) {
            message = "Server is currently not available. Please try again later.";
        } catch (FinancerException financerException) {
            message = Formatter.formatExceptionMessage(financerException);
        } catch (Throwable throwable) {
        } finally {
            this.setContentText(message);
        }
    }
}
package de.raphaelmuesseler.financer.client.connection;

import de.raphaelmuesseler.financer.shared.connection.ConnectionResult;
import de.raphaelmuesseler.financer.shared.model.user.User;

import java.util.Map;

public class ServerRequestHandler implements Runnable {

    private ServerRequest serverRequest;
    private AsyncConnectionCall asyncCall;

    public ServerRequestHandler(String methodName, Map<String, Object> parameters, AsyncConnectionCall asyncCall) {
        this(new ServerRequest(methodName, parameters), asyncCall);
    }

    public ServerRequestHandler(User user, String methodName, Map<String, Object> parameters, AsyncConnectionCall asyncCall) {
        this(new ServerRequest(user, methodName, parameters), asyncCall);
    }

    private ServerRequestHandler(ServerRequest serverRequest, AsyncConnectionCall asyncCall) {
        this.serverRequest = serverRequest;
        this.asyncCall = asyncCall;
    }

    @Override
    public void run() {
        this.asyncCall.onBefore();
        try {
            ConnectionResult result = this.serverRequest.make();
            if (result.getException() == null) {
                this.asyncCall.onSuccess(result);
            } else {
                this.asyncCall.onFailure(result.getException());
            }
        } catch (Exception e) {
            this.asyncCall.onFailure(e);
        } finally {
            this.asyncCall.onAfter();
        }
    }
}

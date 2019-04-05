package vehicle.domain.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import common.config.Config;
import io.javalin.NotFoundResponse;

public class LicensePlateService implements ILicensePlateService{

    @Override
    public String getData(String licensePlate) {
        GetRequest getRequest = Unirest.get("https://v1.motorapi.dk/vehicles/" + licensePlate);
        getRequest.header("X-AUTH-TOKEN", Config.getInstance().getProperty("motor.apikey"));

        try {
            HttpResponse<String> httpResponse = getRequest.asString();
            if(httpResponse.getStatus() == 404) {
                throw new NotFoundResponse(httpResponse.getStatusText());
            }

            return httpResponse.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return "";
    }
}
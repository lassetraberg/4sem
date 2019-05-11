package grp4.vehicle.domain.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import grp4.common.config.Config;
import io.javalin.NotFoundResponse;

import java.util.HashMap;
import java.util.Map;

public class LicensePlateService implements ILicensePlateService {
    private Map<String, String> dataCache;

    public LicensePlateService() {
        this.dataCache = new HashMap<>();
    }

    @Override
    public String getData(String licensePlate) {
        String data = "";

        if (dataCache.containsKey(licensePlate)) {
            data = dataCache.get(licensePlate);
        } else {

            GetRequest getRequest = Unirest.get("https://v1.motorapi.dk/vehicles/" + licensePlate);
            getRequest.header("X-AUTH-TOKEN", Config.getInstance().getProperty("motor.apikey"));

            try {
                HttpResponse<String> httpResponse = getRequest.asString();
                if (httpResponse.getStatus() == 404) {
                    throw new NotFoundResponse(httpResponse.getStatusText());
                }

                data = httpResponse.getBody();
                dataCache.put(licensePlate, data);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
}
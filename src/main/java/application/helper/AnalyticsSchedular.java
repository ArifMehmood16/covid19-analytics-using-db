package application.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

@Component
public class AnalyticsSchedular {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static void downloadFile(URL url, String outputFileName) throws IOException {
        try (InputStream in = url.openStream();
             ReadableByteChannel rbc = Channels.newChannel(in);
             FileOutputStream fos = new FileOutputStream(outputFileName)) {
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    @Scheduled(fixedRate = 4320000)
    public void prepareData() {
        LOGGER.info("Scheduled Task: Data downloading and collection.");
        String urlCovid = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
        try {
            URL url = new URL(urlCovid);
            downloadFile(url, Paths.get("target", "data.csv").toString());
        } catch (MalformedURLException e) {
            LOGGER.error(e.getStackTrace().toString());
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
    }

}
